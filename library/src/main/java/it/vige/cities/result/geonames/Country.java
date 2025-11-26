package it.vige.cities.result.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The geoname country
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

	/**
	 * GeoNames unique identifier for the country
	 */
	private int geonameId;
	
	/**
	 * Default constructor for Country
	 * Creates an empty country object
	 */
	public Country() {
		
	}

	/**
	 * Get the GeoNames unique identifier
	 * 
	 * @return the geoname ID
	 */
	public int getGeonameId() {
		return geonameId;
	}

	/**
	 * Set the GeoNames unique identifier
	 * 
	 * @param geonameId the geoname ID to set
	 */
	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

}
