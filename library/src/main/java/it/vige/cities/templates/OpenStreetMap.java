package it.vige.cities.templates;

import static it.vige.cities.Normalizer.setName;
import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
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
import it.vige.cities.result.osm.OsmElement;
import it.vige.cities.result.osm.OsmResponse;

/**
 * The OpenStreetMap generator using Overpass API
 * 
 * @author lucastancapiano
 */
public class OpenStreetMap extends Template {

	/**
	 * Logger for OpenStreetMap operations
	 */
	private static final Logger logger = getLogger(OpenStreetMap.class);

	/**
	 * Overpass API URL for querying OpenStreetMap data
	 */
	private final static String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter";

	/**
	 * Case sensitive flag for city name matching
	 */
	protected boolean caseSensitive;
	
	/**
	 * Duplicated names flag - allows duplicate city names
	 */
	private boolean duplicatedNames;
	
	/**
	 * Language for location name translations
	 */
	protected Languages language;

	/**
	 * Map of admin_level to OSM ID for the country (not used for regions-only mode)
	 * Used to cache country-level administrative boundaries
	 */
	private Map<Integer, Long> countryOsmIds = new HashMap<>();
	
	/**
	 * Bounding box for the country (not used for regions-only mode with area query)
	 * Format: [minLat, minLon, maxLat, maxLon]
	 */
	private double[] countryBbox = null;

	/**
	 * First level in the hierarchy (0 = regions)
	 */
	protected int firstLevel = 0;

	/**
	 * Constructor for OpenStreetMap template
	 * 
	 * @param country         the country code (ISO 3166-1 alpha-2)
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 * @param language        the language enum for location name translations
	 */
	public OpenStreetMap(String country, boolean caseSensitive, boolean duplicatedNames, Languages language) {
		logger.debug(
				"Creating OpenStreetMap template - country: {}, caseSensitive: {}, duplicatedNames: {}, language: {}",
				country, caseSensitive, duplicatedNames,
				language != null ? language.getCode() : Languages.getDefault().getCode());
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = country;
		this.language = language != null ? language : Languages.getDefault();
		logger.info("OpenStreetMap template initialized - country: {}, language: {}",
				country, this.language.getCode());
	}

	/**
	 * Constructor for OpenStreetMap template (convenience method accepting String)
	 * Accepts language as a string code and converts it to Languages enum
	 * 
	 * @param country         the country code (ISO 3166-1 alpha-2)
	 * @param caseSensitive   true if names should be case-sensitive
	 * @param duplicatedNames true if duplicate names are allowed
	 * @param language        the language code (e.g., "it", "en", "fr")
	 */
	public OpenStreetMap(String country, boolean caseSensitive, boolean duplicatedNames, String language) {
		this(country, caseSensitive, duplicatedNames, Languages.fromCode(language));
	}


	/**
	 * Get administrative boundaries using Overpass API
	 * 
	 * @param adminLevel the administrative level (2=country, 4=region, 6=province,
	 *                   8=municipality)
	 * @return the response as String (JSON)
	 * @throws Exception if there is a problem
	 */
	/**
	 * Extract bounding box from country relation or use hardcoded values
	 * Attempts to get bounding box from OSM relation tags, falls back to hardcoded values for known countries
	 * Format: [minLat, minLon, maxLat, maxLon] = [south, west, north, east]
	 * 
	 * @throws Exception if there is a problem querying OSM or parsing the response
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
								"rel(r:\"boundary\"=\"administrative\",\"admin_level\"=\"4\");\n" +
								"out body;",
						countryId);

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
								countryBbox = new double[] {
										parsedMinLat,
										parsedMinLon,
										parsedMaxLat,
										parsedMaxLon
								};
								logger.info(
										"Extracted valid country bounding box from OSM: [{}, {}, {}, {}] (south, west, north, east)",
										countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3]);
								return;
							} else {
								logger.warn(
										"Invalid bounding box values from OSM - minLat: {}, minLon: {}, maxLat: {}, maxLon: {}",
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
				countryBbox = new double[] { 35.4920, 6.6267, 47.0921, 18.7975 }; // Italy
				logger.info("Using hardcoded bounding box for Italy: [{}, {}, {}, {}] (south, west, north, east)",
						countryBbox[0], countryBbox[1], countryBbox[2], countryBbox[3]);
				break;
			case "GB":
				countryBbox = new double[] { 49.9599, -8.6235, 60.8446, 1.7590 }; // United Kingdom
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
	 * Convenience method that calls executeOverpassQuery with default 3 retries
	 * 
	 * @param query the Overpass QL query string
	 * @return the JSON response as a string
	 * @throws Exception if all retry attempts fail
	 */
	private String executeOverpassQuery(String query) throws Exception {
		return executeOverpassQuery(query, 3); // 3 retries
	}

