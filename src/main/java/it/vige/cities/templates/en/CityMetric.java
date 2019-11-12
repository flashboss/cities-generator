package it.vige.cities.templates.en;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.vige.cities.Generator;
import it.vige.cities.Result;
import it.vige.cities.Template;

public class CityMetric extends Template {

	private Logger logger = LoggerFactory.getLogger(Generator.class);

	private boolean caseSensitive;

	public CityMetric(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public Result generate() {
		logger.debug(caseSensitive + "");
		return Result.KO;
	}
}
