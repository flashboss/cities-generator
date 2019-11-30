package it.vige.cities;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.result.Nodes;

public class FileGenerator {

	private final static String CITIES_HOME = System.getProperty("user.home") + "/cities-generator/";

	protected Countries country;

	private Logger logger = LoggerFactory.getLogger(FileGenerator.class);

	private ObjectMapper mapper = new ObjectMapper();

	protected void writeFile(Nodes nodes) throws Exception {
		new File(CITIES_HOME).mkdir();
		mapper.writeValue(new File(CITIES_HOME + country + ".json"), nodes);
		logger.info(mapper.writeValueAsString(nodes));
	}

	protected Nodes readFile() throws Exception {
		new File(CITIES_HOME).mkdir();
		return mapper.readValue(new File(CITIES_HOME + country + ".json"), Nodes.class);
	}

}
