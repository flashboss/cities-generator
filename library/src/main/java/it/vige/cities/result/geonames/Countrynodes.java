package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A set of geonames nodes
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Countrynodes {

	/**
	 * List of country nodes from GeoNames API
	 */
	private List<Country> geonames;
	
	/**
	 * Status information from GeoNames API response
	 */
	private Map<String, String> status;
	
	/**
	 * Default constructor for Countrynodes
	 * Creates an empty country nodes container
	 */
	public Countrynodes() {
		
	}

	/**
	 * Get the list of country nodes
	 * 
	 * @return the list of countries
	 */
	public List<Country> getGeonames() {
		return geonames;
	}

	/**
	 * Set the list of country nodes
	 * 
	 * @param geonames the list of countries to set
	 */
	public void setGeonames(List<Country> geonames) {
		this.geonames = geonames;
	}

	/**
	 * Get the status information from GeoNames API
	 * 
	 * @return the status map
	 */
	public Map<String, String> getStatus() {
		return status;
	}

	/**
	 * Set the status information
	 * 
	 * @param status the status map to set
	 */
	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
}
