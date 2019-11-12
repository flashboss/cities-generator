package it.vige.cities.templates.it;

import org.apache.commons.cli.CommandLine;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.Generator;
import it.vige.cities.Result;
import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

public class ComuniItaliani extends Template {

	private Logger logger = LoggerFactory.getLogger(ComuniItaliani.class);

	private final static String URL = "http://www.comuni-italiani.it/";

	@Override
	public Result generate(CommandLine cmd) {
		try {
			boolean caseSensitive = cmd.hasOption(Generator.SINGLE_CASE_SENSITIVE);

			Document level0 = getPage(URL);
			System.out.println(level0);
			Elements lines0 = level0.select(".tabwrap").get(0).select("tr td a");
			Nodes nodes = new Nodes();
			int counter = 0;
			for (Element head0 : lines0) {
				Node node0 = new Node();
				node0.setId(counter++);
				node0.setLevel(0);
				node0.setName(caseSensitive(caseSensitive, head0.text()));
				nodes.getZones().add(node0);
				Document level1 = getPage(head0.absUrl("href"));
				Elements lines1 = level1.select(".tabwrap").get(0).select("tr td a");
				for (Element head1 : lines1) {
					Node node1 = new Node();
					node1.setId(counter++);
					node1.setLevel(1);
					node1.setName(caseSensitive(caseSensitive, head1.text()));
					node0.getZones().add(node1);
					Document level2 = getPage(head1.absUrl("href"));
					Elements lines2 = level2.select(".tabwrap").get(2).select("tr td a");
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
		} catch (Exception ex) {
			return Result.KO;
		}
		return Result.OK;
	}

}
