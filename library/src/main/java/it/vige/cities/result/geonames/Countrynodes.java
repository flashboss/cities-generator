package it.vige.cities.result.geonames;

import java.util.List;

import java.util.Map;

/**
 * 
 * @author lucastancapiano
 *
 *         A set of geonames nodes
 */
public class Countrynodes {

	private List<Country> geonames;
	private Map<String, String> status;

	/**
	 * 
	 * @return the list of countries
	 */
	public List<Country> getGeonames() {
		return geonames;
	}

	/**
	 * 
	 * @param geonames the list of countries
	 */
	public void setGeonames(List<Country> geonames) {
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
