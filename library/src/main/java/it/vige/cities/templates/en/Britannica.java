package it.vige.cities.templates.en;

import static it.vige.cities.Countries.uk;
import static it.vige.cities.Normalizer.execute;
import static java.util.stream.Collectors.toList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.vige.cities.HTMLTemplate;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * 
 * @author lucastancapiano
 *
 *         Britannica provider
 */
public class Britannica extends HTMLTemplate {

	private final static String URL = "https://www.britannica.com/topic/list-of-cities-and-towns-in-the-United-Kingdom-2034188";

	private boolean caseSensitive;
	private boolean duplicatedNames;

	/**
	 * 
	 * @param caseSensitive   the case sensitive parameter
	 * @param duplicatedNames the duplicated names parameter
	 */
	public Britannica(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = uk.name();
	}

	/**
	 * 
	 */
	@Override
	public Nodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level0 = getPage(URL);
		Elements lines0 = level0.select(".grid-sm section[data-level=1]:first-child a");
		lines0.remove(0);
		lines0.remove(lines0.last());
		lines0.remove(lines0.last());
		int counter = 0;
		int counterLevel0 = 0;
		int counterLevel1 = 0;
		for (Element head0 : lines0) {
			counterLevel0++;
			Node node0 = new Node();
			node0.setId(counter++);
			node0.setLevel(0);
			node0.setName(execute(caseSensitive, duplicatedNames, head0.text(),
					nodes.getZones().parallelStream().map(e -> e.getName()).collect(toList())));
			nodes.getZones().add(node0);
			Elements lines1 = level0.select(".grid-sm section[data-level=1]:eq(" + counterLevel0 + ")")
					.select("section[data-level=2] h2");
			for (Element head1 : lines1) {
				counterLevel1++;
				Node node1 = new Node();
				node1.setId(counter++);
				node1.setLevel(1);
				node1.setName(execute(caseSensitive, duplicatedNames, head1.text(), nodes.getZones().parallelStream()
						.flatMap(e -> e.getZones().parallelStream()).map(e -> e.getName()).collect(toList())));
				node0.getZones().add(node1);
				Elements lines2 = level0.select(".grid-sm section[data-level=1]:eq(" + counterLevel0 + ")")
						.select("section[data-level=2]:eq(" + counterLevel1 + ") ul");
				if (lines2.size() > 0)
					lines2 = lines2.get(0).children();
				for (Element head2 : lines2) {
					Node node2 = new Node();
					node2.setId(counter++);
					node2.setLevel(2);
					node2.setName(execute(caseSensitive, duplicatedNames, head2.select("a").text(),
							nodes.getZones().parallelStream().flatMap(e -> e.getZones().parallelStream())
									.flatMap(e -> e.getZones().parallelStream()).map(e -> e.getName())
									.collect(toList())));
					node1.getZones().add(node2);
					Elements lines3 = head2.select("li li");
					for (Element head3 : lines3) {
						Node node3 = new Node();
						node3.setId(counter++);
						node3.setLevel(3);
						node3.setName(execute(caseSensitive, duplicatedNames, head3.text(),
								nodes.getZones().parallelStream().flatMap(e -> e.getZones().parallelStream())
										.flatMap(e -> e.getZones().parallelStream())
										.flatMap(e -> e.getZones().parallelStream()).map(e -> e.getName())
										.collect(toList())));
						node2.getZones().add(node3);
					}
				}
			}
			counterLevel1 = 0;
		}
		return nodes;
	}
}
