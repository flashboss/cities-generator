package it.vige.cities;

import java.util.List;

/**
 * Normalizer for the names of the cities. According the configuration
 * they will be formatted
 * @author lucastancapiano
 */
public class Normalizer {

	/**
	 * default Normalizer
	 */
	public Normalizer() {
		
	}
	
	/**
	 * Execute
	 * @param caseSensitive case sensitive parameter
	 * @param duplicatedNames duplicated names parameter
	 * @param text text
	 * @param lines lines
	 * @return the command line
	 */
	public static String execute(boolean caseSensitive, boolean duplicatedNames, final String text,
			List<String> lines) {
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
