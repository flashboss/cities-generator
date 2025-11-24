package it.vige.cities;

import static java.lang.System.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;

/**
 * Git publisher for cities-generator JSON files
 * Handles cloning, committing, and pushing generated JSON files to Git repositories
 * 
 * @author lucastancapiano
 */
public class GitPublisher {

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
	 * Default Git username for authentication
	 */
	public final static String DEFAULT_GIT_USERNAME = "flashboss";

	/**
	 * Logger for Git publisher operations
	 */
	private Logger logger = getLogger(GitPublisher.class);

	/**
	 * Country code (ISO 3166-1 alpha-2) for commit message generation
	 */
	private String country;

	/**
	 * Language for commit message generation
	 */
	private Languages language;

	/**
	 * Source file path (CITIES_HOME) for reading generated JSON files
	 */
	private String citiesHome;

	/**
	 * Constructor for GitPublisher
	 * 
	 * @param country the country code (ISO 3166-1 alpha-2)
	 * @param language the language enum
	 * @param citiesHome the path to cities-generator home directory
	 */
	public GitPublisher(String country, Languages language, String citiesHome) {
		this.country = country;
		this.language = language != null ? language : Languages.getDefault();
		this.citiesHome = citiesHome;
		logger.debug("GitPublisher initialized - country: {}, language: {}, citiesHome: {}", 
				country, this.language.getCode(), citiesHome);
	}

	/**
	 * Publish generated JSON files to Git repository
	 * 
	 * @param gitRepoUrl the Git repository URL (default: https://github.com/flashboss/cities-generator.git)
	 * @param gitBranch the Git branch (default: master)
	 * @param gitDir the directory in repository where to publish files (default: _db)
	 * @param commitMessage the commit message (optional)
	 * @param gitUsername the Git username for authentication (optional, can also use GIT_USERNAME env var, defaults to "flashboss")
	 * @param gitToken the Git token/password for authentication (optional, can also use GIT_TOKEN or GIT_PASSWORD env var)
	 * @throws Exception if there is a problem
	 */
	public void publish(String gitRepoUrl, String gitBranch, String gitDir, String commitMessage, String gitUsername, String gitToken) throws Exception {
		String repoUrl = gitRepoUrl != null ? gitRepoUrl : DEFAULT_GIT_REPO_URL;
		String branch = gitBranch != null ? gitBranch : DEFAULT_GIT_BRANCH;
		String targetDir = gitDir != null ? gitDir : DEFAULT_GIT_DIR;
		String message = commitMessage != null && !commitMessage.isEmpty() 
			? commitMessage 
			: "Update cities data - " + country + " (" + language.getCode() + ")";
		
		// Get credentials from parameters or environment variables
		String username = gitUsername != null ? gitUsername : (getProperty("GIT_USERNAME") != null ? getProperty("GIT_USERNAME") : DEFAULT_GIT_USERNAME);
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
			String fileName = language.getCode() + ".json";
			String sourceFilePath = citiesHome + continentDir + File.separator + countryDir + File.separator + fileName;
			
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
	public void publish() throws Exception {
		publish(null, null, null, null, null, null);
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

