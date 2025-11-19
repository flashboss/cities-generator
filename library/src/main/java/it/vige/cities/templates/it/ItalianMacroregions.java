package it.vige.cities.templates.it;

import static it.vige.cities.Normalizer.setName;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.vige.cities.Languages;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * Utility class for Italian macroregions (level 0) used by EXTRA providers
 * 
 * @author lucastancapiano
 */
public class ItalianMacroregions {

	/**
	 * Private constructor to prevent instantiation
	 */
	private ItalianMacroregions() {
		// Utility class - no instantiation
	}

	/**
	 * Get translated name for a macroregion
	 * @param macroregion the macroregion identifier (1-5)
	 * @param language the language enum
	 * @return the translated name
	 */
	public static String getMacroregionName(int macroregion, Languages language) {
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
	 * Add level 0 nodes (macroregions) to the nodes list
	 * 
	 * @param nodes the nodes list
	 * @param caseSensitive true if it is case sensitive
	 * @param language the language for translations
	 */
	public static void addLevel0(Nodes nodes, boolean caseSensitive, Languages language) {
		int counter = 1;
		for (int i = 1; i <= 5; i++) {
			Node node = new Node();
			node.setId("" + counter++);
			node.setLevel(0);
			setName(caseSensitive, true, getMacroregionName(i, language), null, node);
			nodes.getZones().add(node);
		}
	}

	/**
	 * Add level 0 nodes (macroregions) to the nodes list with associations
	 * Used by Wikipedia provider
	 * 
	 * @param nodes the nodes list
	 * @param caseSensitive true if it is case sensitive
	 * @param associations the map to populate with region associations
	 * @return the number of level 0 nodes added
	 */
	public static int addLevel0(Nodes nodes, boolean caseSensitive, Map<Node, List<String>> associations) {
		int counter = 1;
		
		// Nord-Occidentale
		Node northWest = new Node();
		northWest.setId("" + counter++);
		northWest.setLevel(0);
		setName(caseSensitive, true, "I: ITALIA NORD-OCCIDENTALE", null, northWest);
		nodes.getZones().add(northWest);
		if (associations != null) {
			associations.put(northWest, Arrays.asList("Piemonte", "Liguria", "Valle d'Aosta", "Lombardia"));
		}
		
		// Nord-Orientale
		Node northEast = new Node();
		northEast.setId("" + counter++);
		northEast.setLevel(0);
		setName(caseSensitive, true, "II: ITALIA NORD-ORIENTALE", null, northEast);
		nodes.getZones().add(northEast);
		if (associations != null) {
			associations.put(northEast, Arrays.asList("Emilia Romagna", "Veneto", "Trentino Alto Adige", "Friuli Venezia Giulia"));
		}
		
		// Centrale
		Node centre = new Node();
		centre.setId("" + counter++);
		centre.setLevel(0);
		setName(caseSensitive, true, "III: ITALIA CENTRALE", null, centre);
		nodes.getZones().add(centre);
		if (associations != null) {
			associations.put(centre, Arrays.asList("Lazio", "Marche", "Umbria", "Toscana"));
		}
		
		// Meridionale
		Node south = new Node();
		south.setId("" + counter++);
		south.setLevel(0);
		setName(caseSensitive, true, "IV: ITALIA MERIDIONALE", null, south);
		nodes.getZones().add(south);
		if (associations != null) {
			associations.put(south, Arrays.asList("Abruzzo", "Campania", "Basilicata", "Molise", "Calabria", "Puglia"));
		}
		
		// Insulare
		Node islands = new Node();
		islands.setId("" + counter++);
		islands.setLevel(0);
		setName(caseSensitive, true, "V: ITALIA INSULARE", null, islands);
		nodes.getZones().add(islands);
		if (associations != null) {
			associations.put(islands, Arrays.asList("Sardegna", "Sicilia"));
		}
		
		return counter;
	}
}

