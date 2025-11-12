package it.vige.cities.templates;

import static it.vige.cities.Normalizer.setName;
import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static java.lang.Long.parseLong;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.result.osm.NominatimResult;
import it.vige.cities.result.osm.OsmElement;
import it.vige.cities.result.osm.OsmResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The OpenStreetMap generator using Overpass API
 * 
 * @author lucastancapiano
 */
public class OpenStreetMap extends Template {

	private static final Logger logger = getLogger(OpenStreetMap.class);

	private final static String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter";
	private final static String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search";

	/**
	 * Case sensitive
	 */
	protected boolean caseSensitive;
	private boolean duplicatedNames;
	/**
	 * Language for translations
	 */
	protected Languages language;

	/**
	 * Map of admin_level to OSM ID for the country
	 */
	private Map<Integer, Long> countryOsmIds = new HashMap<>();
	
	/**
	 * Bounding box for the country (minLat, minLon, maxLat, maxLon)
	 */
	private double[] countryBbox = null;

	/**
	 * First level
	 */
	protected int firstLevel = 0;
	private Client client;

	/**
	 * OpenStreetMap
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames the duplicated names parameter
	 * @param language        the language enum
	 */
	public OpenStreetMap(String country, boolean caseSensitive, boolean duplicatedNames, Languages language) {
		logger.debug("Creating OpenStreetMap template - country: {}, caseSensitive: {}, duplicatedNames: {}, language: {}", 
				country, caseSensitive, duplicatedNames, language != null ? language.getCode() : Languages.getDefault().getCode());
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = country;
		this.language = language != null ? language : Languages.getDefault();
		logger.info("OpenStreetMap template initialized - country: {}, language: {}", 
				country, this.language.getCode());
	}

	/**
	 * OpenStreetMap (convenience method accepting String)
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames the duplicated names parameter
	 * @param language        the language code (e.g., "it", "en")
	 */
	public OpenStreetMap(String country, boolean caseSensitive, boolean duplicatedNames, String language) {
		this(country, caseSensitive, duplicatedNames, Languages.fromCode(language));
	}

	/**
	 * Get country OSM ID using Nominatim
	 * Note: This is optional as Overpass queries work directly with country codes.
	 * 
	 * @param countryCode the country code (ISO 3166-1 alpha-2)
	 * @return the OSM ID of the country
	 * @throws Exception if there is a problem
	 */
	private long getCountryOsmId(String countryCode) throws Exception {
		logger.debug("Getting country OSM ID for: {} with language: {}", countryCode, language.getCode());
		client = newClient();
		WebTarget target = client.target(NOMINATIM_API_URL);
		// Search for country as administrative boundary relation
		// Use q parameter to search for the country name, or use countrycodes with more specific parameters
		target = target.queryParam("q", countryCode)
				.queryParam("countrycodes", countryCode.toLowerCase())
				.queryParam("format", "json")
				.queryParam("limit", "1")
				.queryParam("addressdetails", "1")
				.queryParam("extratags", "1")
				.queryParam("namedetails", "1");
		if (language != null) {
			target = target.queryParam("accept-language", language.getCode());
		}
		logger.debug("Requesting Nominatim URL: {}", target.getUri());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		logger.debug("Response status: {}", response.getStatus());
		
		if (response.getStatus() == 200) {
			// Use RESTEasy automatic deserialization with NominatimResult class
			GenericType<List<NominatimResult>> listType = new GenericType<List<NominatimResult>>() {};
			List<NominatimResult> results = response.readEntity(listType);
			client.close();
			
			if (results != null && !results.isEmpty()) {
				// Try to find a relation with admin_level=2 (country level)
				for (NominatimResult result : results) {
					// Prefer relations (countries are usually relations)
					if ("relation".equals(result.getOsmType()) && result.getOsmId() != null) {
						long osmId = result.getOsmId();
						logger.info("Found country OSM ID: {} (relation) for country: {}", osmId, countryCode);
						return osmId;
					}
				}
				
				// Fallback: use first result if no relation found
				NominatimResult firstResult = results.get(0);
				if (firstResult.getOsmId() != null) {
					long osmId = firstResult.getOsmId();
					logger.info("Found country OSM ID: {} for country: {}", osmId, countryCode);
					return osmId;
				}
			}
		}
		client.close();
		logger.warn("Could not find OSM ID for country: {}", countryCode);
		throw new Exception("Could not find OSM ID for country: " + countryCode);
	}

