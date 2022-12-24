package it.vige.cities;

import static java.lang.System.getProperty;
import static java.util.Locale.getDefault;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.result.Nodes;

/**
 * Generator through file
 * @author lucastancapiano
 */
public class FileGenerator {

	/**
	 * Folder home
	 */
	public final static String CITIES_HOME = getProperty("user.home") + "/cities-generator/";

	/**
	 * Country
	 */
	protected String country = getDefault().getCountry().toLowerCase();

	private Logger logger = getLogger(FileGenerator.class);

	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * default fileGenerator
	 */
	public FileGenerator() {
		
	}

	/**
	 * Write file
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
	 * Read file
	 * @return the read nodes
	 * @throws Exception if there is a problem
	 */
	protected Nodes readFile() throws Exception {
		readFile(country);
		new File(CITIES_HOME).mkdir();
		return mapper.readValue(new File(CITIES_HOME + country + ".json"), Nodes.class);
	}

	/**
	 * Read file
	 * @param country the country
	 * @return the nodes of the country
	 * @throws Exception if there is a problem
	 */
	protected Nodes readFile(String country) throws Exception {
		new File(CITIES_HOME).mkdir();
		return mapper.readValue(new File(CITIES_HOME + country + ".json"), Nodes.class);
	}

	/**
	 * Exists
	 * @return true if the file exists
	 */
	protected boolean exists() {
		return new File(CITIES_HOME).exists() && new File(CITIES_HOME + country + ".json").exists();
	}

}
