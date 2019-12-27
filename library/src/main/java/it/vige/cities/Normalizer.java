package it.vige.cities;

import java.util.List;

public class Normalizer {

	public static String execute(boolean caseSensitive, boolean duplicatedNames, final String text, List<String> lines) {
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

}