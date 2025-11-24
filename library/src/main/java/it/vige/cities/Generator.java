package it.vige.cities;

import static it.vige.cities.Result.KO;
import static it.vige.cities.Result.OK;
import static java.util.Locale.getDefault;
import static org.apache.commons.cli.Option.builder;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;

import it.vige.cities.result.Nodes;
import it.vige.cities.templates.GeoNames;
import it.vige.cities.templates.None;
import it.vige.cities.templates.OpenStreetMap;
import it.vige.cities.templates.en.Britannica;
import it.vige.cities.templates.it.ComuniItaliani;
import it.vige.cities.templates.it.ExtraGeoNames;
import it.vige.cities.templates.it.ExtraOpenStreetMap;
import it.vige.cities.templates.it.Wikipedia;

/**
 * Generator of names
 * 
 * @author lucastancapiano
 */
public class Generator extends Template {

	/**
	 * Single case sensitive
	 */
	public final static String SINGLE_CASE_SENSITIVE = "s";

	/**
	 * Single country
	 */
	public final static String SINGLE_COUNTRY = "c";

	/**
	 * Singlw provider
	 */
	public final static String SINGLE_PROVIDER = "p";

	/**
	 * Single duplicated names
	 */
	public final static String SINGLE_DUPLICATED_NAMES = "d";

	/**
	 * Single user
	 */
	public final static String SINGLE_USER = "u";

	/**
	 * Single language
	 */
	public final static String SINGLE_LANGUAGE = "l";

	/**
	 * Multi case sensitive
	 */
	public final static String MULTI_CASE_SENSITIVE = "case_sensitive";

	/**
	 * Multi country
	 */
	public final static String MULTI_COUNTRY = "country";

	/**
	 * Multi provider
	 */
	public final static String MULTI_PROVIDER = "provider";

	/**
	 * Multi duplicated names
	 */
	public final static String MULTI_DUPLICATED_NAMES = "duplicated";

	/**
	 * Multi user
	 */
	public final static String MULTI_USER = "user";

	/**
	 * Multi language
	 */
	public final static String MULTI_LANGUAGE = "language";

	/**
	 * Single help
	 */
	public final static String SINGLE_HELP = "h";

	/**
	 * Multi help
	 */
	public final static String MULTI_HELP = "help";

	/**
	 * Single git config
	 */
	public final static String SINGLE_GIT_CONFIG = "g";

	/**
	 * Multi git config
	 */
	public final static String MULTI_GIT_CONFIG = "git-config";

	/**
	 * Logger for generator operations
	 */
	private static Logger logger = getLogger(Generator.class);

	/**
	 * Case sensitive flag for city name matching
	 */
	private boolean caseSensitive;
	
	/**
	 * Duplicated names flag - allows duplicate city names
	 */
	private boolean duplicatedNames;
	
	/**
	 * Provider name for data generation
	 */
	private String provider;
	
	/**
	 * Username for GeoNames API
	 */
	private String username;
	
	/**
	 * Language for location names
	 */
	private Languages language;
	
	/**
	 * Overwrite flag - if true, generates new data even if file exists
	 */
	private boolean overwrite;

	/**
	 * Constructor for Generator
	 * 
	 * @param configuration the configuration containing country, language, provider, and other settings
	 * @param overwrite     true if the result should overwrite existing files, false to read existing files if available
	 */
	public Generator(Configuration configuration, boolean overwrite) {
		this.caseSensitive = configuration.isCaseSensitive();
		this.duplicatedNames = configuration.isDuplicatedNames();
		this.provider = configuration.getProvider();
		this.country = configuration.getCountry();
		this.username = configuration.getUsername();
		this.language = configuration.getLanguage();
		this.overwrite = overwrite;
	}

