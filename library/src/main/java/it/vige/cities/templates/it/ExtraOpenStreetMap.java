package it.vige.cities.templates.it;

import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;

import java.util.Set;

import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.OpenStreetMap;

/**
 * Extra configuration for italian OpenStreetMap
 * Adds macroregions (level 0) grouping regions by geographical area
 * 
 * @author lucastancapiano
 */
public class ExtraOpenStreetMap extends OpenStreetMap {

	/**
	 * Italian region names in North-West macroregion (language-independent, normalized)
	 */
	private static final Set<String> NORD_OCCIDENTALE = Set.of(
		"Piemonte", "Liguria", "Valle d'Aosta", "Valle d’Aosta", "Lombardia"
	);
	
	/**
	 * Italian region names in North-East macroregion (language-independent, normalized)
	 */
	private static final Set<String> NORD_ORIENTALE = Set.of(
		"Emilia-Romagna", "Veneto", "Trentino-Alto Adige", "Friuli Venezia Giulia"
	);
	
	/**
	 * Italian region names in Central macroregion (language-independent, normalized)
	 */
	private static final Set<String> CENTRALE = Set.of(
		"Lazio", "Marche", "Umbria", "Toscana"
	);
	
	/**
	 * Italian region names in Southern macroregion (language-independent, normalized)
	 */
	private static final Set<String> MERIDIONALE = Set.of(
		"Abruzzo", "Campania", "Basilicata", "Molise", "Calabria", "Puglia"
	);
	
	/**
	 * Italian region names in Insular macroregion (language-independent, normalized)
	 */
	private static final Set<String> INSULARE = Set.of(
		"Sardegna", "Sicilia"
	);

	/**
	 * Constructor for ExtraOpenStreetMap template
	 * Adds Italian macroregions (level 0) grouping regions by geographical area
	 * Sets firstLevel to 1 (regions) since macroregions are added separately
	 * 
	 * @param country         the country code (ISO 3166-1 alpha-2)
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 * @param language        the language enum for location name translations
	 */
	public ExtraOpenStreetMap(String country, boolean caseSensitive, boolean duplicatedNames, Languages language) {
		super(country, caseSensitive, duplicatedNames, language);
		firstLevel = 1;
	}

	/**
	 * Constructor for ExtraOpenStreetMap template (convenience method accepting String)
	 * Accepts language as a string code and converts it to Languages enum
	 * 
	 * @param country         the country code (ISO 3166-1 alpha-2)
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 * @param language        the language code (e.g., "it", "en", "fr")
	 */
	public ExtraOpenStreetMap(String country, boolean caseSensitive, boolean duplicatedNames, String language) {
		this(country, caseSensitive, duplicatedNames, Languages.fromCode(language));
	}

	/**
	 * Check if the template supports the given language
	 * ExtraOpenStreetMap template supports IT, EN, DE, FR, ES, PT
	 */
	@Override
	public boolean isLanguageSupported(Languages language) {
		return language == Languages.IT || 
			   language == Languages.EN || 
			   language == Languages.DE || 
			   language == Languages.FR || 
			   language == Languages.ES || 
			   language == Languages.PT;
	}

	/**
	 * Check if a region name matches a macroregion
	 * @param regionName the region name (may be in different languages)
	 * @param macroregionId the macroregion identifier (1-5)
	 * @return true if the region belongs to the macroregion
	 */
	private boolean matchesMacroregion(String regionName, int macroregionId) {
		if (regionName == null || regionName.isEmpty()) {
			return false;
		}
		
		// Normalize region name (remove extra spaces, handle variations)
		String normalizedName = regionName.trim();
		
		Set<String> macroregionSet;
		switch (macroregionId) {
		case 1:
			macroregionSet = NORD_OCCIDENTALE;
			break;
		case 2:
			macroregionSet = NORD_ORIENTALE;
			break;
		case 3:
			macroregionSet = CENTRALE;
			break;
		case 4:
			macroregionSet = MERIDIONALE;
			break;
		case 5:
			macroregionSet = INSULARE;
			break;
		default:
			return false;
		}
		
		// Check exact match (case-insensitive)
		// Also check if the normalized name contains any of the region names
		// This handles cases where OSM returns names in different languages
		String normalizedLower = normalizedName.toLowerCase();
		return macroregionSet.stream()
				.anyMatch(r -> r.equalsIgnoreCase(normalizedName) || 
							   normalizedLower.contains(r.toLowerCase()) ||
							   r.toLowerCase().contains(normalizedLower));
	}

	/**
	 * Generate cities data with Italian macroregions
	 * Adds macroregions (level 0) and groups regions from OpenStreetMap under them
	 * 
	 * @return ResultNodes with OK result and generated nodes
	 * @throws Exception if there is a problem generating data from OpenStreetMap
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		Nodes nodes = new Nodes();
		addLevel0(nodes, caseSensitive);
		Nodes nodesFromOsm = super.generate().getNodes();
		
		for (Node node0 : nodes.getZones()) {
			for (Node node1 : nodesFromOsm.getZones()) {
				String regionName = node1.getName();
				int macroregionId = Integer.parseInt(node0.getId());
				
				if (matchesMacroregion(regionName, macroregionId)) {
					// Increment level: regions should be level 1 (macroregions are level 0)
					incrementLevels(node1, 1);
					node0.getZones().add(node1);
				}
			}
			changeIds(node0);
		}
		return new ResultNodes(OK, nodes, this);
	}

	/**
	 * Recursively increment levels of a node and its children
	 * @param node the node to increment
	 * @param increment the amount to increment by
	 */
	private void incrementLevels(Node node, int increment) {
		if (node != null) {
			node.setLevel(node.getLevel() + increment);
			if (node.getZones() != null) {
				for (Node child : node.getZones()) {
					incrementLevels(child, increment);
				}
			}
		}
	}

	/**
	 * Recursively change node IDs to include parent prefix
	 * Modifies child node IDs to include the parent ID prefix separated by ID_SEPARATOR
	 * 
	 * @param node the parent node whose children's IDs should be modified
	 */
	private void changeIds(Node node) {
		node.getZones().parallelStream().forEach(x -> {
			String prefix = node.getId();
			String suffix = x.getId();
			// Handle prefix: if it contains ID_SEPARATOR, take only the first part
			if (prefix.indexOf(ID_SEPARATOR) > 0) {
				prefix = prefix.substring(0, prefix.indexOf(ID_SEPARATOR));
			}
			// Handle suffix: if it contains ID_SEPARATOR, take from the separator onwards
			// Otherwise, add the separator before the suffix
			if (suffix.indexOf(ID_SEPARATOR) > 0) {
				suffix = suffix.substring(suffix.indexOf(ID_SEPARATOR));
			} else {
				suffix = ID_SEPARATOR + suffix;
			}
			x.setId(prefix + suffix);
			changeIds(x);
		});
	}

	/**
	 * Add level 0 nodes (macroregions) using centralized utility
	 * 
	 * @param nodes         the nodes
	 * @param caseSensitive true if it is case sensitive
	 */
	private void addLevel0(Nodes nodes, boolean caseSensitive) {
		ItalianMacroregions.addLevel0(nodes, caseSensitive, language);
	}

}

