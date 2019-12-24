package it.vige.cities;

public class Configuration {

	private String country;

	private boolean caseSensitive;

	private boolean duplicatedNames;

	private String provider;

	private String username;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isDuplicatedNames() {
		return duplicatedNames;
	}

	public void setDuplicatedNames(boolean duplicatedNames) {
		this.duplicatedNames = duplicatedNames;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
