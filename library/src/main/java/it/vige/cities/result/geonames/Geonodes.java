package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

/**
 *  A set of geonames nodes
 * @author lucastancapiano
 */
public class Geonodes {

	private int totalResultsCount;
	private List<Geonode> geonames;
	private Map<String, String> status;
	
	/**
	 * default geonodes
	 */
	public Geonodes() {
		
	}

	/**
	 * Total result count
	 * @return the total result count
	 */
	public int getTotalResultsCount() {
		return totalResultsCount;
	}

	/**
	 * Total result count
	 * @param totalResultsCount the total result count
	 */
	public void setTotalResultsCount(int totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}

	/**
	 * Geonames
	 * @return the geonames list
	 */
	public List<Geonode> getGeonames() {
		return geonames;
	}

	/**
	 * Geonames
	 * @param geonames the geonames list
	 */
	public void setGeonames(List<Geonode> geonames) {
		this.geonames = geonames;
	}

	/**
	 * Status
	 * @return the status
	 */
	public Map<String, String> getStatus() {
		return status;
	}

	/**
	 * Status
	 * @param status the status
	 */
	public void setStatus(Map<String, String> status) {
		this.status = status;
	}

}
