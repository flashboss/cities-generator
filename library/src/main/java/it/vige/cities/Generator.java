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
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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

	private static Logger logger = getLogger(Generator.class);

	private boolean caseSensitive;
	private boolean duplicatedNames;
	private String provider;
	private String username;
	private Languages language;
	private boolean overwrite;

	/**
	 * Generator
	 * 
	 * @param configuration the configuration
	 * @param overwrite     true if the result is to overwrite
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
	 * Configure options
	 * 
	 * @param args the parameters
	 * @return the command line
	 * @throws ParseException
	 */
	private static CommandLine configureOptions(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption(builder(SINGLE_CASE_SENSITIVE).longOpt(MULTI_CASE_SENSITIVE).type(Boolean.class)
				.desc(MULTI_CASE_SENSITIVE).get());
		options.addOption(builder(SINGLE_COUNTRY).longOpt(MULTI_COUNTRY).type(String.class).hasArg().numberOfArgs(1)
				.desc(MULTI_COUNTRY).get());
		options.addOption(builder(SINGLE_PROVIDER).longOpt(MULTI_PROVIDER).type(String.class).hasArg().numberOfArgs(1)
				.desc(MULTI_PROVIDER).get());
		options.addOption(builder(SINGLE_DUPLICATED_NAMES).longOpt(MULTI_DUPLICATED_NAMES).type(Boolean.class)
				.desc(MULTI_DUPLICATED_NAMES).get());
		options.addOption(builder(SINGLE_USER).longOpt(MULTI_USER).type(String.class).hasArg().numberOfArgs(1)
				.desc(MULTI_USER).get());
		options.addOption(builder(SINGLE_LANGUAGE).longOpt(MULTI_LANGUAGE).type(String.class).hasArg().numberOfArgs(1)
				.desc(MULTI_LANGUAGE).get());

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		return cmd;
	}

	/**
	 * Templates
	 * 
	 * @return the list of templates
	 */
	private List<Template> getTemplates() {
		logger.debug("Getting templates for country: {}, provider: {}", country, provider);
		List<Template> templates = new ArrayList<Template>();

		if (provider != null && provider.equals(it.vige.cities.templates.Providers.NONE.name())) {
			logger.debug("Using NONE provider");
			templates.add(new None());
		} else {
			if (country.equals(Countries.IT.name())) {
				logger.debug("Country is IT, configuring Italian templates");
				if (provider == null || provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.WIKIPEDIA.name())) {
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates.add(
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.EXTRA_GEONAMES.name())) {
					templates.add(
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates
							.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.EXTRA_OPENSTREETMAP.name())) {
					templates
							.add(new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					templates.add(
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				} else if (provider.equals(it.vige.cities.templates.Providers.GEONAMES.name())) {
					templates
							.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					templates.add(
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				} else if (provider.equals(it.vige.cities.templates.Providers.OPENSTREETMAP.name())) {
					templates.add(new OpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					templates
							.add(new ExtraOpenStreetMap(Countries.IT.name(), caseSensitive, duplicatedNames, language));
					templates.add(
							new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates
							.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				}
			} else if (country.equals(Countries.GB.name())) {
				if (provider == null || provider.equals(it.vige.cities.templates.Providers.GEONAMES.name())) {
					templates
							.add(new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new Britannica(caseSensitive, duplicatedNames));
					templates.add(new OpenStreetMap(Countries.GB.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.en.Providers.BRITANNICA.name())) {
					templates.add(new Britannica(caseSensitive, duplicatedNames));
					templates
							.add(new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new OpenStreetMap(Countries.GB.name(), caseSensitive, duplicatedNames, language));
				} else if (provider.equals(it.vige.cities.templates.Providers.OPENSTREETMAP.name())) {
					templates.add(new OpenStreetMap(Countries.GB.name(), caseSensitive, duplicatedNames, language));
					templates
							.add(new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username, language));
					templates.add(new Britannica(caseSensitive, duplicatedNames));
				}
			}
		}
		return templates;
	}

	/**
	 * Overwrite
	 * 
	 * @param templates the list of templates
	 * @return the generated nodes
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
	 * Generate
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
	 * Country
	 * 
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Case sensitive
	 * 
	 * @return the case sensitive parameter
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Duplicated names
	 * 
	 * @return the duplicated names parameters
	 */
	public boolean isDuplicatedNames() {
		return duplicatedNames;
	}

	/**
	 * Provider
	 * 
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * Overwrite
	 * 
	 * @return the overwrite parameter
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Overwrite
	 * 
	 * @param overwrite the overwrite parameter
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
			cmd = configureOptions(args);
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
		} catch (MissingOptionException ex) {
			logger.error(ex.getMessage());
		}
	}

}