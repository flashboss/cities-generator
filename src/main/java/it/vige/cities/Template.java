package it.vige.cities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class Template extends FileGenerator {

	protected abstract Result generate();

	protected Document getPage(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	protected String caseSensitive(boolean caseSensitive, String text) {
		if (caseSensitive)
			return text;
		else
			return text.toUpperCase();
	}
}
