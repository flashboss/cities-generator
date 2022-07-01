package it.vige.cities.templates.it;

import static it.vige.cities.Normalizer.execute;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static java.util.stream.Collectors.toList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.vige.cities.Countries;
import it.vige.cities.HTMLTemplate;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * Comuni italiani provider
 * @author lucastancapiano
 */
public class ComuniItaliani extends HTMLTemplate {

	private final static String URL = "http://www.comuni-italiani.it/zona";

	private boolean caseSensitive;
	private boolean duplicatedNames;

	/**
	 * ComuniItaliani
	 * @param caseSensitive   the case sensitive parameter
	 * @param duplicatedNames the duplicated names parameter
	 */
	public ComuniItaliani(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = Countries.it.name();
	}

	/**
	 * Generate
	 */
	@Override
	public Nodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level0 = getPage(URL);
		Elements lines0 = level0.select(".tabwrap").get(0).select("tr td a");
		int counter = 1;
		for (Element head0 : lines0) {
			Node node0 = new Node();
			node0.setId("" + counter++);
			node0.setLevel(0);
			node0.setName(execute(caseSensitive, duplicatedNames, head0.text(),
					nodes.getZones().parallelStream().map(e -> e.getName()).collect(toList())));
			nodes.getZones().add(node0);
			Document level1 = getPage(head0.absUrl("href"));
			Elements lines1 = level1.select(".tabwrap").get(0).select("tr td a");
			for (Element head1 : lines1) {
				Node node1 = new Node();
				node1.setId(node0.getId() + ID_SEPARATOR + counter++);
				node1.setLevel(1);
				node1.setName(execute(caseSensitive, duplicatedNames, head1.text(), nodes.getZones().parallelStream()
						.flatMap(e -> e.getZones().parallelStream()).map(e -> e.getName()).collect(toList())));
				node0.getZones().add(node1);
				Document level2 = getPage(head1.absUrl("href"));
				Elements lines2 = level2.select(".tabwrap").get(0).select("tr td a");
				for (Element head2 : lines2) {
					Node node2 = new Node();
					node2.setId(node1.getId() + ID_SEPARATOR + counter++);
					node2.setLevel(2);
					node2.setName(execute(caseSensitive, duplicatedNames, head2.text(),
							nodes.getZones().parallelStream().flatMap(e -> e.getZones().parallelStream())
									.flatMap(e -> e.getZones().parallelStream()).map(e -> e.getName())
									.collect(toList())));
					node1.getZones().add(node2);
					Document level3 = getPage(head2.absUrl("href"));
					Elements lines3 = level3.select(".tabwrap").get(2).select("tr td a");
					for (Element head3 : lines3) {
						Node node3 = new Node();
						node3.setId(node2.getId() + ID_SEPARATOR + counter++);
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
		}
		return nodes;
	}

}
