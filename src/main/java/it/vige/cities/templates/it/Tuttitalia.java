package it.vige.cities.templates.it;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.Result;
import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

public class Tuttitalia extends Template {

	private Logger logger = LoggerFactory.getLogger(Tuttitalia.class);

	private final static String URL = "https://www.tuttitalia.it";

	private boolean caseSensitive;

	public Tuttitalia(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public Result generate() {
		try {
			Nodes nodes = new Nodes();
			int counter = addLevel0(nodes);
			for (Node node0 : nodes.getZones()) {
				Document level1 = getPage(URL);
				Elements lines1 = level1.select("dl dt a");
				for (Element head1 : lines1) {
					Node node1 = new Node();
					node1.setId(counter++);
					node1.setLevel(1);
					node1.setName(caseSensitive(caseSensitive, head1.text()));
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
						node2.setName(caseSensitive(caseSensitive, splittedName[1]));
						node1.getZones().add(node2);
						Document level3 = getPage(head2.absUrl("href"));
						Elements lines3 = level3.select(".at tr td a");
						for (Element head3 : lines3) {
							Node node3 = new Node();
							node3.setId(counter++);
							node3.setLevel(3);
							node3.setName(caseSensitive(caseSensitive, head3.text()));
							node2.getZones().add(node3);
						}
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
	
	private int addLevel0(Nodes nodes) {
		int counter = 0;
		Node northWest = new Node();
		northWest.setId(counter++);
		northWest.setLevel(0);
		northWest.setName(caseSensitive(caseSensitive, "I: ITALIA NORD-OCCIDENTALE"));
		nodes.getZones().add(northWest);
		Node northEast = new Node();
		northEast.setId(counter++);
		northEast.setLevel(0);
		northEast.setName(caseSensitive(caseSensitive, "II: ITALIA NORD-ORIENTALE"));
		nodes.getZones().add(northEast);
		Node south = new Node();
		south.setId(counter++);
		south.setLevel(0);
		south.setName(caseSensitive(caseSensitive, "III: ITALIA CENTRALE"));
		nodes.getZones().add(south);
		Node centre = new Node();
		centre.setId(counter++);
		centre.setLevel(0);
		centre.setName(caseSensitive(caseSensitive, "IV: ITALIA MERIDIONALE"));
		nodes.getZones().add(centre);
		Node islands = new Node();
		islands.setId(counter++);
		islands.setLevel(0);
		islands.setName(caseSensitive(caseSensitive, "V: ITALIA INSULARE"));
		nodes.getZones().add(islands);
		return counter;
	}

}
