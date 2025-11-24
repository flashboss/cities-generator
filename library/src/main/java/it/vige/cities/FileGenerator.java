package it.vige.cities;

import static java.lang.System.getProperty;
import static java.util.Locale.getDefault;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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
	 * Default Git repository URL for publishing JSON files
	 */
	public final static String DEFAULT_GIT_REPO_URL = "https://github.com/flashboss/cities-generator.git";
	
	/**
	 * Default Git branch
	 */
	public final static String DEFAULT_GIT_BRANCH = "master";
	
	/**
	 * Default Git directory path in repository
	 */
	public final static String DEFAULT_GIT_DIR = "_db";

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
		new File(CITIES_HOME).mkdir();
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
		new File(CITIES_HOME).mkdir();
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
		new File(CITIES_HOME).mkdir();
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

	/**
	 * Publish generated JSON files to Git repository
	 * 
	 * @param gitRepoUrl the Git repository URL (default: https://github.com/flashboss/cities-generator.git)
	 * @param gitBranch the Git branch (default: master)
	 * @param gitDir the directory in repository where to publish files (default: _db)
	 * @param commitMessage the commit message (optional)
	 * @param gitUsername the Git username for authentication (optional, can also use GIT_USERNAME env var)
	 * @param gitToken the Git token/password for authentication (optional, can also use GIT_TOKEN or GIT_PASSWORD env var)
	 * @throws Exception if there is a problem
	 */
	public void publishToGit(String gitRepoUrl, String gitBranch, String gitDir, String commitMessage, String gitUsername, String gitToken) throws Exception {
		String repoUrl = gitRepoUrl != null ? gitRepoUrl : DEFAULT_GIT_REPO_URL;
		String branch = gitBranch != null ? gitBranch : DEFAULT_GIT_BRANCH;
		String targetDir = gitDir != null ? gitDir : DEFAULT_GIT_DIR;
		String message = commitMessage != null && !commitMessage.isEmpty() 
			? commitMessage 
			: "Update cities data - " + country + " (" + (language != null ? language.getCode() : Languages.getDefault().getCode()) + ")";
		
		// Get credentials from parameters or environment variables
		String username = gitUsername != null ? gitUsername : getProperty("GIT_USERNAME");
		String token = gitToken != null ? gitToken : (getProperty("GIT_TOKEN") != null ? getProperty("GIT_TOKEN") : getProperty("GIT_PASSWORD"));
		
		logger.info("Publishing to Git - repo: {}, branch: {}, dir: {}", repoUrl, branch, targetDir);
		
		// Create temporary directory for Git clone
		String tempDir = getProperty("java.io.tmpdir") + "/cities-generator-git-" + System.currentTimeMillis();
		File tempDirFile = new File(tempDir);
		tempDirFile.mkdirs();
		
		try {
			// Clone repository
			logger.info("Cloning repository {} to {}", repoUrl, tempDir);
			Git git = Git.cloneRepository()
				.setURI(repoUrl)
				.setBranch(branch)
				.setDirectory(tempDirFile)
				.call();
			
			// Get the file path that was generated
			Continents continent = Continents.fromCountryCode(country);
			String continentDir = continent.getCode();
			String countryDir = country.toUpperCase();
			String lang = this.language.getCode();
			String fileName = lang + ".json";
			String sourceFilePath = CITIES_HOME + continentDir + File.separator + countryDir + File.separator + fileName;
			
			// Create target directory structure in Git repo
			File targetDirFile = new File(tempDir, targetDir + File.separator + continentDir + File.separator + countryDir);
			targetDirFile.mkdirs();
			
			// Copy file to Git repository
			String targetFilePath = targetDir + File.separator + continentDir + File.separator + countryDir + File.separator + fileName;
			File sourceFile = new File(sourceFilePath);
			File targetFile = new File(tempDir, targetFilePath);
			
			if (!sourceFile.exists()) {
				throw new Exception("Source file does not exist: " + sourceFilePath);
			}
			
			logger.info("Copying file from {} to {}", sourceFilePath, targetFile.getAbsolutePath());
			Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			// Add file to Git
			git.add().addFilepattern(targetFilePath).call();
			logger.info("Added file to Git: {}", targetFilePath);
			
			// Check if there are changes to commit
			boolean hasChanges = !git.status().call().getUncommittedChanges().isEmpty();
			if (hasChanges) {
				// Commit changes
				git.commit()
					.setMessage(message)
					.setAuthor("cities-generator", "cities-generator@vige.it")
					.call();
				logger.info("Committed changes with message: {}", message);
				
				// Push to remote (with authentication if provided)
				if (username != null && token != null && !username.isEmpty() && !token.isEmpty()) {
					logger.debug("Using credentials for Git push");
					git.push()
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, token))
						.call();
				} else {
					// Try push without credentials (works for public repos or if credentials are cached)
					logger.debug("Attempting Git push without explicit credentials");
					git.push().call();
				}
				logger.info("Pushed changes to remote repository");
			} else {
				logger.info("No changes to commit");
			}
			
			git.close();
		} catch (GitAPIException e) {
			logger.error("Git API error: {}", e.getMessage(), e);
			throw new Exception("Failed to publish to Git: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IO error: {}", e.getMessage(), e);
			throw new Exception("Failed to publish to Git: " + e.getMessage(), e);
		} finally {
			// Clean up temporary directory
			try {
				deleteDirectory(tempDirFile);
				logger.debug("Cleaned up temporary directory: {}", tempDir);
			} catch (Exception e) {
				logger.warn("Failed to clean up temporary directory: {}", e.getMessage());
			}
		}
	}

	/**
	 * Publish generated JSON files to Git repository (using defaults)
	 * 
	 * @throws Exception if there is a problem
	 */
	public void publishToGit() throws Exception {
		publishToGit(null, null, null, null, null, null);
	}

	/**
	 * Recursively delete a directory
	 * 
	 * @param directory the directory to delete
	 * @throws IOException if there is a problem
	 */
	private void deleteDirectory(File directory) throws IOException {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						deleteDirectory(file);
					} else {
						file.delete();
					}
				}
			}
			directory.delete();
		}
	}

}
