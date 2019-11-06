package it.vige.cities.templates;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.Generator;
import it.vige.cities.Template;

public class ComuniItaliani implements Template {

	private Logger logger = LoggerFactory.getLogger(ComuniItaliani.class);

	@Override
	public void generate(CommandLine cmd) throws ParseException {
		boolean caseSensitive = cmd.hasOption(Generator.SINGLE_CASE_SENSITIVE);
		String provider = cmd.getParsedOptionValue(Generator.SINGLE_PROVIDER) + "";

		logger.info("case sensitive: " + caseSensitive + "");
		logger.info("provider: " + provider);

	}

}
