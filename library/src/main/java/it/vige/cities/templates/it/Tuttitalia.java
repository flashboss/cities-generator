package it.vige.cities.templates.it;

import static it.vige.cities.Countries.it;
import static it.vige.cities.Normalizer.execute;
import static it.vige.cities.Result.KO;
import static it.vige.cities.Result.OK;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.vige.cities.HTMLTemplate;
import it.vige.cities.Result;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * 
 * @author lucastancapiano
 *
 *         Tuttitalia provider
 */
public class Tuttitalia extends HTMLTemplate {

	private final static String URL = "https://www.tuttitalia.it";

	private boolean caseSensitive;
	private boolean duplicatedNames;

	private Map<Node, List<String>> associations = new HashMap<Node, List<String>>();

	/**
	 * 
	 * @param caseSensitive   the case sensitive parameter
	 * @param duplicatedNames the duplicated names parameter
	 */
	public Tuttitalia(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = it.name();
	}

	/**
	 * 
	 */
	@Override
	public Nodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level1 = getPage(URL);
		Elements lines1 = level1.select("dl dt a");
		int counter = addLevel0(nodes, caseSensitive, associations);
		for (Node node0 : nodes.getZones()) {
			for (Element head1 : lines1) {
				String name = head1.text();
				if (associations.get(node0).contains(name)) {
					Node node1 = new Node();
					node1.setId(counter++);
					node1.setLevel(1);
					node1.setName(execute(caseSensitive, duplicatedNames, name,
							lines1.parallelStream().map(e -> e.text()).collect(toList())));
					node0.getZones().add(node1);
					Document level2 = getPage(head1.absUrl("href"));
					Elements lines2 = level2.select(".ut tr td a");
					for (Element head2 : lines2) {
						Node node2 = new Node();
						node2.setId(counter++);
						node2.setLevel(2);
						String text = head2.text();
						String[] splittedName = text.split("Provincia di ");
						if (splittedName.length < 2)
							splittedName = text.split("Provincia del");
						if (splittedName.length < 2)
							splittedName = text.split("Città Metropolitana di ");
						node2.setName(execute(caseSensitive, duplicatedNames, splittedName[1],
								lines2.parallelStream().map(e -> e.text()).collect(toList())));
						node1.getZones().add(node2);
						Document level3 = getPage(head2.absUrl("href"));
						Elements lines3 = level3.select(".at tr td a");
						for (Element head3 : lines3) {
							Node node3 = new Node();
							node3.setId(counter++);
							node3.setLevel(3);
							node3.setName(execute(caseSensitive, duplicatedNames, head3.text(),
									lines3.parallelStream().map(e -> e.text()).collect(toList())));
							node2.getZones().add(node3);
						}
					}
				}
			}
		}
		return nodes;
	}

	/**
	 * 
	 */
	@Override
	public Result generateFile() {
		try {
			writeFile(generate());
		} catch (Exception ex) {
			return KO;
		}
		return OK;
	}

	/**
	 * 
	 * @param nodes         the nodes
	 * @param caseSensitive true if it is case sensitive
	 * @param associations  the associations
	 * @return the number of level 0 nodes
	 */
	private int addLevel0(Nodes nodes, boolean caseSensitive, Map<Node, List<String>> associations) {
		int counter = 1;
		Node northWest = new Node();
		northWest.setId(counter++);
		northWest.setLevel(0);
		northWest.setName(execute(caseSensitive, true, "I: ITALIA NORD-OCCIDENTALE", null));
		nodes.getZones().add(northWest);
		associations.put(northWest, asList(new String[] { "Piemonte", "Liguria", "Valle d'Aosta", "Lombardia" }));
		Node northEast = new Node();
		northEast.setId(counter++);
		northEast.setLevel(0);
		northEast.setName(execute(caseSensitive, true, "II: ITALIA NORD-ORIENTALE", null));
		nodes.getZones().add(northEast);
		associations.put(northEast,
				asList(new String[] { "Emilia Romagna", "Veneto", "Trentino Alto Adige", "Friuli Venezia Giulia" }));
		Node south = new Node();
		south.setId(counter++);
		south.setLevel(0);
		south.setName(execute(caseSensitive, true, "III: ITALIA CENTRALE", null));
		nodes.getZones().add(south);
		associations.put(south, asList(new String[] { "Lazio", "Marche", "Umbria", "Toscana" }));
		Node centre = new Node();
		centre.setId(counter++);
		centre.setLevel(0);
		centre.setName(execute(caseSensitive, true, "IV: ITALIA MERIDIONALE", null));
		nodes.getZones().add(centre);
		associations.put(centre,
				asList(new String[] { "Abruzzo", "Campania", "Basilicata", "Molise", "Calabria", "Puglia" }));
		Node islands = new Node();
		islands.setId(counter++);
		islands.setLevel(0);
		islands.setName(execute(caseSensitive, true, "V: ITALIA INSULARE", null));
		nodes.getZones().add(islands);
		associations.put(islands, asList(new String[] { "Sardegna", "Sicilia" }));
		return counter;
	}

}
