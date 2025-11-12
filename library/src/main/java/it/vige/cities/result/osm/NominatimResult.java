package it.vige.cities.result.osm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Nominatim API search result
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimResult {

	@JsonProperty("osm_type")
	private String osmType;
	
	@JsonProperty("osm_id")
	private Long osmId;
	
	@JsonProperty("display_name")
	private String displayName;
	
	/**
	 * Default constructor
	 */
	public NominatimResult() {
	}

	/**
	 * OSM type (node, way, relation)
	 * @return the OSM type
	 */
	public String getOsmType() {
		return osmType;
	}

	/**
	 * OSM type
	 * @param osmType the OSM type
	 */
	public void setOsmType(String osmType) {
		this.osmType = osmType;
	}

	/**
	 * OSM ID
	 * @return the OSM ID
	 */
	public Long getOsmId() {
		return osmId;
	}

	/**
	 * OSM ID
	 * @param osmId the OSM ID
	 */
	public void setOsmId(Long osmId) {
		this.osmId = osmId;
	}

	/**
	 * Display name
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Display name
	 * @param displayName the display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

