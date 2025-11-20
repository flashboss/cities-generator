package it.vige.cities;

/**
 * The languages actually supported
 * @author lucastancapiano
 */
public enum Languages {

	/**
	 * IT - Italian (default)
	 */
	IT,

	/**
	 * EN - English
	 */
	EN,

	/**
	 * FR - French
	 */
	FR,

	/**
	 * DE - German
	 */
	DE,

	/**
	 * ES - Spanish
	 */
	ES,

	/**
	 * PT - Portuguese
	 */
	PT;

	/**
	 * Get the language code in lowercase (e.g., "it", "en")
	 * @return the language code
	 */
	public String getCode() {
		return name().toLowerCase();
	}

	/**
	 * Get the default language
	 * @return IT (Italian) as default
	 */
	public static Languages getDefault() {
		return IT;
	}

	/**
	 * Find a language by code (case-insensitive)
	 * @param code the language code (e.g., "it", "en", "IT", "EN")
	 * @return the language or null if not found
	 */
	public static Languages fromCode(String code) {
		if (code == null || code.isEmpty()) {
			return getDefault();
		}
		try {
			return valueOf(code.toUpperCase());
		} catch (IllegalArgumentException e) {
			return getDefault();
		}
	}
}

