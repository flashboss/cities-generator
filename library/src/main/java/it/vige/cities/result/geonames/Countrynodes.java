package it.vige.cities.result.geonames;

import java.util.List;

import java.util.Map;

/**
 * A set of geonames nodes
 * @author lucastancapiano
 */
public class Countrynodes {

	private List<Country> geonames;
	private Map<String, String> status;

	/**
	 * Geonames
	 * @return the list of countries
	 */
	public List<Country> getGeonames() {
		return geonames;
	}

	/**
	 * Geonames
	 * @param geonames the list of countries
	 */
	public void setGeonames(List<Country> geonames) {
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
