package it.vige.cities;

import java.util.ArrayList;
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
import it.vige.cities.templates.en.Britannica;
import it.vige.cities.templates.it.ComuniItaliani;
import it.vige.cities.templates.it.Tuttitalia;

public class Generator extends Template {

	public final static String SINGLE_CASE_SENSITIVE = "s";
	public final static String SINGLE_COUNTRY = "c";
	public final static String SINGLE_PROVIDER = "p";
	public final static String SINGLE_DUPLICATED_NAMES = "d";
	public final static String MULTI_CASE_SENSITIVE = "case_sensitive";
	public final static String MULTI_COUNTRY = "country";
	public final static String MULTI_PROVIDER = "provider";
	public final static String MULTI_DUPLICATED_NAMES = "duplicated";

	private static Logger logger = LoggerFactory.getLogger(Generator.class);

	private String country = Locale.getDefault().getCountry().toLowerCase();
	private boolean caseSensitive;
	private boolean duplicatedNames;
	private String provider;

	public Generator(boolean caseSensitive, boolean duplicatedNames) {
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
	}

	public Generator(String country, boolean caseSensitive, boolean duplicatedNames) {
		this.country = country;
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
	}

	public Generator(String country, String provider, boolean caseSensitive, boolean duplicatedNames) {
		this(country, caseSensitive, duplicatedNames);
		this.provider = provider;
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

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		return cmd;
	}

	private List<Template> getTemplates() {
		List<Template> templates = new ArrayList<Template>();

		switch (Countries.valueOf(country)) {
		case it:
			if (provider == null || provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
				templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
				templates.add(new Tuttitalia(caseSensitive, duplicatedNames));
			} else if (provider.equals(it.vige.cities.templates.it.Providers.TUTTITALIA.name())) {
				templates.add(new Tuttitalia(caseSensitive, duplicatedNames));
				templates.add(new ComuniItaliani(caseSensitive, duplicatedNames));
			}
			break;
		case en:
			templates.add(new Britannica(caseSensitive, duplicatedNames));
			break;
		}
		return templates;
	}

	@Override
	public Nodes generate() {
		logger.info("Start object generation for country: " + country + ", provider: " + provider + ", caseSensitive: "
				+ caseSensitive + ", duplicatedNames: " + duplicatedNames);
		Nodes result = null;
		List<Template> templates = getTemplates();
		try {
			result = templates.get(0).generate();
		} catch (Exception ex) {
			try {
				result = templates.get(1).generate();
			} catch (Exception ex2) {
				return null;
			}
		}
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

			Generator generator = new Generator(country, provider, caseSensitive, duplicatedNames);
			generator.generateFile();
		} catch (MissingOptionException ex) {
			logger.error(ex.getMessage());
		}
	}

}