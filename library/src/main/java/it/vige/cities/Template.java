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
	 * Check if the template supports the given language
	 * 
	 * @param language the language to check
	 * @return true if the language is supported, false otherwise
	 */
	public boolean isLanguageSupported(Languages language) {
		// Default implementation: all languages are supported
		// Override in subclasses to restrict language support
		return true;
	}

	/**
	 * Check if the template supports the given country
	 * 
	 * @param country the country to check
	 * @return true if the country is supported, false otherwise
	 */
	public boolean isCountrySupported(String country) {
		// Default implementation: all countries are supported
		// Override in subclasses to restrict country support
		return true;
	}

	/**
	 * Validate that the current language is supported by this template
	 * 
	 * @throws NotSupportedException if the language is not supported
	 */
	protected void validateLanguage() throws NotSupportedException {
		Languages lang = this.language != null ? this.language : Languages.getDefault();
		if (!isLanguageSupported(lang)) {
			String templateName = this.getClass().getSimpleName();
			logger.error("Language '{}' ({}) is not supported by template '{}'", 
					lang != null ? lang.name() : "null", 
					lang != null ? lang.getCode() : "null", 
					templateName);
			throw new NotSupportedException(templateName, lang);
		}
	}

	/**
	 * Validate that the current country is supported by this template
	 * 
	 * @throws NotSupportedException if the country is not supported
	 */
	protected void validateCountry() throws NotSupportedException {
		String cntry = this.country != null ? this.country : "";
		if (!isCountrySupported(cntry)) {
			String templateName = this.getClass().getSimpleName();
			logger.error("Country '{}' is not supported by template '{}'", 
					cntry != null && !cntry.isEmpty() ? cntry : "null", 
					templateName);
			throw new NotSupportedException(templateName, cntry);
		}
	}

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
			// Validate country support before generating
			validateCountry();
			logger.debug("Country validation passed for template: {}", templateName);
			// Validate language support before generating
			validateLanguage();
			logger.debug("Language validation passed for template: {}", templateName);
			logger.debug("Calling generate() method");
			nodes = generate().getNodes();
			logger.info("Generation successful - nodes: {}", nodes != null && nodes.getZones() != null ? nodes.getZones().size() : 0);
			
			// Only write file if there are nodes (avoid empty files)
			if (nodes != null && nodes.getZones() != null && !nodes.getZones().isEmpty()) {
				writeFile(nodes, templateName);
				logger.info("File generation completed successfully for template: {}", templateName);
			} else {
				logger.warn("No nodes generated for template: {}, skipping file generation", templateName);
				return new ResultNodes(KO, nodes, this);
			}
		} catch (NotSupportedException ex) {
			logger.error("Template '{}' cannot be used: {} - No file will be generated", templateName, ex.getMessage());
			// Ensure no file is generated when feature is not supported
			return new ResultNodes(KO, nodes, this);
		} catch (Exception ex) {
			logger.error("File generation failed for template: {} - {}", templateName, ex.getMessage(), ex);
			return new ResultNodes(KO, nodes, this);
		}
		logger.debug("Returning OK result for template: {}", templateName);
		return new ResultNodes(OK, nodes, this);
	}
}
