package it.vige.cities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public interface Template {

	void generate(CommandLine cmd) throws ParseException;
}
