package it.vige.cities.templates.it;

import static it.vige.cities.Countries.it;
import static it.vige.cities.Normalizer.setName;
import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.select.Selector;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.vige.cities.HTMLTemplate;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * Wikipedia provider
 * 
 * @author lucastancapiano
 */
public class Wikipedia extends HTMLTemplate {

	private final static String URL = "https://it.wikipedia.org/wiki/Comuni_d%27Italia";

	private boolean caseSensitive;
	private boolean duplicatedNames;

	private Map<Node, List<String>> associations = new HashMap<Node, List<String>>();

	/**
	 * Wikipedia
	 * 
	 * @param caseSensitive   the case sensitive parameter
	 * @param duplicatedNames the duplicated names parameter
	 */
	public Wikipedia(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = it.name();
	}

	/**
	 * Generate
	 */
	@Override
	public ResultNodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level1 = getPage(URL);
		Elements lines1 = level1.select("div div ul li a[title^=Comuni del], div div ul li a[title^=Comuni della]");
		int counter = addLevel0(nodes, caseSensitive, associations);
		for (Node node0 : nodes.getZones()) {
			for (Element head1 : lines1) {
				String linkText = head1.text();
				String name = null;
				// Handle special case for "Valle d'Aosta"
				if (linkText.contains("Valle d'Aosta")) {
					name = "Valle d'Aosta";
				} else {
					String[] words = linkText.split("del |della |'");
					name = words[words.length - 1];
				}
				if (associations.get(node0).contains(name)) {
					Node node1 = new Node();
					node1.setId(node0.getId() + ID_SEPARATOR + counter++);
					node1.setLevel(1);
					setName(caseSensitive, duplicatedNames, name, nodes.getZones(), node1);
					node0.getZones().add(node1);
					Document level2 = getPage(head1.absUrl("href"));
					Elements lines2 = level2.select(
							"div > table.wikitable > tbody > tr > td:eq(1) > a[title^=Città metropolitana], div > table.wikitable > tbody > tr > td:eq(1) > a[title^=Provincia], div > table.wikitable > tbody > tr > td:eq(0) > a[title^=Città metropolitana], div > table.wikitable > tbody > tr > td:eq(0) > a[title^=Provincia]");
					List<Element> linksNoDuplicated = filterDuplicated(lines2);
					
					// If no provinces found (e.g., Valle d'Aosta), treat the region as having direct municipalities
					if (linksNoDuplicated.isEmpty() && (name.equals("Valle d'Aosta") || name.equals("Sicilia") || name.equals("Sardegna"))) {
						// For regions without provinces, get municipalities directly from the table
						Elements directComuni = level2.select("div > table.wikitable > tbody > tr > td:eq(0) > a");
						for (Element comune : directComuni) {
							String comuneText = comune.text();
							// Skip header links and special pages
							if (comuneText != null && !comuneText.isEmpty() && 
								!comuneText.equals("Comune") && 
								!comuneText.startsWith("Comuni") &&
								!comuneText.contains("Categoria")) {
								Node node2 = new Node();
								node2.setId(node1.getId() + ID_SEPARATOR + counter++);
								node2.setLevel(2);
								setName(caseSensitive, duplicatedNames, comuneText, nodes.getZones(), node2);
								node1.getZones().add(node2);
							}
						}
					} else {
						// Normal flow: provinces -> municipalities
						for (Element head2 : linksNoDuplicated) {
							Node node2 = new Node();
							node2.setId(node1.getId() + ID_SEPARATOR + counter++);
							node2.setLevel(2);
							String text = head2.text();
							setName(caseSensitive, duplicatedNames, text, nodes.getZones(), node2);
							node1.getZones().add(node2);
							String escapedName = Selector.escapeCssIdentifier(node2.getName());
							Elements lines3 = level2.select(
									"div > table.wikitable > tbody > tr:has(td:eq(1) a:matchesOwn((?i)" + escapedName
											+ ")) > td:eq(0) > a");
							for (Element head3 : lines3) {
								Node node3 = new Node();
								node3.setId(node2.getId() + ID_SEPARATOR + counter++);
								node3.setLevel(3);
								setName(caseSensitive, duplicatedNames, head3.text(), nodes.getZones(), node3);
								node2.getZones().add(node3);
							}
						}
					}
				}
			}
		}
		return new ResultNodes(OK, nodes, this);
	}

	/**
	 * Level0
	 * 
	 * @param nodes         the nodes
	 * @param caseSensitive true if it is case sensitive
	 * @param associations  the associations
	 * @return the number of level 0 nodes
	 */
	private int addLevel0(Nodes nodes, boolean caseSensitive, Map<Node, List<String>> associations) {
		int counter = 1;
		Node northWest = new Node();
		northWest.setId("" + counter++);
		northWest.setLevel(0);
		setName(caseSensitive, true, "I: ITALIA NORD-OCCIDENTALE", null, northWest);
		nodes.getZones().add(northWest);
		associations.put(northWest, asList(new String[] { "Piemonte", "Liguria", "Valle d'Aosta", "Lombardia" }));
		Node northEast = new Node();
		northEast.setId("" + counter++);
		northEast.setLevel(0);
		setName(caseSensitive, true, "II: ITALIA NORD-ORIENTALE", null, northEast);
		nodes.getZones().add(northEast);
		associations.put(northEast,
				asList(new String[] { "Emilia Romagna", "Veneto", "Trentino Alto Adige", "Friuli Venezia Giulia" }));
		Node centre = new Node();
		centre.setId("" + counter++);
		centre.setLevel(0);
		setName(caseSensitive, true, "III: ITALIA CENTRALE", null, centre);
		nodes.getZones().add(centre);
		associations.put(centre, asList(new String[] { "Lazio", "Marche", "Umbria", "Toscana" }));
		Node south = new Node();
		south.setId("" + counter++);
		south.setLevel(0);
		setName(caseSensitive, true, "IV: ITALIA MERIDIONALE", null, south);
		nodes.getZones().add(south);
		associations.put(south,
				asList(new String[] { "Abruzzo", "Campania", "Basilicata", "Molise", "Calabria", "Puglia" }));
		Node islands = new Node();
		islands.setId("" + counter++);
		islands.setLevel(0);
		setName(caseSensitive, true, "V: ITALIA INSULARE", null, islands);
		nodes.getZones().add(islands);
		associations.put(islands, asList(new String[] { "Sardegna", "Sicilia" }));
		return counter;
	}

	private List<Element> filterDuplicated(List<Element> list) {
		return list.stream().filter(distinctByKey(Element::text)).collect(toList());
	}

	private <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
		Set<Object> seen = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());
		return t -> seen.add(keyExtractor.apply(t));
	}
}
