package it.vige.cities;

import static org.jsoup.Jsoup.connect;

import org.jsoup.nodes.Document;

/**
 * The HTML template for the format of the names
 * @author lucastancapiano
 */
public abstract class HTMLTemplate extends Template {

	/**
	 * default HTMLTemplate
	 */
	public HTMLTemplate() {
		
	}
	
	/**
	 * Page
	 * @param url the url of the page
	 * @return the document
	 * @throws Exception if there is a problem
	 */
	protected Document getPage(String url) throws Exception {
		Document doc = connect(url).get();
		return doc;
	}

}
