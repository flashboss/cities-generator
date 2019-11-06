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

import it.vige.cities.templates.Wikipedia;

public class Generator {

	public final static String SINGLE_CASE_SENSITIVE = "s";
	public final static String SINGLE_PROVIDER = "p";
	public final static String MULTI_CASE_SENSITIVE = "case_sensitive";
	public final static String MULTI_PROVIDER = "provider";

	private Logger logger = LoggerFactory.getLogger(Generator.class);

	private CommandLine configureOptions(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption(Option.builder(SINGLE_CASE_SENSITIVE).longOpt(MULTI_CASE_SENSITIVE).type(Boolean.class)
				.desc(MULTI_CASE_SENSITIVE).build());
		options.addOption(Option.builder(SINGLE_PROVIDER).longOpt(MULTI_PROVIDER).required().type(String.class).hasArg()
				.numberOfArgs(1).desc(MULTI_PROVIDER).build());

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		String provider = cmd.getParsedOptionValue(SINGLE_PROVIDER) + "";
		try {
			Providers.valueOf(provider);
		} catch (IllegalArgumentException ex) {
			throw new ParseException(provider + " provided with the " + SINGLE_PROVIDER
					+ " argument is not in the list: " + Arrays.toString(Providers.values()));
		}
		return cmd;
	}

	public Template getTemplate(String provider) {
		Template template = null;
		switch (Providers.valueOf(provider)) {
		case WIKIPEDIA:
			template = new Wikipedia();
			break;
		case TUTTITALIA:
			template = new Wikipedia();
			break;
		case COMUNI_ITALIANI:
			template = new Wikipedia();
			break;
		}
		return template;
	}

	public void generate(String[] args) throws ParseException {
		logger.info("Start generation");
		CommandLine cmd = configureOptions(args);
		String provider = cmd.getParsedOptionValue(SINGLE_PROVIDER) + "";

		getTemplate(provider).generate(cmd);

	}

	public static void main(String[] args) throws ParseException {
		Generator generator = new Generator();
		generator.generate(args);
	}

}