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
import java.util.Locale;
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
	private NominatimResult countryNominatimResult;
	
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
	private NominatimResult getCountryFromNominatim(String countryCode) throws Exception {
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
						logger.info("Found country OSM ID: {} (relation) for country: {}", result.getOsmId(), countryCode);
						return result;
					}
				}
				
				// Fallback: use first result if no relation found
				NominatimResult firstResult = results.get(0);
				if (firstResult.getOsmId() != null) {
					logger.info("Found country OSM ID: {} for country: {}", firstResult.getOsmId(), countryCode);
					return firstResult;
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
					logger.debug("Country element tags: {}", countryElement.getTags());
					// Extract bbox from tags if available
					String minLat = countryElement.getTag("min_lat");
					String minLon = countryElement.getTag("min_lon");
					String maxLat = countryElement.getTag("max_lat");
					String maxLon = countryElement.getTag("max_lon");
					
					logger.debug("Bounding box tags from OSM - min_lat: {}, min_lon: {}, max_lat: {}, max_lon: {}", 
							minLat, minLon, maxLat, maxLon);
					
					if (minLat != null && minLon != null && maxLat != null && maxLon != null) {
						try {
							double parsedMinLat = Double.parseDouble(minLat);
							double parsedMinLon = Double.parseDouble(minLon);
							double parsedMaxLat = Double.parseDouble(maxLat);
							double parsedMaxLon = Double.parseDouble(maxLon);
							
							logger.debug("Parsed bounding box values - minLat: {}, minLon: {}, maxLat: {}, maxLon: {}", 
									parsedMinLat, parsedMinLon, parsedMaxLat, parsedMaxLon);
							
							// Validate before using
							if (parsedMinLat >= -90.0 && parsedMinLat <= 90.0 &&
							    parsedMinLon >= -180.0 && parsedMinLon <= 180.0 &&
							    parsedMaxLat >= -90.0 && parsedMaxLat <= 90.0 &&
							    parsedMaxLon >= -180.0 && parsedMaxLon <= 180.0) {
								countryBbox = new double[]{
									parsedMinLat,
									parsedMinLon,
									parsedMaxLat,
									parsedMaxLon
								};
								logger.info("Extracted valid country bounding box from OSM: [{}, {}, {}, {}] (south, west, north, east)", 
										countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3]);
								return;
							} else {
								logger.warn("Invalid bounding box values from OSM - minLat: {}, minLon: {}, maxLat: {}, maxLon: {}", 
										parsedMinLat, parsedMinLon, parsedMaxLat, parsedMaxLon);
							}
						} catch (NumberFormatException e) {
							logger.warn("Could not parse bounding box values from OSM: {}", e.getMessage());
						}
					} else {
						logger.debug("Bounding box tags not found in OSM relation, will use hardcoded values");
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Could not extract bounding box from OSM: {}", e.getMessage());
		}
		
		// Fallback: use hardcoded bounding boxes for known countries
		// Format: [minLat, minLon, maxLat, maxLon] = [south, west, north, east]
		switch (country.toUpperCase()) {
			case "IT":
				countryBbox = new double[]{35.4920, 6.6267, 47.0921, 18.7975}; // Italy
				logger.info("Using hardcoded bounding box for Italy: [{}, {}, {}, {}] (south, west, north, east)", 
						countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3]);
				break;
			case "GB":
				countryBbox = new double[]{49.9599, -8.6235, 60.8446, 1.7590}; // United Kingdom
				logger.info("Using hardcoded bounding box for GB: [{}, {}, {}, {}] (south, west, north, east)", 
						countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3]);
				break;
			default:
				logger.warn("No bounding box available for country: {}", country);
				break;
		}
	}
	
	/**
	 * Execute Overpass query with retry logic
	 */
	private String executeOverpassQuery(String query) throws Exception {
		return executeOverpassQuery(query, 3); // 3 retries
	}
	
	/**
	 * Execute Overpass query with retry logic
	 */
	private String executeOverpassQuery(String query, int maxRetries) throws Exception {
		int attempt = 0;
		long baseDelayMs = 2000; // Start with 2 seconds delay
		
		while (attempt < maxRetries) {
			try {
				if (attempt > 0) {
					long delayMs = baseDelayMs * (1L << (attempt - 1)); // Exponential backoff: 2s, 4s, 8s
					logger.info("Retrying Overpass query (attempt {}/{}) after {}ms delay", 
							attempt + 1, maxRetries, delayMs);
					Thread.sleep(delayMs);
				}
				
				logger.info("Executing Overpass query (attempt {}/{}): {}", attempt + 1, maxRetries, query);
				
				// Use HttpURLConnection directly to avoid RESTEasy writer issues
				URL url = URI.create(OVERPASS_API_URL).toURL();
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/json");
				connection.setDoOutput(true);
				connection.setConnectTimeout(30000); // 30 seconds connect timeout
				connection.setReadTimeout(60000); // 60 seconds read timeout
				
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
				} else if (responseCode == 504 && attempt < maxRetries - 1) {
					// Gateway timeout - retry
					logger.warn("Overpass API returned status code 504 (Gateway Timeout), will retry");
					connection.disconnect();
					attempt++;
					continue;
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
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new Exception("Interrupted while waiting to retry Overpass query", e);
			} catch (Exception e) {
				if (attempt < maxRetries - 1 && e.getMessage() != null && e.getMessage().contains("504")) {
					logger.warn("Overpass query failed, will retry: {}", e.getMessage());
					attempt++;
					continue;
				}
				throw e;
			}
		}
		
		throw new Exception("Overpass query failed after " + maxRetries + " attempts");
	}
	
	private String getAdministrativeBoundaries(int adminLevel, Long parentRelationId) throws Exception {
		return getAdministrativeBoundaries(adminLevel, parentRelationId, false);
	}
	
	private String getAdministrativeBoundaries(int adminLevel, Long parentRelationId, boolean useBbox) throws Exception {
		logger.debug("Getting administrative boundaries - adminLevel: {}, country: {}, language: {}, parentRelationId: {}, useBbox: {}", 
				adminLevel, country, language.getCode(), parentRelationId, useBbox);
		
		// Build Overpass QL query
		// Query: find relations with boundary=administrative and admin_level=X for the country
		String query;
		if (adminLevel == 2) {
			// Country level: use OSM ID if available (from Nominatim), otherwise search by country code
			Long countryOsmId = countryOsmIds.get(2);
			if (countryOsmId != null) {
				// Use the OSM ID we already have from Nominatim - much faster and avoids timeout
				query = String.format(
					"[out:json][timeout:25];\n" +
					"relation(%d);\n" +
					"out body;",
					countryOsmId
				);
			} else {
				// Fallback: search by country code (may timeout if server is busy)
				query = String.format(
					"[out:json][timeout:25];\n" +
					"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
					"out body;",
					adminLevel, country.toUpperCase()
				);
			}
		} else if (countryBbox != null) {
			// Sub-national levels: search by admin_level within country bounding box AND country code
			// Using both bbox and country code filter makes the query more specific and faster
			// Format: (south,west,north,east) - Overpass QL syntax
			// Note: bbox filter must come before other filters in Overpass QL
			// countryBbox format: [minLat, minLon, maxLat, maxLon] = [south, west, north, east]
			double south = countryBbox[0]; // minLat: -90 to 90
			double west = countryBbox[1];  // minLon: -180 to 180
			double north = countryBbox[2]; // maxLat: -90 to 90
			double east = countryBbox[3];  // maxLon: -180 to 180
			
			logger.debug("Using bounding box for query - south: {}, west: {}, north: {}, east: {}", 
					south, west, north, east);
			
			// Validate bounding box values
			if (south < -90.0 || south > 90.0 || north < -90.0 || north > 90.0 ||
			    west < -180.0 || west > 180.0 || east < -180.0 || east > 180.0) {
				logger.error("Invalid bounding box values detected: south={} (range: -90 to 90), west={} (range: -180 to 180), north={} (range: -90 to 90), east={} (range: -180 to 180), falling back to country code filter", 
						south, west, north, east);
				query = String.format(
					"[out:json][timeout:25];\n" +
					"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
					"out body;",
					adminLevel, country.toUpperCase()
				);
			} else {
				logger.debug("Building Overpass query with bounding box: relation({}, {}, {}, {})", 
						south, west, north, east);
				// Use bounding box + admin_level only (no ISO3166-1:alpha2 filter)
				// The bounding box already limits the search geographically to the country
				// Sub-national administrative boundaries (regions, provinces) typically don't have ISO3166-1:alpha2 tags
				query = String.format(Locale.ROOT,
					"[out:json][timeout:60];\n" +
					"relation(%.6f,%.6f,%.6f,%.6f)[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"];\n" +
					"out body;",
					south, west, north, east, adminLevel
				);
				logger.info("Final Overpass query with bbox: {}", query);
			}
		} else {
			// Fallback: try to extract bounding box now, or use country code filter
			try {
				extractCountryBbox();
				if (countryBbox != null) {
					double south = countryBbox[0];
					double west = countryBbox[1];
					double north = countryBbox[2];
					double east = countryBbox[3];
					
					logger.debug("Extracted bounding box for fallback query - south: {}, west: {}, north: {}, east: {}", 
							south, west, north, east);
					
					// Validate bounding box values
					if (south < -90.0 || south > 90.0 || north < -90.0 || north > 90.0 ||
					    west < -180.0 || west > 180.0 || east < -180.0 || east > 180.0) {
						logger.error("Invalid bounding box values in fallback: south={} (range: -90 to 90), west={} (range: -180 to 180), north={} (range: -90 to 90), east={} (range: -180 to 180), falling back to country code filter", 
								south, west, north, east);
						query = String.format(
							"[out:json][timeout:25];\n" +
							"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n" +
							"out body;",
							adminLevel, country.toUpperCase()
						);
					} else {
						logger.debug("Building Overpass query with extracted bounding box: relation({}, {}, {}, {})", 
								south, west, north, east);
						// Use bounding box + admin_level only (no ISO3166-1:alpha2 filter)
						// The bounding box already limits the search geographically to the country
						query = String.format(Locale.ROOT,
							"[out:json][timeout:60];\n" +
							"relation(%.6f,%.6f,%.6f,%.6f)[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"];\n" +
							"out body;",
							south, west, north, east, adminLevel
						);
						logger.info("Final Overpass query with extracted bbox: {}", query);
					}
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
		// admin_level=2: country, admin_level=4: region, admin_level=6: province
		// Note: stopping at provinces level, excluding municipalities
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
			default:
				logger.debug("Stopping at level {} (provinces level reached, municipalities excluded)", numberLevel);
				return;
		}

		// Get parent relation ID if available (for hierarchical queries)
		Long parentRelationId = null;
		if (adminLevel > 2) {
			parentRelationId = countryOsmIds.get(adminLevel - 2);
		}
		
		// Bounding box should already be extracted in generate() method
		// If not, try to extract it now (fallback)
		if (countryBbox == null && queryAdminLevel == 2 && countryOsmIds.containsKey(2)) {
			extractCountryBbox();
		}
		
		// For country level (admin_level=2), use Nominatim data directly to avoid Overpass timeout
		if (queryAdminLevel == 2 && countryNominatimResult != null) {
			logger.debug("Using Nominatim data directly for country level to avoid Overpass timeout");
			String countryName = countryNominatimResult.getDisplayName();
			// Extract country name from display_name (format: "CountryName, ...")
			if (countryName != null && countryName.contains(",")) {
				countryName = countryName.split(",")[0].trim();
			}
			if (countryName == null || countryName.isEmpty()) {
				countryName = country.toUpperCase(); // Fallback to country code
			}
			
			Node node = new Node();
			String countryNodeId = String.valueOf(countryNominatimResult.getOsmId());
			node.setId(countryNodeId);
			node.setLevel(numberLevel);
			setName(caseSensitive, duplicatedNames, countryName, zones, node);
			zones.add(node);
			logger.debug("Added country node: {} (level: {}, OSM ID: {})", countryName, numberLevel, countryNominatimResult.getOsmId());
			
			// Store OSM ID for hierarchical queries
			countryOsmIds.put(queryAdminLevel, countryNominatimResult.getOsmId());
			
			// Recursively add child nodes (regions, provinces, municipalities)
			addNodes(node.getZones(), numberLevel + 1, node.getId(), queryAdminLevel + 2);
		} else {
			// Check if we're using bounding box (more reliable than country tag filtering)
			boolean usingBbox = (countryBbox != null && queryAdminLevel > 2);
			String jsonResponse = getAdministrativeBoundaries(queryAdminLevel, parentRelationId, usingBbox);
			ObjectMapper mapper = new ObjectMapper();
			OsmResponse osmResponse = mapper.readValue(jsonResponse, OsmResponse.class);
			
			List<OsmElement> elements = osmResponse != null ? osmResponse.getElements() : null;
			logger.debug("Retrieved {} OSM elements for level {} and adminLevel {}", 
					elements != null ? elements.size() : 0, numberLevel, queryAdminLevel);
			
			if (elements != null && !elements.isEmpty()) {
				logger.info("Processing {} OSM elements at level {} (queryAdminLevel: {})", elements.size(), numberLevel, queryAdminLevel);
				// Log admin_level distribution for debugging
				Map<Integer, Integer> adminLevelCounts = new HashMap<>();
				for (OsmElement element : elements) {
					String adminLevelStr = element.getTag("admin_level");
					if (adminLevelStr != null) {
						try {
							int actualAdminLevel = Integer.parseInt(adminLevelStr);
							adminLevelCounts.put(actualAdminLevel, adminLevelCounts.getOrDefault(actualAdminLevel, 0) + 1);
						} catch (NumberFormatException e) {
							// Ignore
						}
					}
				}
				if (!adminLevelCounts.isEmpty()) {
					logger.info("Admin level distribution for queryAdminLevel {}: {}", queryAdminLevel, adminLevelCounts);
				}
				
				// Log sample element names for debugging
				int sampleCount = Math.min(5, elements.size());
				logger.info("Sample of {} elements returned (showing first {}):", elements.size(), sampleCount);
				for (int i = 0; i < sampleCount; i++) {
					OsmElement element = elements.get(i);
					String name = element.getName(language.getCode());
					String adminLevelStr = element.getTag("admin_level");
					String countryTag = element.getTag("ISO3166-1:alpha2");
					logger.info("  - Element {}: name='{}', admin_level={}, country={}, type={}", 
							element.getId(), name, adminLevelStr, countryTag, element.getType());
				}
				
				int skippedAdminLevel = 0;
				int skippedCountry = 0;
				int skippedNoName = 0;
				int added = 0;
				
				for (OsmElement element : elements) {
					// Verify admin_level matches what we're looking for
					String actualAdminLevelStr = element.getTag("admin_level");
					if (actualAdminLevelStr != null) {
						try {
							int actualAdminLevel = Integer.parseInt(actualAdminLevelStr);
							if (actualAdminLevel != queryAdminLevel) {
								logger.debug("Skipping element {} - admin_level {} doesn't match expected {}", 
										element.getId(), actualAdminLevel, queryAdminLevel);
								skippedAdminLevel++;
								continue;
							}
						} catch (NumberFormatException e) {
							// If we can't parse admin_level, skip it
							logger.debug("Skipping element {} - invalid admin_level: {}", element.getId(), actualAdminLevelStr);
							skippedAdminLevel++;
							continue;
						}
					} else {
						logger.debug("Skipping element {} - no admin_level tag", element.getId());
						skippedAdminLevel++;
						continue;
					}
					
					// Filter by country only if NOT using bounding box (bbox already limits geographically)
					if (queryAdminLevel > 2 && !usingBbox) {
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
							skippedCountry++;
							continue;
						}
					}
					
					String name = element.getName(language.getCode());
					if (name == null || name.isEmpty()) {
						logger.debug("Skipping element {} with no name", element.getId());
						skippedNoName++;
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
					added++;
					
					// Store relation ID for potential future use
					if (element.getType().equals("relation")) {
						countryOsmIds.put(queryAdminLevel, element.getId());
					}
					
					logger.debug("Added node: {} (level: {}, OSM ID: {})", name, numberLevel, element.getId());
					
					// Recursively add child nodes (stop at provinces level, exclude municipalities)
					if (queryAdminLevel < 6) {
						addNodes(node.getZones(), numberLevel + 1, node.getId(), queryAdminLevel + 2);
					} else {
						logger.debug("Stopping recursion at provinces level (level {}), municipalities excluded", numberLevel);
					}
				}
				
				logger.info("Filtering summary for level {} (queryAdminLevel {}): total={}, added={}, skipped_admin_level={}, skipped_country={}, skipped_no_name={}", 
						numberLevel, queryAdminLevel, elements.size(), added, skippedAdminLevel, skippedCountry, skippedNoName);
			} else {
				logger.debug("No nodes found for level {} and adminLevel {}", numberLevel, queryAdminLevel);
			}
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
				countryNominatimResult = getCountryFromNominatim(country);
				long countryOsmId = countryNominatimResult.getOsmId();
				countryOsmIds.put(2, countryOsmId);
				logger.info("Found country OSM ID: {} for country: {}", countryOsmId, country);
				
				// Extract bounding box early so it's available for all sub-national levels
				// Note: This may timeout, but we have hardcoded fallback values
				try {
					extractCountryBbox();
				} catch (Exception e2) {
					logger.debug("Could not extract bounding box from Overpass (will use hardcoded values): {}", e2.getMessage());
				}
			} catch (Exception e) {
				logger.debug("Could not get country OSM ID from Nominatim (not critical): {}", e.getMessage());
				// Continue anyway - Overpass queries work with country codes directly
				// Try to extract bounding box anyway (uses hardcoded values if OSM ID not available)
				try {
					extractCountryBbox();
				} catch (Exception e2) {
					logger.debug("Could not extract bounding box: {}", e2.getMessage());
				}
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

