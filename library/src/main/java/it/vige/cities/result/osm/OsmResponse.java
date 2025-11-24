package it.vige.cities.result.osm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Overpass API response
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmResponse {

	/**
	 * List of OSM elements returned by the Overpass API
	 */
	private List<OsmElement> elements;
	
	/**
	 * Default constructor for OsmResponse
	 * Creates an empty OSM response
	 */
	public OsmResponse() {
	}

	/**
	 * Get the list of OSM elements
	 * 
	 * @return the list of OSM elements
	 */
	public List<OsmElement> getElements() {
		return elements;
	}

	/**
	 * Set the list of OSM elements
	 * 
	 * @param elements the list of OSM elements to set
	 */
	public void setElements(List<OsmElement> elements) {
		this.elements = elements;
	}
}

