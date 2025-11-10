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
import it.vige.cities.templates.en.Britannica;
import it.vige.cities.templates.it.ComuniItaliani;
import it.vige.cities.templates.it.ExtraGeoNames;
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

	private static Logger logger = getLogger(Generator.class);

	private boolean caseSensitive;
	private boolean duplicatedNames;
	private String provider;
	private String username;
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
		List<Template> templates = new ArrayList<Template>();

		if (provider != null && provider.equals(it.vige.cities.templates.Providers.NONE.name())) {
			templates.add(new None());
		} else {
			if (country.equals(Countries.IT.name())) {
				if (provider == null || provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
					templates.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.WIKIPEDIA.name())) {
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates.add(new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
					templates.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates.add(new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
					templates.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
				} else if (provider.equals(it.vige.cities.templates.it.Providers.EXTRA_GEONAMES.name())) {
					templates.add(new ExtraGeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
					templates.add(new Wikipedia(caseSensitive, duplicatedNames));
					templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
					templates.add(new GeoNames(Countries.IT.name(), caseSensitive, duplicatedNames, username));
				}
			} else if (country.equals(Countries.GB.name())) {
				if (provider == null || provider.equals(it.vige.cities.templates.en.Providers.GEONAMES.name())) {
					templates.add(new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username));
					templates.add(new Britannica(caseSensitive, duplicatedNames));
				} else if (provider.equals(it.vige.cities.templates.en.Providers.BRITANNICA.name())) {
					templates.add(new Britannica(caseSensitive, duplicatedNames));
					templates.add(new GeoNames(Countries.GB.name(), caseSensitive, duplicatedNames, username));
				}
			} else
				templates.add(new GeoNames(country, caseSensitive, duplicatedNames, username));
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
		Nodes result = null;
		Iterator<Template> iterator = templates.iterator();
		while (iterator.hasNext() && result == null) {
			try {
				Template template = iterator.next();
				ResultNodes resultG = template.generateFile();
				if (resultG.getResult() == OK)
					result = template.readFile();
			} catch (Exception ex) {
				result = null;
			}
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
						+ caseSensitive + ", duplicatedNames: " + duplicatedNames + ", username: " + username,
				", overwrite: " + overwrite);
		Nodes result = null;
		List<Template> templates = getTemplates();
		if (overwrite)
			result = overwrite(templates);
		else if (exists())
			try {
				result = readFile();
			} catch (Exception e) {
				result = null;
			}
		else
			result = overwrite(templates);
		logger.info("End object generation");
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

			Configuration configuration = new Configuration();
			configuration.setCaseSensitive(caseSensitive);
			configuration.setCountry(country);
			configuration.setDuplicatedNames(duplicatedNames);
			configuration.setProvider(provider);
			Generator generator = new Generator(configuration, true);
			generator.generate();
		} catch (MissingOptionException ex) {
			logger.error(ex.getMessage());
		}
	}

}