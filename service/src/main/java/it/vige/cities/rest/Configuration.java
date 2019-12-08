package it.vige.cities.rest;

import it.vige.cities.Countries;

public class Configuration {

	private Countries country;
	
	private boolean caseSensitive;
	
	private boolean duplicatedNames;
	
	private String provider;

	public Countries getCountry() {
		return country;
	}

	public void setCountry(Countries country) {
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
}