	/**
	 * Execute Overpass query with retry logic and exponential backoff
	 * Handles rate limiting and network errors with automatic retries
	 * 
	 * @param query the Overpass QL query string
	 * @param maxRetries the maximum number of retry attempts
	 * @return the JSON response as a string
	 * @throws Exception if all retry attempts fail
	 */
	private String executeOverpassQuery(String query, int maxRetries) throws Exception {
		int attempt = 0;
		long baseDelayMs = 2000; // Start with 2 seconds delay
		boolean lastWasRateLimit = false;

		while (attempt < maxRetries) {
			try {
				if (attempt > 0) {
					long delayMs;
					if (lastWasRateLimit) {
						// For rate limiting (429), use longer delays: 30s, 60s, 120s
						delayMs = 30000L * (1L << (attempt - 1)); // 30s, 60s, 120s
						logger.warn("Rate limited detected, using longer delay: {}ms ({} seconds)", delayMs,
								delayMs / 1000);
					} else {
						// For other errors, use exponential backoff: 2s, 4s, 8s
						delayMs = baseDelayMs * (1L << (attempt - 1));
					}
					logger.info("Retrying Overpass query (attempt {}/{}) after {}ms delay",
							attempt + 1, maxRetries, delayMs);
					Thread.sleep(delayMs);
					lastWasRateLimit = false; // Reset after delay
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
				try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),
						StandardCharsets.UTF_8)) {
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
				} else if (responseCode == 429 && attempt < maxRetries - 1) {
					// Rate limiting - retry with longer delay
					logger.warn("Overpass API returned status code 429 (Rate Limited), will retry with longer delay");
					lastWasRateLimit = true;
					connection.disconnect();
					attempt++;
					continue;
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
				if (attempt < maxRetries - 1) {
					String errorMsg = e.getMessage();
					if (errorMsg != null && (errorMsg.contains("504") || errorMsg.contains("429"))) {
						if (errorMsg.contains("429")) {
							lastWasRateLimit = true;
							logger.warn("Overpass query failed with rate limiting, will retry with longer delay: {}",
									errorMsg);
						} else {
							logger.warn("Overpass query failed, will retry: {}", errorMsg);
						}
						attempt++;
						continue;
					}
				}
				throw e;
			}
		}

