package it.vige.cities;

import static java.lang.System.getProperty;
import static java.util.Locale.getDefault;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.time.Instant;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.vige.cities.result.Nodes;

/**
 * Generator through file
 * @author lucastancapiano
 */
public class FileGenerator {

	/**
	 * Folder home
	 */
	public final static String CITIES_HOME = getProperty("user.home") + "/cities-generator/";
	
	/**
	 * Version of cities-generator
	 * Reads from manifest Implementation-Version, or defaults to "unknown" if not available
	 */
	public final static String VERSION = getVersion();
	
	/**
	 * Get version from manifest or properties file or default
	 * @return the version string
	 */
	private static String getVersion() {
		String version = null;
		
		// First try: Package Implementation-Version (works when running from JAR)
		version = FileGenerator.class.getPackage().getImplementationVersion();
		
		// Second try: Read from version.properties file (works during tests and development)
		if (version == null || version.isEmpty()) {
			try {
				java.io.InputStream is = FileGenerator.class.getResourceAsStream("/it/vige/cities/version.properties");
				if (is != null) {
					java.util.Properties props = new java.util.Properties();
					props.load(is);
					version = props.getProperty("version");
					is.close();
				}
			} catch (Exception e) {
				// Ignore
			}
		}
		
		// Third try: Read from manifest directly (fallback)
		if (version == null || version.isEmpty()) {
			try {
				java.net.URL url = FileGenerator.class.getProtectionDomain().getCodeSource().getLocation();
				if (url != null) {
					java.util.jar.JarFile jarFile = new java.util.jar.JarFile(new java.io.File(url.toURI()));
					java.util.jar.Manifest manifest = jarFile.getManifest();
					if (manifest != null) {
						version = manifest.getMainAttributes().getValue("Implementation-Version");
					}
					jarFile.close();
				}
			} catch (Exception e) {
				// Ignore
			}
		}
		
		return version != null && !version.isEmpty() ? version : "unknown";
	}

	/**
	 * Country code (ISO 3166-1 alpha-2) for the generated cities
	 * Defaults to the country from the system default locale
	 */
	protected String country = getDefault().getCountry().toUpperCase();
	
	/**
	 * Language for location names
	 * Defaults to IT (Italian)
	 */
	protected Languages language = Languages.getDefault();

	/**
	 * Logger for file generator operations
	 */
	private Logger logger = getLogger(FileGenerator.class);

	/**
	 * Jackson ObjectMapper for JSON serialization/deserialization
	 */
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Default constructor for FileGenerator
	 * Initializes the file generator with default settings
	 */
	public FileGenerator() {
		logger.debug("Creating FileGenerator instance");
		logger.info("FileGenerator initialized - CITIES_HOME: {}, Version: {}", CITIES_HOME, VERSION);
	}

	/**
	 * Ensure CITIES_HOME directory exists
	 * Creates the directory structure if needed
	 */
	private void ensureCitiesHome() {
		new File(CITIES_HOME).mkdirs();
	}

	/**
	 * Write nodes to JSON file
	 * Convenience method that calls writeFile(nodes, null)
	 * 
	 * @param nodes the nodes to write to file
	 * @throws Exception if there is a problem writing the file
	 */
	protected void writeFile(Nodes nodes) throws Exception {
		writeFile(nodes, null);
	}

	/**
	 * Write nodes to JSON file with metadata
	 * Creates the directory structure {continent}/{country}/{language}.json and writes the nodes
	 * Adds copyright, generation date, and template name to the JSON output
	 * 
	 * @param nodes the nodes to write to file
	 * @param templateName the name of the template used to generate the data (optional, can be null)
	 * @throws Exception if there is a problem writing the file
	 */
	protected void writeFile(Nodes nodes, String templateName) throws Exception {
		logger.debug("Starting writeFile - country: {}, language: {}, template: {}", country, language != null ? language.getCode() : Languages.getDefault().getCode(), templateName);
		ensureCitiesHome();
		// Structure: {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
		Continents continent = Continents.fromCountryCode(country);
		String continentDir = continent.getCode();
		String countryDir = country.toUpperCase();
		String lang = this.language.getCode();
		File continentFolder = new File(CITIES_HOME + continentDir);
		continentFolder.mkdirs();
		File countryFolder = new File(CITIES_HOME + continentDir + File.separator + countryDir);
		countryFolder.mkdirs();
		String fileName = lang + ".json";
		String name = CITIES_HOME + continentDir + File.separator + countryDir + File.separator + fileName;
		logger.debug("File path: {}", name);
		
		// Serialize nodes to JSON
		logger.debug("Serializing nodes to JSON");
		JsonNode jsonNode = mapper.valueToTree(nodes);
		logger.debug("Nodes serialized, total zones: {}", nodes != null && nodes.getZones() != null ? nodes.getZones().size() : 0);
		
		// Add copyright as first field
		ObjectNode objectNode = (ObjectNode) jsonNode;
		String copyright = "Generated by cities-generator maintained by Vige community (https://cities-generator.vige.it), version " + VERSION;
		if (templateName != null && !templateName.isEmpty()) {
			copyright += ", template: " + templateName;
		}
		copyright += ". Copyright (c) Vige, Home of Professional Open Source. Licensed under the Apache License, Version 2.0.";
		ObjectNode newObjectNode = mapper.createObjectNode();
		newObjectNode.put("copyright", copyright);
		newObjectNode.put("generationDate", Instant.now().toString());
		newObjectNode.setAll(objectNode);
		
		// Write the modified JSON to file
		logger.debug("Writing JSON to file: {}", name);
		mapper.writeValue(new File(name), newObjectNode);
		logger.info("File generated successfully in {}", name);
		logger.debug("File content preview: {}", mapper.writeValueAsString(newObjectNode).substring(0, Math.min(200, mapper.writeValueAsString(newObjectNode).length())));
	}

