package it.vige.cities.result.osm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Nominatim API search result
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimResult {

	/**
	 * OSM element type: "node", "way", or "relation"
	 */
	@JsonProperty("osm_type")
	private String osmType;
	
	/**
	 * Unique OSM identifier
	 */
	@JsonProperty("osm_id")
	private Long osmId;
	
	/**
	 * Human-readable display name from Nominatim
	 */
	@JsonProperty("display_name")
	private String displayName;
	
	/**
	 * Default constructor for NominatimResult
	 * Creates an empty Nominatim result
	 */
	public NominatimResult() {
	}

	/**
	 * Get the OSM element type
	 * 
	 * @return the OSM type ("node", "way", or "relation")
	 */
	public String getOsmType() {
		return osmType;
	}

	/**
	 * Set the OSM element type
	 * 
	 * @param osmType the OSM type to set
	 */
	public void setOsmType(String osmType) {
		this.osmType = osmType;
	}

	/**
	 * Get the unique OSM identifier
	 * 
	 * @return the OSM ID
	 */
	public Long getOsmId() {
		return osmId;
	}

	/**
	 * Set the unique OSM identifier
	 * 
	 * @param osmId the OSM ID to set
	 */
	public void setOsmId(Long osmId) {
		this.osmId = osmId;
	}

	/**
	 * Get the human-readable display name
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set the human-readable display name
	 * 
	 * @param displayName the display name to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

