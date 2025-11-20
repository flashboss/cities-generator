package it.vige.cities.result.geonames;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The node according the scheme of geonames
 * Only fields actually used in the code are kept.
 * Unknown fields from GeoNames API are ignored via @JsonIgnoreProperties.
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geonode {

	private int geonameId;
	private String name;
	private List<Geonode> geonames;
	
	/**
	 * default geonode
	 */
	public Geonode() {
		
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
	 * Name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
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