	/**
	 * Print help message to standard output
	 * Formats and displays all available command line options with descriptions
	 * 
	 * @param options the command line options to display
	 */
	private static void printHelp(Options options) {
		System.out.println("Usage: java -jar cities-generator-<version>.jar [options]");
		System.out.println("Generate cities data for different countries and languages");
		System.out.println();
		System.out.println("Options:");
		
		int padding = 40;
		int maxLineWidth = 80;
		
		for (Option option : options.getOptions()) {
			StringBuilder optLine = new StringBuilder();
			optLine.append("  ");
			
			if (option.getOpt() != null) {
				optLine.append("-").append(option.getOpt());
			}
			if (option.getLongOpt() != null) {
				if (option.getOpt() != null) {
					optLine.append(", ");
				}
				optLine.append("--").append(option.getLongOpt());
			}
			if (option.hasArg()) {
				optLine.append(" <arg>");
			}
			
			// Pad to align descriptions
			int currentLength = optLine.length();
			if (currentLength < padding) {
				for (int i = currentLength; i < padding; i++) {
					optLine.append(" ");
				}
			} else {
				optLine.append("\n");
				for (int i = 0; i < padding; i++) {
					optLine.append(" ");
				}
			}
			
			if (option.getDescription() != null) {
				String description = option.getDescription();
				String[] wrappedLines = wrapText(description, maxLineWidth - padding);
				
				// First line
				optLine.append(wrappedLines[0]);
				System.out.println(optLine.toString());
				
				// Subsequent lines with proper indentation
				for (int i = 1; i < wrappedLines.length; i++) {
					StringBuilder continuationLine = new StringBuilder();
					for (int j = 0; j < padding; j++) {
						continuationLine.append(" ");
					}
					continuationLine.append(wrappedLines[i]);
					System.out.println(continuationLine.toString());
				}
			} else {
				System.out.println(optLine.toString());
			}
		}
		
		System.out.println();
		System.out.println("For more information, visit: https://cities-generator.vige.it");
	}
	
	/**
	 * Parse Git configuration from CSV string
	 * Format: key=value pairs separated by commas
	 * Keys: repo, branch, dir, username, token, message
	 * 
	 * @param csvConfig the CSV configuration string
	 * @return a map with the parsed values
	 */
	private static java.util.Map<String, String> parseGitConfig(String csvConfig) {
		java.util.Map<String, String> config = new java.util.HashMap<>();
		if (csvConfig == null || csvConfig.trim().isEmpty()) {
			return config;
		}
		
		String[] pairs = csvConfig.split(",");
		for (String pair : pairs) {
			pair = pair.trim();
			if (pair.isEmpty()) {
				continue;
			}
			int equalsIndex = pair.indexOf('=');
			if (equalsIndex > 0) {
				String key = pair.substring(0, equalsIndex).trim();
				String value = pair.substring(equalsIndex + 1).trim();
				if (!key.isEmpty() && !value.isEmpty()) {
					config.put(key.toLowerCase(), value);
				}
			}
		}
		return config;
	}

	/**
	 * Wrap text to fit within a maximum line width, breaking at word boundaries
	 * 
	 * @param text the text to wrap
	 * @param maxWidth maximum width of each line
	 * @return array of wrapped lines
	 */
	private static String[] wrapText(String text, int maxWidth) {
		if (text == null || text.isEmpty()) {
			return new String[] { "" };
		}
		
		java.util.List<String> lines = new java.util.ArrayList<>();
		String[] words = text.split("\\s+");
		StringBuilder currentLine = new StringBuilder();
		
		for (String word : words) {
			if (currentLine.length() == 0) {
				currentLine.append(word);
			} else if (currentLine.length() + 1 + word.length() <= maxWidth) {
				currentLine.append(" ").append(word);
			} else {
				lines.add(currentLine.toString());
				currentLine = new StringBuilder(word);
			}
		}
		
		if (currentLine.length() > 0) {
			lines.add(currentLine.toString());
		}
		
		return lines.toArray(new String[0]);
	}

