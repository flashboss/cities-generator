package it.vige.cities;

import java.util.Arrays;

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

public class Generator {

	public final static String SINGLE_CASE_SENSITIVE = "s";
	public final static String SINGLE_COUNTRY = "c";
	public final static String SINGLE_PROVIDER = "p";
	public final static String MULTI_CASE_SENSITIVE = "case_sensitive";
	public final static String MULTI_COUNTRY = "country";
	public final static String MULTI_PROVIDER = "provider";

	private Logger logger = LoggerFactory.getLogger(Generator.class);

	private CommandLine configureOptions(String[] args) throws ParseException {
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

	public Template getTemplate(String country, String provider) {
		Template template = null;
		switch (Countries.valueOf(country)) {
		case IT:
			switch (it.vige.cities.templates.it.Providers.valueOf(provider)) {
				case TUTTITALIA:
					template = new Tuttitalia();
					break;
				case COMUNI_ITALIANI:
					template = new ComuniItaliani();
					break;
				default:
					template = new Tuttitalia();
					break;
			}
			break;
		case EN:
			switch (it.vige.cities.templates.en.Providers.valueOf(provider)) {
			case CITYMETRIC:
				template = new CityMetric();
				break;
			case CITYPOPULATION:
				template = new CityPopulation();
				break;
			default:
				template = new CityMetric();
				break;
		}
		break;
		}
		return template;
	}

	public void generate(String[] args) throws Exception {
		logger.info("Start generation");
		CommandLine cmd = configureOptions(args);
		String country = cmd.getParsedOptionValue(SINGLE_COUNTRY) + "";
		String provider = cmd.getParsedOptionValue(SINGLE_PROVIDER) + "";

		getTemplate(country, provider).generate(cmd);

	}

	public static void main(String[] args) throws Exception {
		Generator generator = new Generator();
		generator.generate(args);
	}

}