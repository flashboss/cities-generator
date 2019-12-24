package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

public class Geonodes {

	private int totalResultsCount;
	private List<Geonode> geonames;
	private Map<String, String> status;
	
	public int getTotalResultsCount() {
		return totalResultsCount;
	}
	public void setTotalResultsCount(int totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}
	public List<Geonode> getGeonames() {
		return geonames;
	}
	public void setGeonames(List<Geonode> geonames) {
		this.geonames = geonames;
	}
	public Map<String, String> getStatus() {
		return status;
	}
	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
	
}
