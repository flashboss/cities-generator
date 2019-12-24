package it.vige.cities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.result.Nodes;
import it.vige.cities.templates.GeoNames;
import it.vige.cities.templates.en.Britannica;
import it.vige.cities.templates.it.ComuniItaliani;
import it.vige.cities.templates.it.ExtraGeoNames;
import it.vige.cities.templates.it.Tuttitalia;

public class Generator extends Template {

	public final static String SINGLE_CASE_SENSITIVE = "s";
	public final static String SINGLE_COUNTRY = "c";
	public final static String SINGLE_PROVIDER = "p";
	public final static String SINGLE_DUPLICATED_NAMES = "d";
	public final static String SINGLE_USER = "u";
	public final static String MULTI_CASE_SENSITIVE = "case_sensitive";
	public final static String MULTI_COUNTRY = "country";
	public final static String MULTI_PROVIDER = "provider";
	public final static String MULTI_DUPLICATED_NAMES = "duplicated";
	public final static String MULTI_USER = "user";

	private static Logger logger = LoggerFactory.getLogger(Generator.class);
	
	private boolean caseSensitive;
	private boolean duplicatedNames;
	private String provider;
	private String username;
	private boolean overwrite;

	public Generator(Configuration configuration, boolean overwrite) {
		this.caseSensitive = configuration.isCaseSensitive();
		this.duplicatedNames = configuration.isDuplicatedNames();
		this.provider = configuration.getProvider();
		this.country = configuration.getCountry();
		this.username = configuration.getUsername();
		this.overwrite = overwrite;
	}

	private static CommandLine configureOptions(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption(Option.builder(SINGLE_CASE_SENSITIVE).longOpt(MULTI_CASE_SENSITIVE).type(Boolean.class)
				.desc(MULTI_CASE_SENSITIVE).build());
		options.addOption(Option.builder(SINGLE_COUNTRY).longOpt(MULTI_COUNTRY).type(String.class).hasArg()
				.numberOfArgs(1).desc(MULTI_COUNTRY).build());
		options.addOption(Option.builder(SINGLE_PROVIDER).longOpt(MULTI_PROVIDER).type(String.class).hasArg()
				.numberOfArgs(1).desc(MULTI_PROVIDER).build());
		options.addOption(Option.builder(SINGLE_DUPLICATED_NAMES).longOpt(MULTI_DUPLICATED_NAMES).type(Boolean.class)
				.desc(MULTI_DUPLICATED_NAMES).build());
		options.addOption(Option.builder(SINGLE_USER).longOpt(MULTI_USER).type(String.class).hasArg().numberOfArgs(1)
				.desc(MULTI_USER).build());

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		return cmd;
	}

	private List<Template> getTemplates() {
		List<Template> templates = new ArrayList<Template>();

		if (country.equals(Countries.it.name())) {
			if (provider == null || provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
				templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				templates.add(new Tuttitalia(caseSensitive, duplicatedNames));
				templates.add(new ExtraGeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
				templates.add(new GeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
			} else if (provider.equals(it.vige.cities.templates.it.Providers.TUTTITALIA.name())) {
				templates.add(new Tuttitalia(caseSensitive, duplicatedNames));
				templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				templates.add(new ExtraGeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
				templates.add(new GeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
			} else if (provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
				templates.add(new Tuttitalia(caseSensitive, duplicatedNames));
				templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				templates.add(new ExtraGeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
				templates.add(new GeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
			} else if (provider.equals(it.vige.cities.templates.it.Providers.EXTRA_GEONAMES.name())) {
				templates.add(new ExtraGeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
				templates.add(new Tuttitalia(caseSensitive, duplicatedNames));
				templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				templates.add(new GeoNames(Countries.it.name(), caseSensitive, duplicatedNames, username));
			}
		} else if (country.equals(Countries.uk.name())) {
			if (provider == null || provider.equals(it.vige.cities.templates.en.Providers.BRITANNICA.name())) {
				templates.add(new Britannica(caseSensitive, duplicatedNames));
				templates.add(new GeoNames(Countries.uk.name(), caseSensitive, duplicatedNames, username));
			} else if (provider.equals(it.vige.cities.templates.en.Providers.GEONAMES.name())) {
				templates.add(new GeoNames(Countries.uk.name(), caseSensitive, duplicatedNames, username));
				templates.add(new Britannica(caseSensitive, duplicatedNames));
			}
		} else
			templates.add(new GeoNames(country, caseSensitive, duplicatedNames, username));
		return templates;
	}

	private Nodes overwrite(List<Template> templates) {
		Nodes result = null;
		Iterator<Template> iterator = templates.iterator();
		while (iterator.hasNext() && result == null) {
			try {
				result = iterator.next().generate();
			} catch (Exception ex) {
				result = null;
			}
		}
		return result;
	}

	@Override
	public Nodes generate() {
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
		return result;
	}

	@Override
	public Result generateFile() {
		logger.info("Start file generation for country: " + country + ", provider: " + provider + ", caseSensitive: "
				+ caseSensitive + ", duplicatedNames: " + duplicatedNames);
		Result result = null;
		List<Template> templates = getTemplates();
		result = templates.get(0).generateFile();
		if (result == Result.KO)
			templates.get(1).generateFile();
		return result;
	}

	public String getCountry() {
		return country;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isDuplicatedNames() {
		return duplicatedNames;
	}

	public String getProvider() {
		return provider;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public static void main(String[] args) throws Exception {
		CommandLine cmd = null;
		try {
			cmd = configureOptions(args);
			String country = null;
			Object fromCountry = cmd.getParsedOptionValue(SINGLE_COUNTRY);
			if (fromCountry == null)
				country = Locale.getDefault().getCountry().toLowerCase();
			else
				country = fromCountry + "";
			String provider = cmd.hasOption(SINGLE_PROVIDER) ? cmd.getParsedOptionValue(SINGLE_PROVIDER) + "" : null;
			boolean caseSensitive = cmd.hasOption(Generator.SINGLE_CASE_SENSITIVE);
			boolean duplicatedNames = cmd.hasOption(Generator.SINGLE_DUPLICATED_NAMES);

			Configuration configuration = new Configuration();
			configuration.setCaseSensitive(caseSensitive);
			configuration.setCountry(country);
			configuration.setDuplicatedNames(duplicatedNames);
			configuration.setProvider(provider);
			Generator generator = new Generator(configuration, true);
			generator.generateFile();
		} catch (MissingOptionException ex) {
			logger.error(ex.getMessage());
		}
	}

}