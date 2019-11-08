package it.vige.cities.templates;

import org.apache.commons.cli.CommandLine;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.Generator;
import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

public class Tuttitalia extends Template {

	private Logger logger = LoggerFactory.getLogger(Tuttitalia.class);

	private final static String URL = "https://www.tuttitalia.it/";

	@Override
	public void generate(CommandLine cmd) throws Exception {
		boolean caseSensitive = cmd.hasOption(Generator.SINGLE_CASE_SENSITIVE);

		Document level0 = getPage(URL);
		Elements lines0 = level0.select("dl dt a");
		Nodes nodes = new Nodes();
		int counter = 0;
		for (Element head0 : lines0) {
			Node node0 = new Node();
			node0.setId(counter++);
			node0.setLevel(0);
			caseSensitive(caseSensitive, head0.text());
			nodes.getZones().add(node0);
			Document level1 = getPage(head0.absUrl("href"));
			Elements lines1 = level1.select(".ut tr td a");
			for (Element head1 : lines1) {
				Node node1 = new Node();
				node1.setId(counter++);
				node1.setLevel(1);
				String name = caseSensitive(caseSensitive, head1.text());
				String[] splittedName = name.split("Provincia di ");
				if (splittedName.length < 2)
					splittedName = name.split("Provincia del");
				if (splittedName.length < 2)
					splittedName = name.split("Città Metropolitana di ");
				node1.setName(splittedName[1]);
				node0.getZones().add(node1);
				Document level2 = getPage(head1.absUrl("href"));
				Elements lines2 = level2.select(".at tr td a");
				for (Element head2 : lines2) {
					Node node2 = new Node();
					node2.setId(counter++);
					node2.setLevel(2);
					node2.setName(caseSensitive(caseSensitive, head2.text()));
					node1.getZones().add(node2);
				}
			}
		}
		logger.info(nodes + "");
		writeFile(nodes);
	}

}
