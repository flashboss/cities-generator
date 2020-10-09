package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author lucastancapiano
 *
 *         A set of geonames nodes
 */
public class Geonodes {

	private int totalResultsCount;
	private List<Geonode> geonames;
	private Map<String, String> status;

	/**
	 * 
	 * @return the total result count
	 */
	public int getTotalResultsCount() {
		return totalResultsCount;
	}

	/**
	 * 
	 * @param totalResultsCount the total result count
	 */
	public void setTotalResultsCount(int totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}

	/**
	 * 
	 * @return the geonames list
	 */
	public List<Geonode> getGeonames() {
		return geonames;
	}

	/**
	 * 
	 * @param geonames the geonames list
	 */
	public void setGeonames(List<Geonode> geonames) {
		this.geonames = geonames;
	}

	/**
	 * 
	 * @return the status
	 */
	public Map<String, String> getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status the status
	 */
	public void setStatus(Map<String, String> status) {
		this.status = status;
	}

}
