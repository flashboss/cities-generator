package it.vige.cities;

/**
 * 
 * @author lucastancapiano
 *
 *         All the configuration parameters are here
 */
public class Configuration {

	private String country;

	private boolean caseSensitive;

	private boolean duplicatedNames;

	private String provider;

	private String username;

	/**
	 * 
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country the country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return the case sensitive configuration
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * 
	 * @param caseSensitive true if it is case sensitive
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * 
	 * @return the duplicated names
	 */
	public boolean isDuplicatedNames() {
		return duplicatedNames;
	}

	/**
	 * 
	 * @param duplicatedNames the duplicated names
	 */
	public void setDuplicatedNames(boolean duplicatedNames) {
		this.duplicatedNames = duplicatedNames;
	}

	/**
	 * 
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * 
	 * @param provider the name of the provider
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * 
	 * @return the user name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username the user name
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
