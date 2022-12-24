package it.vige.cities.result.geonames;

/**
 * The geoname country
 * @author lucastancapiano
 */
public class Country {

	private String countryCode;
	private String countryName;
	private String isoNumeric;
	private String isoAlpha3;
	private String fipsCode;
	private String continent;
	private String continentName;
	private String capital;
	private String areaInSqKm;
	private String population;
	private String currencyCode;
	private String languages;
	private int geonameId;
	private float west;
	private float north;
	private float east;
	private float south;
	private String postalCodeFormat;
	
	/**
	 * Default country
	 */
	public Country() {
		
	}

	/**
	 * Country code
	 * @return the country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Country code
	 * @param countryCode the country code
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * Country name
	 * @return the country name
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Country name
	 * @param countryName the country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Iso numeric
	 * @return the iso numeric
	 */
	public String getIsoNumeric() {
		return isoNumeric;
	}

	/**
	 * Iso numeric
	 * @param isoNumeric the iso numeric
	 */
	public void setIsoNumeric(String isoNumeric) {
		this.isoNumeric = isoNumeric;
	}

	/**
	 * Iso alpha3
	 * @return the iso alpha3
	 */
	public String getIsoAlpha3() {
		return isoAlpha3;
	}

	/**
	 * Iso alpha3
	 * @param isoAlpha3 the iso alpha3
	 */
	public void setIsoAlpha3(String isoAlpha3) {
		this.isoAlpha3 = isoAlpha3;
	}

	/**
	 * Fips code
	 * @return the fips code
	 */
	public String getFipsCode() {
		return fipsCode;
	}

	/**
	 * Fips code
	 * @param fipsCode the fips code
	 */
	public void setFipsCode(String fipsCode) {
		this.fipsCode = fipsCode;
	}

	/**
	 * Continent
	 * @return the continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * Continent
	 * @param continent the continent
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}

	/**
	 * Continent name
	 * @return the continent name
	 */
	public String getContinentName() {
		return continentName;
	}

	/**
	 * Continent name
	 * @param continentName the continent name
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * Capital
	 * @return the capital
	 */
	public String getCapital() {
		return capital;
	}

	/**
	 * Capital
	 * @param capital the capital
	 */
	public void setCapital(String capital) {
		this.capital = capital;
	}

	/**
	 * Area in sqKm
	 * @return the area in sqkm
	 */
	public String getAreaInSqKm() {
		return areaInSqKm;
	}

	/**
	 * Area in sqKm
	 * @param areaInSqKm the area in sqkm
	 */
	public void setAreaInSqKm(String areaInSqKm) {
		this.areaInSqKm = areaInSqKm;
	}

	/**
	 * Population
	 * @return the population
	 */
	public String getPopulation() {
		return population;
	}

	/**
	 * Population
	 * @param population the population
	 */
	public void setPopulation(String population) {
		this.population = population;
	}

	/**
	 * Currency code
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * Currency code
	 * @param currencyCode the currency code
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * Languages
	 * @return the languages
	 */
	public String getLanguages() {
		return languages;
	}

	/**
	 * Languages
	 * @param languages the languages
	 */
	public void setLanguages(String languages) {
		this.languages = languages;
	}

	/**
	 * Geoname id
	 * @return the geoname id
	 */
	public int getGeonameId() {
		return geonameId;
	}

	/**
	 * Geoname id
	 * @param geonameId the geoname id
	 */
	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	/**
	 * West coordinate
	 * @return the west coordinate
	 */
	public float getWest() {
		return west;
	}

	/**
	 * West
	 * @param west coordinate the west coordinate
	 */
	public void setWest(float west) {
		this.west = west;
	}

	/**
	 * North
	 * @return the north coordinate
	 */
	public float getNorth() {
		return north;
	}

	/**
	 * North
	 * @param north the north coordinate
	 */
	public void setNorth(float north) {
		this.north = north;
	}

	/**
	 * East
	 * @return the east coordinate
	 */
	public float getEast() {
		return east;
	}

	/**
	 * East
	 * @param east the east coordinate
	 */
	public void setEast(float east) {
		this.east = east;
	}

	/**
	 * South
	 * @return the south coordinate
	 */
	public float getSouth() {
		return south;
	}

	/**
	 * South
	 * @param south the south coordinate
	 */
	public void setSouth(float south) {
		this.south = south;
	}

	/**
	 * Postal code format
	 * @return the postal code format
	 */
	public String getPostalCodeFormat() {
		return postalCodeFormat;
	}

	/**
	 * Postal code format
	 * @param postalCodeFormat the postal code format
	 */
	public void setPostalCodeFormat(String postalCodeFormat) {
		this.postalCodeFormat = postalCodeFormat;
	}

}
