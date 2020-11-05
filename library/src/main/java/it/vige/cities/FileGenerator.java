package it.vige.cities;

import java.io.File;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.result.Nodes;

/**
 * 
 * @author lucastancapiano
 *
 *         Generator through file
 */
public class FileGenerator {

	/**
	 * 
	 */
	public final static String CITIES_HOME = System.getProperty("user.home") + "/cities-generator/";

	/**
	 * 
	 */
	protected String country = Locale.getDefault().getCountry().toLowerCase();

	private Logger logger = LoggerFactory.getLogger(FileGenerator.class);

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @param nodes the nodes to write
	 * @throws Exception if there is a problem
	 */
	protected void writeFile(Nodes nodes) throws Exception {
		new File(CITIES_HOME).mkdir();
		String name = CITIES_HOME + country + ".json";
		mapper.writeValue(new File(name), nodes);
		logger.info(mapper.writeValueAsString(nodes));
		logger.info("File generated in " + name);
	}

	/**
	 * 
	 * @return the read nodes
	 * @throws Exception if there is a problem
	 */
	protected Nodes readFile() throws Exception {
		readFile(country);
		new File(CITIES_HOME).mkdir();
		return mapper.readValue(new File(CITIES_HOME + country + ".json"), Nodes.class);
	}

	/**
	 * 
	 * @param country the country
	 * @return the nodes of the country
	 * @throws Exception if there is a problem
	 */
	protected Nodes readFile(String country) throws Exception {
		new File(CITIES_HOME).mkdir();
		return mapper.readValue(new File(CITIES_HOME + country + ".json"), Nodes.class);
	}

	/**
	 * 
	 * @return true if the file exists
	 */
	protected boolean exists() {
		return new File(CITIES_HOME).exists() && new File(CITIES_HOME + country + ".json").exists();
	}

}