	/**
	 * Get administrative boundaries using Overpass API
	 * 
	 * @param adminLevel the administrative level (2=country, 4=region, 6=province, 8=municipality)
	 * @return the response as String (JSON)
	 * @throws Exception if there is a problem
	 */
	/**
	 * Extract bounding box from country relation or use hardcoded values
	 */
	private void extractCountryBbox() throws Exception {
		if (countryBbox != null) {
			return; // Already extracted
		}
		
		// Try to get bounding box from Nominatim result
		try {
			Long countryId = countryOsmIds.get(2);
			if (countryId != null) {
				// Get country relation details to extract bounding box
				String query = String.format(
					"[out:json][timeout:25];\n" +
					"relation(%d);\n" +
					"out body;",
					countryId
				);
				
				String jsonResponse = executeOverpassQuery(query);
				ObjectMapper mapper = new ObjectMapper();
				OsmResponse osmResponse = mapper.readValue(jsonResponse, OsmResponse.class);
				
				if (osmResponse != null && osmResponse.getElements() != null && !osmResponse.getElements().isEmpty()) {
					OsmElement countryElement = osmResponse.getElements().get(0);
					// Extract bbox from tags if available
					String minLat = countryElement.getTag("min_lat");
					String minLon = countryElement.getTag("min_lon");
					String maxLat = countryElement.getTag("max_lat");
					String maxLon = countryElement.getTag("max_lon");
					
					if (minLat != null && minLon != null && maxLat != null && maxLon != null) {
						countryBbox = new double[]{
							Double.parseDouble(minLat),
							Double.parseDouble(minLon),
							Double.parseDouble(maxLat),
							Double.parseDouble(maxLon)
						};
						logger.debug("Extracted country bounding box from OSM: [{}, {}, {}, {}]", 
								countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3]);
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Could not extract bounding box from OSM: {}", e.getMessage());
		}
		
		// Fallback: use hardcoded bounding boxes for known countries
		// Format: [minLat, minLon, maxLat, maxLon]
		switch (country.toUpperCase()) {
			case "IT":
				countryBbox = new double[]{35.4920, 6.6267, 47.0921, 18.7975}; // Italy
				logger.debug("Using hardcoded bounding box for Italy");
				break;
			case "GB":
				countryBbox = new double[]{49.9599, -8.6235, 60.8446, 1.7590}; // United Kingdom
				logger.debug("Using hardcoded bounding box for GB");
				break;
			default:
				logger.warn("No bounding box available for country: {}", country);
				break;
		}
	}
	
	/**
	 * Execute Overpass query
	 */
	private String executeOverpassQuery(String query) throws Exception {
		logger.debug("Overpass query: {}", query);
		
		// Use HttpURLConnection directly to avoid RESTEasy writer issues
		URL url = URI.create(OVERPASS_API_URL).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);
		
		// Send query as form data
		String formData = "data=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
		try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
			writer.write(formData);
			writer.flush();
		}
		
		int responseCode = connection.getResponseCode();
		logger.debug("Response status: {}", responseCode);
		
		if (responseCode == 200) {
			StringBuilder response = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
			}
			return response.toString();
		} else {
			// Read error response for debugging
			String errorMessage = "Overpass API returned status code: " + responseCode;
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
				StringBuilder errorResponse = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					errorResponse.append(line);
				}
				if (errorResponse.length() > 0) {
					errorMessage += " - " + errorResponse.toString();
				}
			} catch (Exception e) {
				// Ignore error reading error stream
			}
			throw new Exception(errorMessage);
		}
	}
	
	private String getAdministrativeBoundaries(int adminLevel, Long parentRelationId) throws Exception {
		logger.debug("Getting administrative boundaries - adminLevel: {}, country: {}, language: {}, parentRelationId: {}", 
				adminLevel, country, language.getCode(), parentRelationId);
		
		// Build Overpass QL query
		// Query: find relations with boundary=administrative and admin_level=X for the country
		String query;
		if (adminLevel == 2) {
			// Country level: search by country code
			query = String.format(
				"[out:json][timeout:25];\n" +
				"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
				"out body;",
				adminLevel, country.toUpperCase()
			);
		} else if (countryBbox != null) {
			// Sub-national levels: search by admin_level within country bounding box AND country code
			// Using both bbox and country code filter makes the query more specific and faster
			// Format: (south,west,north,east) - Overpass QL syntax
			// Note: bbox filter must come before other filters in Overpass QL
			query = String.format(
				"[out:json][timeout:25];\n" +
				"relation(%.6f,%.6f,%.6f,%.6f)[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
				"out body;",
				countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3], adminLevel, country.toUpperCase()
			);
		} else {
			// Fallback: try to extract bounding box now, or use country code filter
			try {
				extractCountryBbox();
				if (countryBbox != null) {
					query = String.format(
						"[out:json][timeout:25];\n" +
						"relation(%.6f,%.6f,%.6f,%.6f)[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
						"out body;",
						countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3], adminLevel, country.toUpperCase()
					);
				} else {
					// Last resort: search by admin_level with country code filter (may timeout)
					query = String.format(
						"[out:json][timeout:25];\n" +
						"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
						"out body;",
						adminLevel, country.toUpperCase()
					);
				}
			} catch (Exception e) {
				logger.warn("Could not extract bounding box, using country code filter: {}", e.getMessage());
				query = String.format(
					"[out:json][timeout:25];\n" +
					"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
					"out body;",
					adminLevel, country.toUpperCase()
				);
			}
		}
		
		return executeOverpassQuery(query);
	}

	/**
	 * Add nodes recursively
	 * 
	 * @param zones       the zones
	 * @param numberLevel the number level
	 * @param parentId    the parent ID
	 * @param adminLevel  the administrative level
	 * @throws Exception if there is a problem
	 */
	private void addNodes(List<Node> zones, int numberLevel, String parentId, int adminLevel) throws Exception {
		logger.debug("Adding nodes - level: {}, parentId: {}, adminLevel: {}", numberLevel, parentId, adminLevel);
		
		if (numberLevel > MAX_LEVEL) {
			logger.debug("Max level ({}) reached, stopping recursion", MAX_LEVEL);
			return;
		}

		// Map admin_level to OSM query level
		// admin_level=2: country, admin_level=4: region, admin_level=6: province, admin_level=8: municipality
		int queryAdminLevel;
		switch (numberLevel) {
			case 0:
				queryAdminLevel = 2; // Country
				break;
			case 1:
				queryAdminLevel = 4; // Region/State
				break;
			case 2:
				queryAdminLevel = 6; // Province/County
				break;
			case 3:
				queryAdminLevel = 8; // Municipality/City
				break;
			default:
				logger.debug("Unknown level {}, stopping", numberLevel);
				return;
		}

		// Get parent relation ID if available (for hierarchical queries)
		Long parentRelationId = null;
		if (adminLevel > 2) {
			parentRelationId = countryOsmIds.get(adminLevel - 2);
		}
		
		// Extract bounding box from country relation if available
		if (queryAdminLevel == 2 && countryOsmIds.containsKey(2)) {
			extractCountryBbox();
		}
		
		String jsonResponse = getAdministrativeBoundaries(queryAdminLevel, parentRelationId);
		ObjectMapper mapper = new ObjectMapper();
		OsmResponse osmResponse = mapper.readValue(jsonResponse, OsmResponse.class);
		
		List<OsmElement> elements = osmResponse != null ? osmResponse.getElements() : null;
		logger.debug("Retrieved {} OSM elements for level {} and adminLevel {}", 
				elements != null ? elements.size() : 0, numberLevel, queryAdminLevel);
		
		if (elements != null && !elements.isEmpty()) {
			logger.debug("Processing {} nodes at level {}", elements.size(), numberLevel);
			for (OsmElement element : elements) {
				// Filter by country if not at country level
				if (queryAdminLevel > 2) {
					String countryTag = element.getTag("ISO3166-1:alpha2");
					if (countryTag == null) {
						countryTag = element.getTag("country");
					}
					if (countryTag == null) {
						countryTag = element.getTag("addr:country");
					}
					// Skip if country tag doesn't match (case-insensitive)
					if (countryTag == null || !countryTag.equalsIgnoreCase(country)) {
						logger.debug("Skipping element {} - country tag '{}' doesn't match '{}'", 
								element.getId(), countryTag, country);
						continue;
					}
				}
				
				String name = element.getName(language.getCode());
				if (name == null || name.isEmpty()) {
					logger.debug("Skipping element {} with no name", element.getId());
					continue;
				}
				
				String noFirstLevelId = "";
				if (numberLevel > 0) {
					noFirstLevelId = parentId + ID_SEPARATOR;
				}
				Node node = new Node();
				node.setId(noFirstLevelId + element.getId());
				node.setLevel(numberLevel);
				setName(caseSensitive, duplicatedNames, name, zones, node);
				zones.add(node);
				
				// Store relation ID for potential future use
				if (element.getType().equals("relation")) {
					countryOsmIds.put(queryAdminLevel, element.getId());
				}
				
				logger.debug("Added node: {} (level: {}, OSM ID: {})", name, numberLevel, element.getId());
				
				// Recursively add child nodes
				addNodes(node.getZones(), numberLevel + 1, node.getId(), queryAdminLevel + 2);
			}
		} else {
			logger.debug("No nodes found for level {} and adminLevel {}", numberLevel, queryAdminLevel);
		}
	}

	/**
	 * Generate
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		logger.info("Starting OpenStreetMap generation for country: {} with language: {}", country, language.getCode());
		Nodes nodes = new Nodes();
		
		try {
			// Try to get country OSM ID (optional - not required for Overpass queries)
			// Overpass queries work directly with country codes (ISO3166-1:alpha2)
			try {
				long countryOsmId = getCountryOsmId(country);
				countryOsmIds.put(2, countryOsmId);
				logger.info("Found country OSM ID: {} for country: {}", countryOsmId, country);
			} catch (Exception e) {
				logger.debug("Could not get country OSM ID from Nominatim (not critical): {}", e.getMessage());
				// Continue anyway - Overpass queries work with country codes directly
			}
			
			// Start recursive node addition
			addNodes(nodes.getZones(), firstLevel, "", 2);
			logger.info("OpenStreetMap generation completed - total zones: {}", 
					nodes.getZones() != null ? nodes.getZones().size() : 0);
		} catch (Exception e) {
			logger.error("Error during OpenStreetMap generation: {}", e.getMessage(), e);
			throw e;
		}
		
		return new ResultNodes(OK, nodes, this);
	}
}

