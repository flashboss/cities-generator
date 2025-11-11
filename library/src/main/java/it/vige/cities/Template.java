package it.vige.cities;

import static it.vige.cities.Result.KO;
import static it.vige.cities.Result.OK;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import it.vige.cities.result.Nodes;

/**
 * A template for the file generator
 * 
 * @author lucastancapiano
 */
public abstract class Template extends FileGenerator {

	/**
	 * Logger for template operations
	 */
	protected static final Logger logger = getLogger(Template.class);

	/**
	 * Max level
	 */
	protected final static int MAX_LEVEL = 3;

	/**
	 * Generate
	 * 
	 * @return the generated nodes
	 * @throws Exception if there is a problem
	 */
	protected abstract ResultNodes generate() throws Exception;

	/**
	 * default template
	 */
	public Template() {

	}

	/**
	 * Generate file
	 * 
	 * @return the result of the generation
	 */
	protected ResultNodes generateFile() {
		String templateName = this.getClass().getSimpleName();
		logger.info("Starting file generation with template: {}", templateName);
		logger.debug("Template: {}, country: {}, language: {}", templateName, country, language != null ? language.getCode() : Languages.getDefault().getCode());
		Nodes nodes = null;
		try {
			logger.debug("Calling generate() method");
			nodes = generate().getNodes();
			logger.info("Generation successful - nodes: {}", nodes != null && nodes.getZones() != null ? nodes.getZones().size() : 0);
			writeFile(nodes, templateName);
			logger.info("File generation completed successfully for template: {}", templateName);
		} catch (Exception ex) {
			logger.error("File generation failed for template: {} - {}", templateName, ex.getMessage(), ex);
			return new ResultNodes(KO, nodes, this);
		}
		logger.debug("Returning OK result for template: {}", templateName);
		return new ResultNodes(OK, nodes, this);
	}
}
