package it.vige.cities.result.osm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Overpass API response
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmResponse {

	private List<OsmElement> elements;
	
	/**
	 * Default OSM response
	 */
	public OsmResponse() {
	}

	/**
	 * Elements
	 * @return the list of OSM elements
	 */
	public List<OsmElement> getElements() {
		return elements;
	}

	/**
	 * Elements
	 * @param elements the list of OSM elements
	 */
	public void setElements(List<OsmElement> elements) {
		this.elements = elements;
	}
}

