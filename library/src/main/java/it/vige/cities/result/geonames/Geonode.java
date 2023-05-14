package it.vige.cities.result.geonames;

import java.util.List;
import java.util.Map;

/**
 * The node according the scheme of geonames
 * @author lucastancapiano
 */
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

	/**
	 * Admin code 1
	 * @return the admin code 1
	 */
	public String getAdminCode1() {
		return adminCode1;
	}

	/**
	 * Admin code 1
	 * @param adminCode1 the admin code 1
	 */
	public void setAdminCode1(String adminCode1) {
		this.adminCode1 = adminCode1;
	}

	/**
	 * Lng
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * Lng
	 * @param lng the lng
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * Geoname id
	 * @return the geoname id
	 */
	public int getGeonameId() {
		return geonameId;
	}

	/**
	 * Geoname id
	 * @param geonameId the geoname id
	 */
	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	/**
	 * Toponomy name
	 * @return the toponomy name
	 */
	public String getToponymName() {
		return toponymName;
	}

	/**
	 * Toponomy name
	 * @param toponymName the toponomy name
	 */
	public void setToponymName(String toponymName) {
		this.toponymName = toponymName;
	}

	/**
	 * Country id
	 * @return the country id
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * Country id
	 * @param countryId the country id
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * Fcl
	 * @return the fcl
	 */
	public String getFcl() {
		return fcl;
	}

	/**
	 * Fcl
	 * @param fcl the fcl
	 */
	public void setFcl(String fcl) {
		this.fcl = fcl;
	}

	/**
	 * Population
	 * @return the population
	 */
	public int getPopulation() {
		return population;
	}

	/**
	 * Population
	 * @param population the population
	 */
	public void setPopulation(int population) {
		this.population = population;
	}

	/**
	 * Country code
	 * @return the country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Country code
	 * @param countryCode the country code
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * Names
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Names
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Fcl name
	 * @return the fcl name
	 */
	public String getFclName() {
		return fclName;
	}

	/**
	 * Fcl name
	 * @param fclName the fcl name
	 */
	public void setFclName(String fclName) {
		this.fclName = fclName;
	}

	/**
	 * Admin codes 1
	 * @return the admin codes 1
	 */
	public Map<String, String> getAdminCodes1() {
		return adminCodes1;
	}

	/**
	 * Admin codes 1
	 * @param adminCodes1 the admin codes 1
	 */
	public void setAdminCodes1(Map<String, String> adminCodes1) {
		this.adminCodes1 = adminCodes1;
	}

	/**
	 * Country name
	 * @return the country name
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Country name
	 * @param countryName the country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Fcode name
	 * @return the fcode name
	 */
	public String getFcodeName() {
		return fcodeName;
	}

	/**
	 * Fcode name
	 * @param fcodeName the fcode name
	 */
	public void setFcodeName(String fcodeName) {
		this.fcodeName = fcodeName;
	}

	/**
	 * Admin name 1
	 * @return the admin name 1
	 */
	public String getAdminName1() {
		return adminName1;
	}

	/**
	 * Admin name 1
	 * @param adminName1 the admin name 1
	 */
	public void setAdminName1(String adminName1) {
		this.adminName1 = adminName1;
	}

	/**
	 * Lat
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Lat
	 * @param lat the lat
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * Fcode
	 * @return the fcode
	 */
	public String getFcode() {
		return fcode;
	}

	/**
	 * Fcode
	 * @param fcode the fcode
	 */
	public void setFcode(String fcode) {
		this.fcode = fcode;
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
}
