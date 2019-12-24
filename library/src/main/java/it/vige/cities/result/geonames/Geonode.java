package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

public class Geonode {

	private String adminCode1;
	private double lng;
	private int geonameId;
	private String toponymName;
	private int countryId;
	private String fcl;
	private int population;
	private String countryCode;
	private String name;
	private String fclName;
	private Map<String, String> adminCodes1;
	private String countryName;
	private String fcodeName;
	private String adminName1;
	private double lat;
	private String fcode;
	private List<Geonode> geonames;

	public String getAdminCode1() {
		return adminCode1;
	}

	public void setAdminCode1(String adminCode1) {
		this.adminCode1 = adminCode1;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getGeonameId() {
		return geonameId;
	}

	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	public String getToponymName() {
		return toponymName;
	}

	public void setToponymName(String toponymName) {
		this.toponymName = toponymName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getFcl() {
		return fcl;
	}

	public void setFcl(String fcl) {
		this.fcl = fcl;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFclName() {
		return fclName;
	}

	public void setFclName(String fclName) {
		this.fclName = fclName;
	}

	public Map<String, String> getAdminCodes1() {
		return adminCodes1;
	}

	public void setAdminCodes1(Map<String, String> adminCodes1) {
		this.adminCodes1 = adminCodes1;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getFcodeName() {
		return fcodeName;
	}

	public void setFcodeName(String fcodeName) {
		this.fcodeName = fcodeName;
	}

	public String getAdminName1() {
		return adminName1;
	}

	public void setAdminName1(String adminName1) {
		this.adminName1 = adminName1;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public List<Geonode> getGeonames() {
		return geonames;
	}

	public void setGeonames(List<Geonode> geonames) {
		this.geonames = geonames;
	}
}