		throw new Exception("Overpass query failed after " + maxRetries + " attempts");
	}

	/**
	 * Get administrative boundaries from Overpass API
	 * Queries OSM for administrative boundaries at a specific admin_level
	 * 
	 * @param adminLevel the administrative level (2=country, 4=region, 6=province, 8=municipality)
	 * @param parentRelationId the parent relation ID (null for top-level queries)
	 * @param useBbox true to use bounding box query, false to use area-based query
	 * @return the JSON response as a string
	 * @throws Exception if there is a problem executing the Overpass query
	 */
	private String getAdministrativeBoundaries(int adminLevel, Long parentRelationId, boolean useBbox)
			throws Exception {
		logger.debug(
				"Getting administrative boundaries - adminLevel: {}, country: {}, language: {}, parentRelationId: {}, useBbox: {}",
				adminLevel, country, language.getCode(), parentRelationId, useBbox);

		// Build Overpass QL query
		// Query: find relations with boundary=administrative and admin_level=X for the
		// country
		String query;
		if (adminLevel == 2) {
			// Country level: use OSM ID if available (from Nominatim), otherwise search by
			// country code
			Long countryOsmId = countryOsmIds.get(2);
			if (countryOsmId != null) {
				// Use the OSM ID we already have from Nominatim - much faster and avoids
				// timeout
				query = String.format(
						"[out:json][timeout:25];\n" +
								"relation(%d);\n" +
								"out body;",
						countryOsmId);
			} else {
				// Fallback: search by country code (may timeout if server is busy)
				query = String.format(
						"[out:json][timeout:25];\n" +
								"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n"
								+
								"out body;",
						adminLevel, country.toUpperCase());
			}
		} else if (adminLevel == 4) {
			// For regions (admin_level=4): use area-based query with country ISO code
			// This is the most efficient and precise method - no need for countryOsmId or bounding box
			logger.debug("Using area-based query for regions - country: {}", country);
			query = String.format(
					"[out:json][timeout:60];\n" +
							"area[\"ISO3166-1\"=\"%s\"]->.searchArea;\n" +
							"(\n" +
							"  relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"](area.searchArea);\n" +
							");\n" +
							"out body;",
					country.toUpperCase(), adminLevel);
			logger.info("Area-based query for regions: {}", query);
		} else if (countryBbox != null) {
			// Sub-national levels: search by admin_level within country bounding box AND
			// country code
			// Using both bbox and country code filter makes the query more specific and
			// faster
			// Format: (south,west,north,east) - Overpass QL syntax
			// Note: bbox filter must come before other filters in Overpass QL
			// countryBbox format: [minLat, minLon, maxLat, maxLon] = [south, west, north,
			// east]
			double south = countryBbox[0]; // minLat: -90 to 90
			double west = countryBbox[1]; // minLon: -180 to 180
			double north = countryBbox[2]; // maxLat: -90 to 90
			double east = countryBbox[3]; // maxLon: -180 to 180

			logger.debug("Using bounding box for query - south: {}, west: {}, north: {}, east: {}",
					south, west, north, east);

			// Validate bounding box values
			if (south < -90.0 || south > 90.0 || north < -90.0 || north > 90.0 ||
					west < -180.0 || west > 180.0 || east < -180.0 || east > 180.0) {
				logger.error(
						"Invalid bounding box values detected: south={} (range: -90 to 90), west={} (range: -180 to 180), north={} (range: -90 to 90), east={} (range: -180 to 180), falling back to country code filter",
						south, west, north, east);
				query = String.format(
						"[out:json][timeout:25];\n" +
								"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n"
								+
								"out body;",
						adminLevel, country.toUpperCase());
			} else {
				logger.debug("Building Overpass query with bounding box: relation({}, {}, {}, {})",
						south, west, north, east);
				// Use bounding box + admin_level only (no ISO3166-1:alpha2 filter)
				// The bounding box already limits the search geographically to the country
				// Sub-national administrative boundaries (regions, provinces) typically don't
				// have ISO3166-1:alpha2 tags
				query = String.format(Locale.ROOT,
						"[out:json][timeout:60];\n" +
								"relation(%.6f,%.6f,%.6f,%.6f)[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"];\n"
								+
								"out body;",
						south, west, north, east, adminLevel);
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
						logger.error(
								"Invalid bounding box values in fallback: south={} (range: -90 to 90), west={} (range: -180 to 180), north={} (range: -90 to 90), east={} (range: -180 to 180), falling back to country code filter",
								south, west, north, east);
						query = String.format(
								"[out:json][timeout:25];\n" +
										"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n"
										+
										"out body;",
								adminLevel, country.toUpperCase());
					} else {
						logger.debug("Building Overpass query with extracted bounding box: relation({}, {}, {}, {})",
								south, west, north, east);
						// Use bounding box + admin_level only (no ISO3166-1:alpha2 filter)
						// The bounding box already limits the search geographically to the country
						query = String.format(Locale.ROOT,
								"[out:json][timeout:60];\n" +
										"relation(%.6f,%.6f,%.6f,%.6f)[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"];\n"
										+
										"out body;",
								south, west, north, east, adminLevel);
						logger.info("Final Overpass query with extracted bbox: {}", query);
					}
				} else {
					// Last resort: search by admin_level with country code filter (may timeout)
					query = String.format(
							"[out:json][timeout:25];\n" +
									"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n"
									+
									"out body;",
							adminLevel, country.toUpperCase());
				}
			} catch (Exception e) {
				logger.warn("Could not extract bounding box, using country code filter: {}", e.getMessage());
				query = String.format(
						"[out:json][timeout:25];\n" +
								"relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"%d\"][\"ISO3166-1:alpha2\"=\"%s\"];\n"
								+
								"out body;",
						adminLevel, country.toUpperCase());
			}
		}

		return executeOverpassQuery(query);
	}

	/**
	 * Recursively add nodes from OpenStreetMap
	 * Queries OSM for administrative boundaries and recursively processes them up to MAX_LEVEL
	 * 
	 * @param zones       the list of zones to add nodes to
	 * @param numberLevel the current hierarchical level (0=regions, 1=provinces, 2=municipalities)
	 * @param parentId    the parent node ID (used to build hierarchical IDs)
	 * @param adminLevel  the OSM administrative level (4=regions, 6=provinces, 8=municipalities)
	 * @throws Exception if there is a problem fetching or processing nodes from OSM
	 */
	private void addNodes(List<Node> zones, int numberLevel, String parentId, int adminLevel) throws Exception {
		logger.debug("Adding nodes - level: {}, parentId: {}, adminLevel: {}", numberLevel, parentId, adminLevel);

		if (numberLevel > MAX_LEVEL) {
			logger.debug("Max level ({}) reached, stopping recursion", MAX_LEVEL);
			return;
		}

		// Map admin_level to OSM query level
		int queryAdminLevel;
		switch (numberLevel) {
			case 0:
				queryAdminLevel = 4; // Regions
				break;
			case 1:
				queryAdminLevel = 6; // Provinces/Metropolitan cities
				break;
			case 2:
				queryAdminLevel = 8; // Municipalities/Comuni
				break;
			default:
				logger.debug("Stopping at level {} (max level reached)", numberLevel);
				return;
		}

		// Build query based on admin level
		String jsonResponse;
		if (queryAdminLevel == 4) {
			// Regions: use area-based query
			jsonResponse = getAdministrativeBoundaries(queryAdminLevel, null, false);
		} else if (queryAdminLevel == 6 && !parentId.isEmpty()) {
			// Provinces: query within parent region
			// Extract region OSM ID from parentId
			String regionOsmId = parentId;
			if (parentId.contains(ID_SEPARATOR)) {
				regionOsmId = parentId.substring(parentId.lastIndexOf(ID_SEPARATOR) + 1);
			}
			
			// Query provinces within the region using area
			// First get the region relation, then get provinces within it
			String query = String.format(
				"[out:json][timeout:60];\n" +
				"relation(%s);\n" +
				"map_to_area;\n" +
				"(\n" +
				"  relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"6\"](area);\n" +
				");\n" +
				"out body;",
				regionOsmId);
			logger.debug("Querying provinces for region {}: {}", regionOsmId, query);
			jsonResponse = executeOverpassQuery(query);
		} else if (queryAdminLevel == 8 && !parentId.isEmpty()) {
			// Municipalities: query within parent province
			// Extract province OSM ID from parentId
			String provinceOsmId = parentId;
			if (parentId.contains(ID_SEPARATOR)) {
				provinceOsmId = parentId.substring(parentId.lastIndexOf(ID_SEPARATOR) + 1);
			}
			
			// Query municipalities within the province using area
			// First get the province relation, then get municipalities within it
			String query = String.format(
				"[out:json][timeout:60];\n" +
				"relation(%s);\n" +
				"map_to_area;\n" +
				"(\n" +
				"  relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"8\"](area);\n" +
				");\n" +
				"out body;",
				provinceOsmId);
			logger.debug("Querying municipalities for province {}: {}", provinceOsmId, query);
			jsonResponse = executeOverpassQuery(query);
		} else {
			logger.debug("Cannot query without a parent (level: {}, parentId: {})", numberLevel, parentId);
			return; // Cannot query without a parent
		}

		ObjectMapper mapper = new ObjectMapper();
		OsmResponse osmResponse = mapper.readValue(jsonResponse, OsmResponse.class);

		List<OsmElement> elements = osmResponse != null ? osmResponse.getElements() : null;
		logger.debug("Retrieved {} OSM elements for level {} and adminLevel {}",
				elements != null ? elements.size() : 0, numberLevel, queryAdminLevel);

		if (elements != null && !elements.isEmpty()) {
			logger.info("Processing {} OSM elements at level {} (queryAdminLevel: {})", elements.size(),
					numberLevel, queryAdminLevel);

			int skippedAdminLevel = 0;
			int skippedNoName = 0;
			int added = 0;

			for (OsmElement element : elements) {
				// Verify admin_level matches what we're looking for
				String actualAdminLevelStr = element.getTag("admin_level");
				if (actualAdminLevelStr == null) {
					logger.debug("Skipping element {} - no admin_level tag", element.getId());
					skippedAdminLevel++;
					continue;
				}
				
				try {
					int actualAdminLevel = Integer.parseInt(actualAdminLevelStr);
					if (actualAdminLevel != queryAdminLevel) {
						logger.debug("Skipping element {} - admin_level {} doesn't match expected {}",
								element.getId(), actualAdminLevel, queryAdminLevel);
						skippedAdminLevel++;
						continue;
					}
				} catch (NumberFormatException e) {
					logger.debug("Skipping element {} - invalid admin_level: {}", element.getId(),
							actualAdminLevelStr);
					skippedAdminLevel++;
					continue;
				}

				String name = element.getName(language.getCode());
				if (name == null || name.isEmpty()) {
					logger.debug("Skipping element {} with no name", element.getId());
					skippedNoName++;
					continue;
				}

				// Build node ID
				String nodeId;
				if (parentId.isEmpty()) {
					nodeId = String.valueOf(element.getId());
				} else {
					nodeId = parentId + ID_SEPARATOR + element.getId();
				}

				Node node = new Node();
				node.setId(nodeId);
				node.setLevel(numberLevel);
				setName(caseSensitive, duplicatedNames, name, zones, node);
				zones.add(node);
				added++;

				logger.debug("Added node: {} (level: {}, OSM ID: {})", name, numberLevel, element.getId());

				// Recursively add children: regions -> provinces -> municipalities
				if (queryAdminLevel == 4) {
					// Add provinces for regions
					addNodes(node.getZones(), numberLevel + 1, nodeId, 6);
				} else if (queryAdminLevel == 6) {
					// Add municipalities for provinces
					addNodes(node.getZones(), numberLevel + 1, nodeId, 8);
				}
			}

			// Sort nodes alphabetically by name (case-insensitive, like GeoNames)
			// This ensures consistent ordering since Overpass API doesn't guarantee order
			if (!zones.isEmpty()) {
				Collections.sort(zones, new Comparator<Node>() {
					@Override
					public int compare(Node n1, Node n2) {
						String name1 = n1.getName() != null ? n1.getName() : "";
						String name2 = n2.getName() != null ? n2.getName() : "";
						// Case-insensitive comparison to match GeoNames behavior
						return name1.compareToIgnoreCase(name2);
					}
				});
				logger.debug("Sorted {} nodes alphabetically at level {}", zones.size(), numberLevel);
			}

			logger.info(
					"Filtering summary for level {} (queryAdminLevel {}): total={}, added={}, skipped_admin_level={}, skipped_no_name={}",
					numberLevel, queryAdminLevel, elements.size(), added, skippedAdminLevel, skippedNoName);
		} else {
			logger.debug("No nodes found for level {} and adminLevel {}", numberLevel, queryAdminLevel);
		}
	}

	/**
	 * Generate
	 */
	/**
	 * Generate cities data from OpenStreetMap
	 * Queries the Overpass API to retrieve administrative boundaries (regions, provinces, municipalities)
	 * Uses area-based queries for efficient retrieval
	 * 
	 * @return ResultNodes with OK result and generated nodes
	 * @throws Exception if there is a problem querying or processing data from OpenStreetMap
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		logger.info("Starting OpenStreetMap generation for country: {} with language: {}", country, language.getCode());
		Nodes nodes = new Nodes();

		try {
			// Start recursive node addition: regions (level 0) -> provinces (level 1) -> municipalities (level 2)
			// No need for Nominatim or bounding box - area query is self-contained
			addNodes(nodes.getZones(), 0, "", 4);
			logger.info("OpenStreetMap generation completed - total zones: {}",
					nodes.getZones() != null ? nodes.getZones().size() : 0);
		} catch (Exception e) {
			logger.error("Error during OpenStreetMap generation: {}", e.getMessage(), e);
			throw e;
		}

		return new ResultNodes(OK, nodes, this);
	}
}