	/**
	 * Read nodes from JSON file
	 * Convenience method that reads using the current country and language
	 * 
	 * @return the nodes read from file
	 * @throws Exception if there is a problem reading the file
	 */
	protected Nodes readFile() throws Exception {
		return readFile(country);
	}

	/**
	 * Read nodes from JSON file for a specific country
	 * Uses the current language setting
	 * 
	 * @param country the country code (ISO 3166-1 alpha-2) to read
	 * @return the nodes read from file for the specified country
	 * @throws Exception if there is a problem reading the file
	 */
	protected Nodes readFile(String country) throws Exception {
		return readFile(country, language);
	}
	
	/**
	 * Read nodes from JSON file for a specific country and language
	 * Reads from the file path: {continent}/{country}/{language}.json
	 * 
	 * @param country the country code (ISO 3166-1 alpha-2) to read
	 * @param language the language enum to read
	 * @return the nodes read from file for the specified country and language
	 * @throws Exception if there is a problem reading the file
	 */
	protected Nodes readFile(String country, Languages language) throws Exception {
		logger.debug("Reading file - country: {}, language: {}", country, language != null ? language.getCode() : Languages.getDefault().getCode());
		ensureCitiesHome();
		// Structure: {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
		Continents continent = Continents.fromCountryCode(country);
		String continentDir = continent.getCode();
		String countryDir = country.toUpperCase();
		Languages lang = language != null ? language : Languages.getDefault();
		String fileName = lang.getCode() + ".json";
		String filePath = CITIES_HOME + continentDir + File.separator + countryDir + File.separator + fileName;
		logger.debug("Reading from file: {}", filePath);
		Nodes nodes = mapper.readValue(new File(filePath), Nodes.class);
		logger.info("File read successfully from: {}", filePath);
		logger.debug("Nodes loaded - total zones: {}", nodes != null && nodes.getZones() != null ? nodes.getZones().size() : 0);
		return nodes;
	}

	/**
	 * Read nodes from JSON file for a specific country and language (convenience method)
	 * Accepts language as a string code and converts it to Languages enum
	 * 
	 * @param country the country code (ISO 3166-1 alpha-2) to read
	 * @param language the language code (e.g., "it", "en", "fr")
	 * @return the nodes read from file for the specified country and language
	 * @throws Exception if there is a problem reading the file
	 */
	protected Nodes readFile(String country, String language) throws Exception {
		return readFile(country, Languages.fromCode(language));
	}

	/**
	 * Check if the JSON file exists for the current country and language
	 * Checks the file path: {continent}/{country}/{language}.json
	 * 
	 * @return true if the file exists, false otherwise
	 */
	protected boolean exists() {
		logger.debug("Checking if file exists - country: {}, language: {}", country, language != null ? language.getCode() : Languages.getDefault().getCode());
		ensureCitiesHome();
		// Structure: {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
		Continents continent = Continents.fromCountryCode(country);
		String continentDir = continent.getCode();
		String countryDir = country.toUpperCase();
		String lang = this.language.getCode();
		String fileName = lang + ".json";
		String filePath = CITIES_HOME + continentDir + File.separator + countryDir + File.separator + fileName;
		boolean exists = new File(CITIES_HOME).exists() && new File(filePath).exists();
		logger.debug("File existence check - path: {}, exists: {}", filePath, exists);
		return exists;
	}


}