	/**
	 * Create command line options for the generator
	 * Defines all available CLI parameters including short and long forms
	 * 
	 * @return the configured Options object with all command line options
	 */
	private static Options createOptions() {
		Options options = new Options();
		options.addOption(builder(SINGLE_CASE_SENSITIVE).longOpt(MULTI_CASE_SENSITIVE).type(Boolean.class)
				.desc("Enable case-sensitive matching for city names. If not specified, defaults to false (case-insensitive).").get());
		options.addOption(builder(SINGLE_COUNTRY).longOpt(MULTI_COUNTRY).type(String.class).hasArg().numberOfArgs(1)
				.desc("Country code (ISO 3166-1 alpha-2) for the generated cities (e.g., GB, IT, US, FR). If not specified, uses the default locale of the machine.").get());
		options.addOption(builder(SINGLE_PROVIDER).longOpt(MULTI_PROVIDER).type(String.class).hasArg().numberOfArgs(1)
				.desc("Choose the primary data provider. For GB: BRITANNICA, GEONAMES, OPENSTREETMAP. For IT: COMUNI_ITALIANI, WIKIPEDIA, EXTRA_GEONAMES, EXTRA_OPENSTREETMAP, GEONAMES, OPENSTREETMAP. For other countries: GEONAMES, OPENSTREETMAP. If not specified, uses default provider order.").get());
		options.addOption(builder(SINGLE_DUPLICATED_NAMES).longOpt(MULTI_DUPLICATED_NAMES).type(Boolean.class)
				.desc("Allow duplicate city names (e.g., 'Bedford (City)' and 'Bedford (Town)'). If not specified, defaults to false (no duplicates).").get());
		options.addOption(builder(SINGLE_USER).longOpt(MULTI_USER).type(String.class).hasArg().numberOfArgs(1)
				.desc("Optional username for GEONAMES and EXTRA_GEONAMES providers. If not specified, 'vota' is used as default.").get());
		options.addOption(builder(SINGLE_LANGUAGE).longOpt(MULTI_LANGUAGE).type(String.class).hasArg().numberOfArgs(1)
				.desc("Language code for location names (e.g., 'it', 'en', 'fr', 'de', 'es', 'pt'). Supported: IT (Italian, default), EN (English), FR (French), DE (German), ES (Spanish), PT (Portuguese). If not specified, defaults to 'it'.").get());
		options.addOption(builder(SINGLE_GIT_CONFIG).longOpt(MULTI_GIT_CONFIG).type(String.class).hasArg().numberOfArgs(1)
				.desc("Publish generated JSON files to Git repository. Format: CSV with key=value pairs separated by commas. Keys: repo (repository URL), branch (default: master), dir (default: _db), username, token, message (commit message). Example: --git-config \"repo=https://github.com/user/repo.git,branch=main,dir=data,username=user,token=xxx,message=Update\". All fields except repo are optional. Default repo: https://github.com/flashboss/cities-generator.git. Username and token can also be set via GIT_USERNAME and GIT_TOKEN environment variables.").get());
		options.addOption(builder(SINGLE_HELP).longOpt(MULTI_HELP).desc("Print this help message and exit.").get());
		return options;
	}


	/**
	 * Add template to list only if it supports the current language
	 * Checks if the template supports the configured language before adding it to the list
	 * 
	 * @param templates the list of templates to add to
	 * @param template  the template to check and potentially add
	 */
	private void addTemplateIfLanguageSupported(List<Template> templates, Template template) {
		Languages currentLanguage = this.language != null ? this.language : Languages.getDefault();
		if (template.isLanguageSupported(currentLanguage)) {
			templates.add(template);
			logger.debug("Added template {} - supports language {}", template.getClass().getSimpleName(), currentLanguage.getCode());
		} else {
			logger.debug("Skipped template {} - does not support language {}", template.getClass().getSimpleName(), currentLanguage.getCode());
		}
	}

