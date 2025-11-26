package it.vige.cities.templates.it;

import static it.vige.cities.Normalizer.setName;
import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.vige.cities.Countries;
import it.vige.cities.HTMLTemplate;
import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * Comuni italiani provider
 * 
 * @author lucastancapiano
 */
public class ComuniItaliani extends HTMLTemplate {

	/**
	 * Comuni Italiani URL for Italian administrative divisions
	 */
	private final static String URL = "http://www.comuni-italiani.it/zona";

	/**
	 * Case sensitive flag for city name matching
	 */
	private boolean caseSensitive;
	
	/**
	 * Duplicated names flag - allows duplicate city names
	 */
	private boolean duplicatedNames;

	/**
	 * Constructor for ComuniItaliani template
	 * 
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 */
	public ComuniItaliani(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = Countries.IT.name();
	}

	/**
	 * Check if the template supports the given language
	 * ComuniItaliani template only supports Italian (IT)
	 */
	@Override
	public boolean isLanguageSupported(Languages language) {
		return language == Languages.IT;
	}

	/**
	 * Check if the template supports the given country
	 * ComuniItaliani template only supports Italy (IT)
	 */
	@Override
	public boolean isCountrySupported(String country) {
		return Countries.IT.name().equalsIgnoreCase(country);
	}

	/**
	 * Generate cities data from Comuni Italiani
	 * Scrapes the comuni-italiani.it website to extract Italian administrative divisions (regions, provinces, municipalities)
	 * 
	 * @return ResultNodes with OK result and generated nodes
	 * @throws Exception if there is a problem fetching or parsing the Comuni Italiani pages
	 */
	@Override
	public ResultNodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level0 = getPage(URL);
		Elements lines0 = level0.select(".tabwrap").get(0).select("tr td a");
		int counter = 1;
		for (Element head0 : lines0) {
			Node node0 = new Node();
			node0.setId("" + counter++);
			node0.setLevel(0);
			setName(caseSensitive, duplicatedNames, head0.text(), nodes.getZones(), node0);
			nodes.getZones().add(node0);
			Document level1 = getPage(head0.absUrl("href"));
			Elements lines1 = level1.select(".tabwrap").get(0).select("tr td a");
			for (Element head1 : lines1) {
				Node node1 = new Node();
				node1.setId(node0.getId() + ID_SEPARATOR + counter++);
				node1.setLevel(1);
				setName(caseSensitive, duplicatedNames, head1.text(), nodes.getZones(), node1);
				node0.getZones().add(node1);
				Document level2 = getPage(head1.absUrl("href"));
				Elements lines2 = level2.select(".tabwrap").get(0).select("tr td a");
				for (Element head2 : lines2) {
					Node node2 = new Node();
					node2.setId(node1.getId() + ID_SEPARATOR + counter++);
					node2.setLevel(2);
					setName(caseSensitive, duplicatedNames, head2.text(), nodes.getZones(), node2);
					node1.getZones().add(node2);
					Document level3 = getPage(head2.absUrl("href"));
					Elements lines3 = level3.select(".tabwrap").get(2).select("tr td a");
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
		return new ResultNodes(OK, nodes, this);
	}

}
