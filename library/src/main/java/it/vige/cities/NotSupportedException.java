package it.vige.cities;

/**
 * Exception thrown when a template does not support a specific language or country
 * 
 * @author lucastancapiano
 */
public class NotSupportedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Language not supported exception
	 * 
	 * @param templateName the name of the template
	 * @param language     the unsupported language
	 */
	public NotSupportedException(String templateName, Languages language) {
		super(String.format("Template '%s' does not support language '%s' (%s)", 
				templateName, language != null ? language.name() : "null", 
				language != null ? language.getCode() : "null"));
	}

	/**
	 * Country not supported exception
	 * 
	 * @param templateName the name of the template
	 * @param country      the unsupported country
	 */
	public NotSupportedException(String templateName, String country) {
		super(String.format("Template '%s' does not support country '%s'", 
				templateName, country != null ? country : "null"));
	}

	/**
	 * Language and country not supported exception
	 * 
	 * @param templateName the name of the template
	 * @param language     the unsupported language
	 * @param country      the unsupported country
	 */
	public NotSupportedException(String templateName, Languages language, String country) {
		super(String.format("Template '%s' does not support language '%s' (%s) and/or country '%s'", 
				templateName, language != null ? language.name() : "null", 
				language != null ? language.getCode() : "null",
				country != null ? country : "null"));
	}

	/**
	 * Not supported exception with custom message
	 * 
	 * @param message the error message
	 */
	public NotSupportedException(String message) {
		super(message);
	}
}

