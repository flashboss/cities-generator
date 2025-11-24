package it.vige.cities;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

/**
 * All the configuration parameters are here
 * @author lucastancapiano
 */
public class Configuration {

	/**
	 * Logger for configuration operations
	 */
	private static final Logger logger = getLogger(Configuration.class);

	/**
	 * Country code (ISO 3166-1 alpha-2)
	 */
	private String country;

	/**
	 * Case sensitive flag for city name matching
	 */
	private boolean caseSensitive;

	/**
	 * Duplicated names flag - allows duplicate city names
	 */
	private boolean duplicatedNames;

	/**
	 * Provider name for data generation
	 */
	private String provider;

	/**
	 * Username for GeoNames API
	 */
	private String username;
	
	/**
	 * Language for location names (default: IT - Italian)
	 */
	private Languages language = Languages.getDefault();
	
	/**
	 * Default configuration
	 */
	public Configuration() {
		logger.debug("Creating default configuration");
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
		logger.debug("Setting country: {}", country);
		this.country = country != null ? country.toUpperCase() : null;
		logger.info("Country set to: {}", this.country);
	}

	/**
	 * Case sensitive
	 * @return the case sensitive configuration
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Case sensitive
	 * @param caseSensitive true if it is case sensitive
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		logger.debug("Setting case sensitive: {}", caseSensitive);
		this.caseSensitive = caseSensitive;
		logger.info("Case sensitive set to: {}", caseSensitive);
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
		logger.debug("Setting duplicated names: {}", duplicatedNames);
		this.duplicatedNames = duplicatedNames;
		logger.info("Duplicated names set to: {}", duplicatedNames);
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
		logger.debug("Setting provider: {}", provider);
		this.provider = provider;
		logger.info("Provider set to: {}", provider);
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
		logger.debug("Setting username: {}", username != null ? "***" : null);
		this.username = username;
		logger.info("Username set: {}", username != null ? "configured" : "not set");
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
		logger.debug("Setting language: {}", language != null ? language.getCode() : Languages.getDefault().getCode());
		this.language = language != null ? language : Languages.getDefault();
		logger.info("Language set to: {}", this.language.getCode());
	}

	/**
	 * Language (convenience method accepting String)
	 * @param language the language code (e.g., "it", "en"). Defaults to "it" if null or empty.
	 */
	public void setLanguage(String language) {
		logger.debug("Setting language from string: {}", language);
		this.language = Languages.fromCode(language);
		logger.info("Language set from string to: {}", this.language.getCode());
	}

	/**
	 * Get language code as String (lowercase)
	 * @return the language code (e.g., "it", "en")
	 */
	public String getLanguageCode() {
		return language.getCode();
	}
}
