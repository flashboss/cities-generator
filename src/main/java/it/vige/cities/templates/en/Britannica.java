package it.vige.cities.templates.en;

import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

public class Britannica extends Template {

	private Logger logger = LoggerFactory.getLogger(Britannica.class);

	private final static String URL = "https://www.britannica.com/topic/list-of-cities-and-towns-in-the-United-Kingdom-2034188";

	private boolean caseSensitive;
	private boolean duplicatedNames;

	public Britannica(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
	}

	@Override
	public Nodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level0 = getPage(URL);
		Elements lines0 = level0.select(".grid-sm section[data-level=1]:first-child a");
		lines0.remove(0);
		lines0.remove(lines0.last());
		lines0.remove(lines0.last());
		int counter = 0;
		for (Element head0 : lines0) {
			Node node0 = new Node();
			node0.setId(counter++);
			node0.setLevel(0);
			node0.setName(normalize(caseSensitive, duplicatedNames, head0.text(),
					lines0.parallelStream().map(e -> e.text()).collect(Collectors.toList())));
			nodes.getZones().add(node0);
			Elements lines1 = level0.select(".grid-sm section[data-level=2]").get(counter).select("h2 a");
			for (Element head1 : lines1) {
				Node node1 = new Node();
				node1.setId(counter++);
				node1.setLevel(1);
				node1.setName(normalize(caseSensitive, duplicatedNames, head1.text(),
						lines1.parallelStream().map(e -> e.text()).collect(Collectors.toList())));
				node0.getZones().add(node1);
				Document level2 = getPage(head1.absUrl("href"));
				Elements lines2 = level2.select(".tabwrap").get(0).select("tr td a");
				for (Element head2 : lines2) {
					Node node2 = new Node();
					node2.setId(counter++);
					node2.setLevel(2);
					node2.setName(normalize(caseSensitive, duplicatedNames, head2.text(),
							lines2.parallelStream().map(e -> e.text()).collect(Collectors.toList())));
					node1.getZones().add(node2);
					Document level3 = getPage(head2.absUrl("href"));
					Elements lines3 = level3.select(".tabwrap").get(2).select("tr td a");
					for (Element head3 : lines3) {
						Node node3 = new Node();
						node3.setId(counter++);
						node3.setLevel(3);
						node3.setName(normalize(caseSensitive, duplicatedNames, head3.text(),
								lines3.parallelStream().map(e -> e.text()).collect(Collectors.toList())));
						node2.getZones().add(node3);
					}
				}
			}
		}
		logger.info(nodes + "");
		return nodes;
	}
}
