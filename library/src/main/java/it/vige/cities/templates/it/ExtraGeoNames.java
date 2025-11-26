package it.vige.cities.templates.it;

import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static java.lang.Integer.parseInt;

import java.util.Set;

import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.GeoNames;

/**
 * Extra configuration for italian geonames
 * 
 * @author lucastancapiano
 */
public class ExtraGeoNames extends GeoNames {

	/**
	 * GeoNames IDs for Italian regions in North-West macroregion (language-independent)
	 */
	private static final Set<Integer> NORD_OCCIDENTALE = Set.of(
		3170831, // Piemonte
		3174725, // Liguria
		3164857, // Valle d'Aosta
		3174618  // Lombardia
	);
	
	/**
	 * GeoNames IDs for Italian regions in North-East macroregion (language-independent)
	 */
	private static final Set<Integer> NORD_ORIENTALE = Set.of(
		3177401, // Emilia-Romagna
		3164604, // Veneto
		3165244, // Trentino-Alto Adige
		3176525  // Friuli Venezia Giulia
	);
	
	/**
	 * GeoNames IDs for Italian regions in Central macroregion (language-independent)
	 */
	private static final Set<Integer> CENTRALE = Set.of(
		3174976, // Lazio
		3174004, // Marche
		3165048, // Umbria
		3165361  // Toscana
	);
	
	/**
	 * GeoNames IDs for Italian regions in Southern macroregion (language-independent)
	 */
	private static final Set<Integer> MERIDIONALE = Set.of(
		3183560, // Abruzzo
		3181042, // Campania
		3182306, // Basilicata
		3173222, // Molise
		2525468, // Calabria
		3169778  // Puglia
	);
	
	/**
	 * GeoNames IDs for Italian regions in Insular macroregion (language-independent)
	 */
	private static final Set<Integer> INSULARE = Set.of(
		2523228, // Sardegna
		2523119  // Sicilia
	);

	/**
	 * Constructor for ExtraGeoNames template
	 * Uses default language (IT - Italian)
	 * 
	 * @param country         the country code (ISO 3166-1 alpha-2)
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 * @param username        the GeoNames API username (null to use default)
	 */
	public ExtraGeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username) {
		this(country, caseSensitive, duplicatedNames, username, Languages.getDefault());
	}

	/**
	 * Constructor for ExtraGeoNames template with language
	 * Adds Italian macroregions (level 0) grouping regions by geographical area
	 * 
	 * @param country         the country code (ISO 3166-1 alpha-2)
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 * @param username        the GeoNames API username (null to use default)
	 * @param language        the language enum for location name translations
	 */
	public ExtraGeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username, Languages language) {
		super(country, caseSensitive, duplicatedNames, username, language);
		firstLevel = 1;
	}

	/**
	 * ExtraGeoNames (convenience method accepting String)
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames true if it accepts duplicated names
	 * @param username        the username
	 * @param language        the language code (e.g., "it", "en")
	 */
	public ExtraGeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username, String language) {
		this(country, caseSensitive, duplicatedNames, username, Languages.fromCode(language));
	}

	/**
	 * Check if the template supports the given language
	 * ExtraGeoNames template supports IT, EN, DE, FR, ES, PT
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
	 * Extract geonameId from node ID
	 * @param nodeId the node ID (format: "geonameId" or "parent-geonameId")
	 * @return the geonameId as integer
	 */
	private int extractGeonameId(String nodeId) {
		if (nodeId == null || nodeId.isEmpty()) {
			return -1;
		}
		String[] parts = nodeId.split(ID_SEPARATOR);
		return parseInt(parts[parts.length - 1]);
	}

	/**
	 * Generate cities data with Italian macroregions
	 * Adds macroregions (level 0) and groups regions from GeoNames under them based on GeoNames IDs
	 * 
	 * @return ResultNodes with OK result and generated nodes
	 * @throws Exception if there is a problem generating data from GeoNames
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		Nodes nodes = new Nodes();
		addLevel0(nodes, caseSensitive);
		Nodes nodesFromGeoname = super.generate().getNodes();
		for (Node node0 : nodes.getZones()) {
			for (Node node1 : nodesFromGeoname.getZones()) {
				int geonameId = extractGeonameId(node1.getId());
				boolean matches = false;
				
				// Use node ID (1-5) to determine macroregion, language-independent
				int macroregionId = parseInt(node0.getId());
				switch (macroregionId) {
				case 1:
					matches = NORD_OCCIDENTALE.contains(geonameId);
					break;
				case 2:
					matches = NORD_ORIENTALE.contains(geonameId);
					break;
				case 3:
					matches = CENTRALE.contains(geonameId);
					break;
				case 4:
					matches = MERIDIONALE.contains(geonameId);
					break;
				case 5:
					matches = INSULARE.contains(geonameId);
					break;
				default:
					matches = false;
					break;
				}
				
				if (matches) {
					node0.getZones().add(node1);
				}
			}
			changeIds(node0);
		}
		return new ResultNodes(OK, nodes, this);
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
			if (prefix.indexOf('-') > 0)
				prefix = prefix.substring(0, prefix.indexOf(ID_SEPARATOR));
			suffix = suffix.substring(suffix.indexOf('-'));
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
