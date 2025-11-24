package it.vige.cities.result.osm;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An OSM element (node, way, or relation)
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmElement {

	/**
	 * OSM element type: "node", "way", or "relation"
	 */
	private String type;
	
	/**
	 * Unique OSM identifier
	 */
	private long id;
	
	/**
	 * Map of OSM tags (key-value pairs containing metadata)
	 */
	private Map<String, String> tags;
	
	/**
	 * Default constructor for OsmElement
	 * Creates an empty OSM element
	 */
	public OsmElement() {
	}

	/**
	 * Get the OSM element type
	 * 
	 * @return the type ("node", "way", or "relation")
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the OSM element type
	 * 
	 * @param type the type to set ("node", "way", or "relation")
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the unique OSM identifier
	 * 
	 * @return the OSM ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the unique OSM identifier
	 * 
	 * @param id the OSM ID to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get the map of OSM tags
	 * Tags contain metadata about the element (e.g., name, admin_level, etc.)
	 * 
	 * @return the map of tags (key-value pairs)
	 */
	public Map<String, String> getTags() {
		return tags;
	}

	/**
	 * Set the map of OSM tags
	 * 
	 * @param tags the map of tags to set
	 */
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	/**
	 * Get tag value by key
	 * 
	 * @param key the tag key to look up
	 * @return the tag value or null if not found
	 */
	public String getTag(String key) {
		return tags != null ? tags.get(key) : null;
	}

	/**
	 * Get name from tags, preferring localized name
	 * First tries to get a localized name (e.g., "name:it", "name:en"), then falls back to "name"
	 * 
	 * @param languageCode the language code (e.g., "it", "en", "fr")
	 * @return the name in the specified language, or the default name, or null if not found
	 */
	public String getName(String languageCode) {
		if (tags == null) {
			return null;
		}
		// Try localized name first (e.g., name:it, name:en)
		String localizedKey = "name:" + languageCode;
		if (tags.containsKey(localizedKey)) {
			return tags.get(localizedKey);
		}
		// Fallback to default name
		return tags.get("name");
	}
}

