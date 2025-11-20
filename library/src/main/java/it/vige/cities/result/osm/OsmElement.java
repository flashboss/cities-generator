package it.vige.cities.result.osm;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An OSM element (node, way, or relation)
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmElement {

	private String type;
	private long id;
	private Map<String, String> tags;
	
	/**
	 * Default OSM element
	 */
	public OsmElement() {
	}

	/**
	 * Type (node, way, or relation)
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Type
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * OSM ID
	 * @return the OSM ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * OSM ID
	 * @param id the OSM ID
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Tags (key-value pairs)
	 * @return the tags
	 */
	public Map<String, String> getTags() {
		return tags;
	}

	/**
	 * Tags
	 * @param tags the tags
	 */
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	/**
	 * Get tag value by key
	 * @param key the tag key
	 * @return the tag value or null
	 */
	public String getTag(String key) {
		return tags != null ? tags.get(key) : null;
	}

	/**
	 * Get name from tags (preferring localized name)
	 * @param languageCode the language code (e.g., "it", "en")
	 * @return the name
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

