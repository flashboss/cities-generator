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

	/**
	 * GeoNames unique identifier
	 */
	private int geonameId;
	
	/**
	 * Name of the location
	 */
	private String name;
	
	/**
	 * List of child geonodes (hierarchical structure)
	 */
	private List<Geonode> geonames;
	
	/**
	 * Default constructor for Geonode
	 * Creates an empty geonode
	 */
	public Geonode() {
		
	}

	/**
	 * Get the GeoNames unique identifier
	 * 
	 * @return the geoname ID
	 */
	public int getGeonameId() {
		return geonameId;
	}

	/**
	 * Set the GeoNames unique identifier
	 * 
	 * @param geonameId the geoname ID to set
	 */
	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	/**
	 * Get the name of the location
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the location
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the list of child geonodes
	 * 
	 * @return the list of child geonodes
	 */
	public List<Geonode> getGeonames() {
		return geonames;
	}

	/**
	 * Set the list of child geonodes
	 * 
	 * @param geonames the list of child geonodes to set
	 */
	public void setGeonames(List<Geonode> geonames) {
		this.geonames = geonames;
	}
}
