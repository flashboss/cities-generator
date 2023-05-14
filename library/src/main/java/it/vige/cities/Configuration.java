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
		this.country = country;
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
}
