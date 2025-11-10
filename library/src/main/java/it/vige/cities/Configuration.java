package it.vige.cities;

/**
 * All the configuration parameters are here
 * @author lucastancapiano
 */
public class Configuration {

	private String country;

	private boolean caseSensitive;

	private boolean duplicatedNames;

	private String provider;

	private String username;
	
	private Languages language = Languages.getDefault();
	
	/**
	 * Default configuration
	 */
	public Configuration() {
		
	}

	/**
	 * Country
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Country
	 * @param country the country
	 */
	public void setCountry(String country) {
		this.country = country != null ? country.toUpperCase() : null;
	}

	/**
	 * Case sensistive
	 * @return the case sensitive configuration
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Case sensistive
	 * @param caseSensitive true if it is case sensitive
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * Duplicated names
	 * @return the duplicated names
	 */
	public boolean isDuplicatedNames() {
		return duplicatedNames;
	}

	/**
	 * Duplicated names
	 * @param duplicatedNames the duplicated names
	 */
	public void setDuplicatedNames(boolean duplicatedNames) {
		this.duplicatedNames = duplicatedNames;
	}

	/**
	 * Provider
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * Provider
	 * @param provider the name of the provider
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * Username
	 * @return the user name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Username
	 * @param username the user name
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Language
	 * @return the language enum
	 */
	public Languages getLanguage() {
		return language;
	}

	/**
	 * Language
	 * @param language the language enum
	 */
	public void setLanguage(Languages language) {
		this.language = language != null ? language : Languages.getDefault();
	}

	/**
	 * Language (convenience method accepting String)
	 * @param language the language code (e.g., "it", "en"). Defaults to "it" if null or empty.
	 */
	public void setLanguage(String language) {
		this.language = Languages.fromCode(language);
	}

	/**
	 * Get language code as String (lowercase)
	 * @return the language code (e.g., "it", "en")
	 */
	public String getLanguageCode() {
		return language.getCode();
	}
}
