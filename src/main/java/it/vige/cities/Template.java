package it.vige.cities;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import it.vige.cities.result.Nodes;

public abstract class Template extends FileGenerator {

	protected abstract Nodes generate() throws Exception;

	protected Document getPage(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	protected String normalize(boolean caseSensitive, boolean duplicatedNames, final String text, List<String> lines) {
		String newText = text;
		if (!duplicatedNames) {
			long count = lines.parallelStream().filter(e -> e.equalsIgnoreCase(text)).count();
			for (int i = 0; i < count; i++)
				newText = newText + " (" + (i + 2) + ")";
		}
		if (!caseSensitive)
			newText = newText.toUpperCase();
		return newText;
	}

	protected Result generateFile() {
		try {
			writeFile(generate());
		} catch (Exception ex) {
			return Result.KO;
		}
		return Result.OK;
	}
}
