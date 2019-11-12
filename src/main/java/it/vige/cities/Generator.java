package it.vige.cities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.templates.en.CityMetric;
import it.vige.cities.templates.en.CityPopulation;
import it.vige.cities.templates.it.ComuniItaliani;
import it.vige.cities.templates.it.Tuttitalia;

public class Generator extends Template {

	public final static String SINGLE_CASE_SENSITIVE = "s";
	public final static String SINGLE_COUNTRY = "c";
	public final static String SINGLE_PROVIDER = "p";
	public final static String MULTI_CASE_SENSITIVE = "case_sensitive";
	public final static String MULTI_COUNTRY = "country";
	public final static String MULTI_PROVIDER = "provider";

	private Logger logger = LoggerFactory.getLogger(Generator.class);

	private String provider;
	private String country;
	private boolean caseSensitive;

	public Generator(String country, String provider, boolean caseSensitive) {
		this.provider = provider;
		this.country = country;
		this.caseSensitive = caseSensitive;
	}

	private static CommandLine configureOptions(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption(Option.builder(SINGLE_CASE_SENSITIVE).longOpt(MULTI_CASE_SENSITIVE).type(Boolean.class)
				.desc(MULTI_CASE_SENSITIVE).build());
		options.addOption(Option.builder(SINGLE_COUNTRY).longOpt(MULTI_COUNTRY).required().type(String.class).hasArg()
				.numberOfArgs(1).desc(MULTI_COUNTRY).build());
		options.addOption(Option.builder(SINGLE_PROVIDER).longOpt(MULTI_PROVIDER).type(String.class).hasArg()
				.numberOfArgs(1).desc(MULTI_PROVIDER).build());

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		String provider = cmd.getParsedOptionValue(SINGLE_COUNTRY) + "";
		try {
			Countries.valueOf(provider);
		} catch (IllegalArgumentException ex) {
			throw new ParseException(provider + " provided with the " + SINGLE_COUNTRY
					+ " argument is not in the list: " + Arrays.toString(Countries.values()));
		}
		return cmd;
	}

	public Result generate() {
		logger.info("Start generation");
		Result result = null;
		List<Template> templates = new ArrayList<Template>();

		switch (Countries.valueOf(country)) {
		case IT:
			if (provider == null || provider.equals(it.vige.cities.templates.it.Providers.TUTTITALIA.name())) {
				templates.add(new ComuniItaliani(caseSensitive));
				templates.add(new Tuttitalia(caseSensitive));
			} else if (provider.equals(it.vige.cities.templates.it.Providers.COMUNI_ITALIANI.name())) {
				templates.add(new Tuttitalia(caseSensitive));
				templates.add(new ComuniItaliani(caseSensitive));
			}
			result = templates.get(0).generate();
			if (result == Result.KO)
				templates.get(1).generate();
			break;
		case EN:
			if (provider.isEmpty() || provider.equals(it.vige.cities.templates.en.Providers.CITYMETRIC.name())) {
				templates.add(new CityMetric(caseSensitive));
				templates.add(new CityPopulation(caseSensitive));
			} else if (provider.equals(it.vige.cities.templates.en.Providers.CITYPOPULATION.name())) {
				templates.add(new CityPopulation(caseSensitive));
				templates.add(new CityMetric(caseSensitive));
			}
			result = templates.get(0).generate();
			if (result == Result.KO)
				templates.get(1).generate();
			break;
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		CommandLine cmd = configureOptions(args);
		String country = cmd.getParsedOptionValue(SINGLE_COUNTRY) + "";
		String provider = cmd.hasOption(SINGLE_PROVIDER) ? cmd.getParsedOptionValue(SINGLE_PROVIDER) + "" : null;
		boolean caseSensitive = cmd.hasOption(Generator.SINGLE_CASE_SENSITIVE);

		Generator generator = new Generator(country, provider, caseSensitive);
		generator.generate();
	}

}