package it.vige.cities.templates.it;

import static it.vige.cities.Normalizer.setName;
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

	// GeoNames IDs for Italian regions (language-independent)
	private static final Set<Integer> NORD_OCCIDENTALE = Set.of(
		3170831, // Piemonte
		3174725, // Liguria
		3164857, // Valle d'Aosta
		3174618  // Lombardia
	);
	
	private static final Set<Integer> NORD_ORIENTALE = Set.of(
		3177401, // Emilia-Romagna
		3164604, // Veneto
		3165244, // Trentino-Alto Adige
		3176525  // Friuli Venezia Giulia
	);
	
	private static final Set<Integer> CENTRALE = Set.of(
		3174976, // Lazio
		3174004, // Marche
		3165048, // Umbria
		3165361  // Toscana
	);
	
	private static final Set<Integer> MERIDIONALE = Set.of(
		3183560, // Abruzzo
		3181042, // Campania
		3182306, // Basilicata
		3173222, // Molise
		2525468, // Calabria
		3169778  // Puglia
	);
	
	private static final Set<Integer> INSULARE = Set.of(
		2523228, // Sardegna
		2523119  // Sicilia
	);

	/**
	 * ExtraGeoNames
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames true if it accepts duplicated names
	 * @param username        the username
	 */
	public ExtraGeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username) {
		this(country, caseSensitive, duplicatedNames, username, Languages.getDefault());
	}

	/**
	 * ExtraGeoNames
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames true if it accepts duplicated names
	 * @param username        the username
	 * @param language        the language enum
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
	 * Generate
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
	 * Get translated name for a macroregion
	 * @param macroregion the macroregion identifier (1-5)
	 * @return the translated name
	 */
	private String getMacroregionName(int macroregion) {
		Languages lang = language != null ? language : Languages.getDefault();
		switch (lang) {
		case IT:
			switch (macroregion) {
			case 1:
				return "I: ITALIA NORD-OCCIDENTALE";
			case 2:
				return "II: ITALIA NORD-ORIENTALE";
			case 3:
				return "III: ITALIA CENTRALE";
			case 4:
				return "IV: ITALIA MERIDIONALE";
			case 5:
				return "V: ITALIA INSULARE";
			default:
				return "";
			}
		case EN:
			switch (macroregion) {
			case 1:
				return "I: NORTH-WEST ITALY";
			case 2:
				return "II: NORTH-EAST ITALY";
			case 3:
				return "III: CENTRAL ITALY";
			case 4:
				return "IV: SOUTHERN ITALY";
			case 5:
				return "V: INSULAR ITALY";
			default:
				return "";
			}
		case FR:
			switch (macroregion) {
			case 1:
				return "I: ITALIE DU NORD-OUEST";
			case 2:
				return "II: ITALIE DU NORD-EST";
			case 3:
				return "III: ITALIE CENTRALE";
			case 4:
				return "IV: ITALIE MÉRIDIONALE";
			case 5:
				return "V: ITALIE INSULAIRE";
			default:
				return "";
			}
		case DE:
			switch (macroregion) {
			case 1:
				return "I: NORDWESTITALIEN";
			case 2:
				return "II: NORDOSTITALIEN";
			case 3:
				return "III: ZENTRALITALIEN";
			case 4:
				return "IV: SÜDITALIEN";
			case 5:
				return "V: INSELITALIEN";
			default:
				return "";
			}
		case ES:
			switch (macroregion) {
			case 1:
				return "I: ITALIA NOROCCIDENTAL";
			case 2:
				return "II: ITALIA NORORIENTAL";
			case 3:
				return "III: ITALIA CENTRAL";
			case 4:
				return "IV: ITALIA MERIDIONAL";
			case 5:
				return "V: ITALIA INSULAR";
			default:
				return "";
			}
		case PT:
			switch (macroregion) {
			case 1:
				return "I: ITÁLIA DO NOROESTE";
			case 2:
				return "II: ITÁLIA DO NORDESTE";
			case 3:
				return "III: ITÁLIA CENTRAL";
			case 4:
				return "IV: ITÁLIA MERIDIONAL";
			case 5:
				return "V: ITÁLIA INSULAR";
			default:
				return "";
			}
		default:
			// Fallback to Italian
			switch (macroregion) {
			case 1:
				return "I: ITALIA NORD-OCCIDENTALE";
			case 2:
				return "II: ITALIA NORD-ORIENTALE";
			case 3:
				return "III: ITALIA CENTRALE";
			case 4:
				return "IV: ITALIA MERIDIONALE";
			case 5:
				return "V: ITALIA INSULARE";
			default:
				return "";
			}
		}
	}

	/**
	 * 
	 * @param nodes         the nodes
	 * @param caseSensitive true if it is case sensitive
	 */
	private void addLevel0(Nodes nodes, boolean caseSensitive) {
		int counter = 1;
		Node northWest = new Node();
		northWest.setId("" + counter++);
		northWest.setLevel(0);
		setName(caseSensitive, true, getMacroregionName(1), null, northWest);
		nodes.getZones().add(northWest);
		Node northEast = new Node();
		northEast.setId("" + counter++);
		northEast.setLevel(0);
		setName(caseSensitive, true, getMacroregionName(2), null, northEast);
		nodes.getZones().add(northEast);
		Node centre = new Node();
		centre.setId("" + counter++);
		centre.setLevel(0);
		setName(caseSensitive, true, getMacroregionName(3), null, centre);
		nodes.getZones().add(centre);
		Node south = new Node();
		south.setId("" + counter++);
		south.setLevel(0);
		setName(caseSensitive, true, getMacroregionName(4), null, south);
		nodes.getZones().add(south);
		Node islands = new Node();
		islands.setId("" + counter++);
		islands.setLevel(0);
		setName(caseSensitive, true, getMacroregionName(5), null, islands);
		nodes.getZones().add(islands);
	}

}
