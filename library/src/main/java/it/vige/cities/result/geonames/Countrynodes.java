package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

public class Countrynodes {

	private List<Country> geonames;
	private Map<String, String> status;

	public List<Country> getGeonames() {
		return geonames;
	}

	public void setGeonames(List<Country> geonames) {
		this.geonames = geonames;
	}

	public Map<String, String> getStatus() {
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
}