	/**
	 * Get list of templates based on country and provider configuration
	 * Returns templates in priority order based on the configured provider
	 * 
	 * @return the list of templates ordered by priority for the current configuration
	 */
	private List<Template> getTemplates() {
		logger.debug("Getting templates for country: {}, provider: {}, language: {}", country, provider, language != null ? language.getCode() : Languages.getDefault().getCode());
		List<Template> templates = new ArrayList<Template>();

		if (provider != null && provider.equals(it.vige.cities.templates.Providers.NONE.name())) {
			logger.debug("Using NONE provider");
			templates.add(new None());
		} else {
			if (country.equals(Countries.IT.name())) {
				logger.debug("Country is IT, configuring Italian templates");
				if (provider == null || provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
					addTemplateIfLanguageSupported(templates, new ComuniItaliani(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new Wikipedia(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates,
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.WIKIPEDIA.name())) {
					addTemplateIfLanguageSupported(templates, new Wikipedia(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new ComuniItaliani(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates,
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.EXTRA_GEONAMES.name())) {
					addTemplateIfLanguageSupported(templates,
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new Wikipedia(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new ComuniItaliani(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.EXTRA_OPENSTREETMAP.name())) {
					addTemplateIfLanguageSupported(templates,
							new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new Wikipedia(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new ComuniItaliani(caseSensitive, duplicatedNames));
				} else if (provider.equals(it.vige.cities.templates.Providers.GEONAMES.name())) {
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new Wikipedia(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new ComuniItaliani(caseSensitive, duplicatedNames));
				} else if (provider.equals(it.vige.cities.templates.Providers.OPENSTREETMAP.name())) {
					addTemplateIfLanguageSupported(templates, new OpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					addTemplateIfLanguageSupported(templates,
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new Wikipedia(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new ComuniItaliani(caseSensitive, duplicatedNames));
				}
			} else if (country.equals(Countries.GB.name())) {
				if (provider == null || provider.equals(it.vige.cities.templates.Providers.GEONAMES.name())) {
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new Britannica(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates, new OpenStreetMap(Countries.GB.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.en.Providers.BRITANNICA.name())) {
					addTemplateIfLanguageSupported(templates, new Britannica(caseSensitive, duplicatedNames));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new OpenStreetMap(Countries.GB.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.Providers.OPENSTREETMAP.name())) {
					addTemplateIfLanguageSupported(templates, new OpenStreetMap(Countries.GB.name(), caseSensitive, duplicatedNames, language));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new Britannica(caseSensitive, duplicatedNames));
				}
			} else {
				// Generic countries (not IT, not GB)
				logger.debug("Country is {}, configuring generic templates", country);
				if (provider == null || provider.equals(it.vige.cities.templates.Providers.GEONAMES.name())) {
					addTemplateIfLanguageSupported(templates,
							new GeoNames(country, caseSensitive, duplicatedNames, username, language));
					addTemplateIfLanguageSupported(templates, new OpenStreetMap(country, caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.Providers.OPENSTREETMAP.name())) {
					addTemplateIfLanguageSupported(templates, new OpenStreetMap(country, caseSensitive, duplicatedNames, language));
					addTemplateIfLanguageSupported(templates,
							new GeoNames(country, caseSensitive, duplicatedNames, username, language));
				}
			}
		}
		return templates;
	}

	/**
	 * Generate nodes by trying templates in order until one succeeds
	 * Attempts each template in the list until one successfully generates data
	 * 
	 * @param templates the list of templates to try in order
	 * @return the generated nodes from the first successful template, or null if all fail
	 */
	private Nodes overwrite(List<Template> templates) {
		logger.debug("Starting overwrite with {} templates", templates.size());
		Nodes result = null;
		Iterator<Template> iterator = templates.iterator();
		int templateIndex = 0;
		while (iterator.hasNext() && result == null) {
			templateIndex++;
			try {
				Template template = iterator.next();
				logger.debug("Trying template {}: {}", templateIndex, template.getClass().getSimpleName());
				// Set language and country on template
				template.language = this.language;
				template.country = this.country;
				logger.debug("Template configured - country: {}, language: {}", this.country,
						this.language != null ? this.language.getCode() : Languages.getDefault().getCode());
				ResultNodes resultG = template.generateFile();
				logger.debug("Template {} generation result: {}", template.getClass().getSimpleName(),
						resultG.getResult());
				if (resultG.getResult() == OK) {
					logger.info("Template {} succeeded, reading file", template.getClass().getSimpleName());
					result = template.readFile();
					logger.info("Successfully generated nodes using template: {}", template.getClass().getSimpleName());
				} else {
					logger.debug("Template {} failed, trying next template", template.getClass().getSimpleName());
				}
			} catch (Exception ex) {
				logger.debug("Template {} threw exception: {}", templateIndex, ex.getMessage(), ex);
				result = null;
			}
		}
		if (result == null) {
			logger.warn("All {} templates failed to generate nodes", templates.size());
		}
		return result;
	}

	/**
	 * Generate cities data nodes
	 * Generates data using configured templates, or reads from existing file if available and overwrite is false
	 * 
	 * @return ResultNodes containing the generated nodes and result status (OK or KO)
	 */
	@Override
	public ResultNodes generate() {
		logger.info(
				"Start object generation for country: " + country + ", provider: " + provider + ", caseSensitive: "
						+ caseSensitive + ", duplicatedNames: " + duplicatedNames + ", username: " + username
						+ ", language: " + (language != null ? language.getCode() : Languages.getDefault().getCode())
						+ ", overwrite: " + overwrite);
		logger.debug(
				"Generation parameters - country: {}, provider: {}, caseSensitive: {}, duplicatedNames: {}, language: {}, overwrite: {}",
				country, provider, caseSensitive, duplicatedNames,
				language != null ? language.getCode() : Languages.getDefault().getCode(), overwrite);
		Nodes result = null;
		List<Template> templates = getTemplates();
		logger.debug("Retrieved {} templates for generation", templates.size());
		if (overwrite) {
			logger.debug("Overwrite mode enabled, generating new data");
			result = overwrite(templates);
		} else if (exists()) {
			logger.debug("File exists, attempting to read from file");
			try {
				result = readFile();
				logger.info("Successfully read existing file");
			} catch (Exception e) {
				logger.warn("Failed to read existing file: {}, will generate new data", e.getMessage(), e);
				result = null;
			}
		} else {
			logger.debug("File does not exist, generating new data");
			result = overwrite(templates);
		}
		if (result != null) {
			logger.info("End object generation - SUCCESS (nodes: {})",
					result.getZones() != null ? result.getZones().size() : 0);
		} else {
			logger.warn("End object generation - FAILED (no nodes generated)");
		}
		return result == null ? new ResultNodes(KO, result, this) : new ResultNodes(OK, result, this);
	}

	/**
	 * Get the country code
	 * 
	 * @return the country code (ISO 3166-1 alpha-2)
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Check if case-sensitive matching is enabled
	 * 
	 * @return true if case-sensitive matching is enabled, false otherwise
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Check if duplicate names are allowed
	 * 
	 * @return true if duplicate city names are allowed, false otherwise
	 */
	public boolean isDuplicatedNames() {
		return duplicatedNames;
	}

	/**
	 * Get the provider name
	 * 
	 * @return the provider name, or null if using default provider order
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * Check if overwrite mode is enabled
	 * 
	 * @return true if overwrite mode is enabled, false otherwise
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Set overwrite mode
	 * 
	 * @param overwrite true to enable overwrite mode (always generate new data), false to read existing files if available
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * Main
	 * 
	 * @param args the parameters to start the generator
	 * @throws Exception if there is a problem
	 */
	public static void main(String[] args) throws Exception {
		CommandLine cmd = null;
		try {
			Options options = createOptions();
			CommandLineParser parser = new DefaultParser();
			cmd = parser.parse(options, args);
			
			// Check if help was requested
			if (cmd.hasOption(SINGLE_HELP) || cmd.hasOption(MULTI_HELP)) {
				printHelp(options);
				return;
			}
			
			String country = null;
			Object fromCountry = cmd.getParsedOptionValue(SINGLE_COUNTRY);
			if (fromCountry == null)
				country = getDefault().getCountry().toUpperCase();
			else
				country = (fromCountry + "").toUpperCase();
			String provider = cmd.hasOption(SINGLE_PROVIDER) ? cmd.getParsedOptionValue(SINGLE_PROVIDER) + "" : null;
			boolean caseSensitive = cmd.hasOption(SINGLE_CASE_SENSITIVE);
			boolean duplicatedNames = cmd.hasOption(SINGLE_DUPLICATED_NAMES);
			String language = cmd.hasOption(SINGLE_LANGUAGE) ? cmd.getParsedOptionValue(SINGLE_LANGUAGE) + "" : "it";

			Configuration configuration = new Configuration();
			configuration.setCaseSensitive(caseSensitive);
			configuration.setCountry(country);
			configuration.setDuplicatedNames(duplicatedNames);
			configuration.setLanguage(language);
			configuration.setProvider(provider);
			Generator generator = new Generator(configuration, true);
			generator.generate();
			
			// Publish to Git if requested
			if (cmd.hasOption(SINGLE_GIT_CONFIG) || cmd.hasOption(MULTI_GIT_CONFIG)) {
				String gitConfigValue = cmd.hasOption(SINGLE_GIT_CONFIG) ? cmd.getOptionValue(SINGLE_GIT_CONFIG)
					: cmd.getOptionValue(MULTI_GIT_CONFIG);
				
				java.util.Map<String, String> gitConfig = parseGitConfig(gitConfigValue);
				String gitRepoUrl = gitConfig.get("repo");
				String gitBranch = gitConfig.get("branch");
				String gitDir = gitConfig.get("dir");
				String commitMessage = gitConfig.get("message");
				String gitUsername = gitConfig.get("username");
				String gitToken = gitConfig.get("token");
				
				try {
					Languages lang = Languages.fromCode(language);
					GitPublisher gitPublisher = new GitPublisher(country, lang, FileGenerator.CITIES_HOME);
					gitPublisher.publish(gitRepoUrl, gitBranch, gitDir, commitMessage, gitUsername, gitToken);
					logger.info("Successfully published to Git repository");
				} catch (Exception e) {
					logger.error("Failed to publish to Git: {}", e.getMessage(), e);
					throw e;
				}
			}
		} catch (MissingOptionException ex) {
			logger.error(ex.getMessage());
		}
	}

}