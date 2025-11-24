package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

/**
 *  A set of geonames nodes
 * @author lucastancapiano
 */
public class Geonodes {

	/**
	 * Total number of results from GeoNames API query
	 */
	private int totalResultsCount;
	
	/**
	 * List of geonodes from GeoNames API
	 */
	private List<Geonode> geonames;
	
	/**
	 * Status information from GeoNames API response
	 */
	private Map<String, String> status;
	
	/**
	 * Default constructor for Geonodes
	 * Creates an empty geonodes container
	 */
	public Geonodes() {
		
	}

	/**
	 * Get the total number of results
	 * 
	 * @return the total result count
	 */
	public int getTotalResultsCount() {
		return totalResultsCount;
	}

	/**
	 * Set the total number of results
	 * 
	 * @param totalResultsCount the total result count to set
	 */
	public void setTotalResultsCount(int totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}

	/**
	 * Get the list of geonodes
	 * 
	 * @return the geonames list
	 */
	public List<Geonode> getGeonames() {
		return geonames;
	}

	/**
	 * Set the list of geonodes
	 * 
	 * @param geonames the geonames list to set
	 */
	public void setGeonames(List<Geonode> geonames) {
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
