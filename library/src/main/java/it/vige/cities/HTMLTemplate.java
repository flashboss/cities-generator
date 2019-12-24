package it.vige.cities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class HTMLTemplate extends Template {

	protected Document getPage(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

}
