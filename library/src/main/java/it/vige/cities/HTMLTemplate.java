package it.vige.cities;

import static org.jsoup.Jsoup.connect;

import org.jsoup.nodes.Document;

/**
 * The HTML template for the format of the names
 * @author lucastancapiano
 */
public abstract class HTMLTemplate extends Template {

	/**
	 * Default constructor for HTMLTemplate
	 * Creates a new HTML template instance
	 */
	public HTMLTemplate() {
		
	}
	
	/**
	 * Fetch and parse an HTML page from a URL
	 * Uses JSoup to connect to the URL and parse the HTML document
	 * 
	 * @param url the URL of the page to fetch
	 * @return the parsed HTML document
	 * @throws Exception if there is a problem connecting to or parsing the URL
	 */
	protected Document getPage(String url) throws Exception {
		Document doc = connect(url).get();
		return doc;
	}

}
