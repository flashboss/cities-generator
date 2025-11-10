package it.vige.cities.result.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The geoname country
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

	private int geonameId;
	
	/**
	 * Default country
	 */
	public Country() {
		
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

}
