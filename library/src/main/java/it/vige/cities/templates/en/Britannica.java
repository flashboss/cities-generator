package it.vige.cities.templates.en;

import static it.vige.cities.Countries.GB;
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
 * Britannica provider
 * 
 * @author lucastancapiano
 */
public class Britannica extends HTMLTemplate {

	/**
	 * Britannica URL for UK cities and towns list
	 */
	private final static String URL = "https://www.britannica.com/topic/list-of-cities-and-towns-in-the-United-Kingdom-2034188";

	/**
	 * Case sensitive flag for city name matching
	 */
	private boolean caseSensitive;
	
	/**
	 * Duplicated names flag - allows duplicate city names
	 */
	private boolean duplicatedNames;

	/**
	 * Constructor for Britannica template
	 * 
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 */
	public Britannica(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = GB.name();
	}

	/**
	 * Check if the template supports the given language
	 * Britannica template only supports English (EN)
	 */
	@Override
	public boolean isLanguageSupported(Languages language) {
		return language == Languages.EN;
	}

	/**
	 * Check if the template supports the given country
	 * Britannica template only supports Great Britain (GB)
	 */
	@Override
	public boolean isCountrySupported(String country) {
		return Countries.GB.name().equalsIgnoreCase(country);
	}

	/**
	 * Generate cities data from Britannica
	 * Scrapes the Britannica website to extract UK cities and towns organized by regions
	 * 
	 * @return ResultNodes with OK result and generated nodes
	 * @throws Exception if there is a problem fetching or parsing the Britannica page
	 */
	@Override
	public ResultNodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Document level0 = getPage(URL);
		Elements lines0 = level0.select("section[data-level=\"1\"] h2.h1 a");
		int counter = 1;
		int counterLevel0 = 0;
		for (Element head0 : lines0) {
			counterLevel0++;
			Node node0 = new Node();
			node0.setId("" + counter++);
			node0.setLevel(0);
			setName(caseSensitive, duplicatedNames, head0.text(), nodes.getZones(), node0);
			nodes.getZones().add(node0);
			Elements lines1 = level0.select("div > section[data-level=1]").get(counterLevel0)
					.select("section > section");
			for (Element head1 : lines1) {
				Element head1title = head1.select("h2 > a").first();
				if (head1title == null)
					head1title = head1.select("h2").first();
				Node node1 = new Node();
				node1.setId(node0.getId() + ID_SEPARATOR + counter++);
				node1.setLevel(1);
				setName(caseSensitive, duplicatedNames, head1title.text(), nodes.getZones(), node1);
				node0.getZones().add(node1);
				Elements lines2 = head1.select("section > ul > li");
				for (Element head2 : lines2) {
					Element head2title = head2.select("div").first();
					Node node2 = new Node();
					node2.setId(node1.getId() + ID_SEPARATOR + counter++);
					node2.setLevel(2);
					setName(caseSensitive, duplicatedNames, head2title.text(), nodes.getZones(), node2);
					node1.getZones().add(node2);
					Elements lines3 = head2.select("div > ul > li a");
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
