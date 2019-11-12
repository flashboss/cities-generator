package it.vige.cities;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.result.Nodes;

public abstract class Template {

	private ObjectMapper mapper = new ObjectMapper();

	protected abstract Result generate();

	protected Document getPage(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	protected void writeFile(Nodes nodes) throws Exception {
		mapper.writeValue(new File("build/output.json"), nodes);
	}

	protected String caseSensitive(boolean caseSensitive, String text) {
		if (caseSensitive)
			return text;
		else
			return text.toUpperCase();
	}
}
