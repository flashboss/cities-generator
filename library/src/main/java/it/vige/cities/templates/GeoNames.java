package it.vige.cities.templates;

import static it.vige.cities.Normalizer.setName;
import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static java.lang.Integer.parseInt;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.result.geonames.Countrynodes;
import it.vige.cities.result.geonames.Geonode;
import it.vige.cities.result.geonames.Geonodes;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * The geonames generator
 * 
 * @author lucastancapiano
 */
public class GeoNames extends Template {

	private static final Logger logger = getLogger(GeoNames.class);

	private final static String URL_CHILDREN = "http://api.geonames.org/childrenJSON";
	private final static String URL_COUNTRY = "http://api.geonames.org/countryInfoJSON";
	private final static String DEFAULT_USERNAME = "vota";

	/**
	 * Case sensitive
	 */
	protected boolean caseSensitive;
	private boolean duplicatedNames;
	private String username;
	/**
	 * Language for translations
	 */
	protected Languages language;

	/**
	 * First level
	 */
	protected int firstLevel = 0;
	private Client client;

	/**
	 * GeoNames
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames the duplicated names parameter
	 * @param username        the user name
	 */
	public GeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username) {
		this(country, caseSensitive, duplicatedNames, username, Languages.getDefault());
	}

	/**
	 * GeoNames
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames the duplicated names parameter
	 * @param username        the user name
	 * @param language        the language enum
	 */
	public GeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username, Languages language) {
		logger.debug("Creating GeoNames template - country: {}, caseSensitive: {}, duplicatedNames: {}, language: {}", 
				country, caseSensitive, duplicatedNames, language != null ? language.getCode() : Languages.getDefault().getCode());
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = country;
		this.language = language != null ? language : Languages.getDefault();
		if (username != null)
			this.username = username;
		else
			this.username = DEFAULT_USERNAME;
		logger.info("GeoNames template initialized - country: {}, username: {}, language: {}", 
				country, this.username, this.language != null ? this.language.getCode() : Languages.getDefault().getCode());
	}

	/**
	 * GeoNames (convenience method accepting String)
	 * 
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames the duplicated names parameter
	 * @param username        the user name
	 * @param language        the language code (e.g., "it", "en")
	 */
	public GeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username, String language) {
		this(country, caseSensitive, duplicatedNames, username, Languages.fromCode(language));
	}

	/**
	 * Page country
	 * 
	 * @param country the country
	 * @return the response
	 * @throws Exception if there is a problem
	 */
	protected Response getPageCountry(String country) throws Exception {
		logger.debug("Getting country page for: {} with language: {}", country, language != null ? language.getCode() : Languages.getDefault().getCode());
		client = newClient();
		WebTarget target = client.target(URL_COUNTRY);
		target = target.queryParam("country", country).queryParam("username", username);
		if (language != null) {
			target = target.queryParam("lang", language.getCode());
		}
		logger.debug("Requesting URL: {}", target.getUri());
		Response response = target.request().get();
		logger.debug("Response status: {}", response.getStatus());
		return response;
	}

	/**
	 * Page children
	 * 
	 * @param id the id
	 * @return the response
	 * @throws Exception if there is a problem
	 */
	protected Response getPageChildren(int id) throws Exception {
		logger.debug("Getting children for geonameId: {} with language: {}", id, language != null ? language.getCode() : Languages.getDefault().getCode());
		client = newClient();
		WebTarget target = client.target(URL_CHILDREN);
		target = target.queryParam("geonameId", id).queryParam("username", username);
		if (language != null) {
			target = target.queryParam("lang", language.getCode());
		}
		logger.debug("Requesting URL: {}", target.getUri());
		Response response = target.request().get();
		logger.debug("Response status: {}, geonameId: {}", response.getStatus(), id);
		return response;
	}

	/**
	 * Nodes
	 * 
	 * @param zones       the zones
	 * @param numberLevel the number level
	 * @param id          the id
	 * @throws Exception if there is a problem
	 */
	private void addNodes(List<Node> zones, int numberLevel, String idStr) throws Exception {
		logger.debug("Adding nodes - level: {}, id: {}", numberLevel, idStr);
		if (numberLevel <= MAX_LEVEL) {
			String[] splittedIds = idStr.split("-");
			int id = parseInt(splittedIds[splittedIds.length - 1]);
			Response level = getPageChildren(id);
			List<Geonode> lines = level.readEntity(Geonodes.class).getGeonames();
			client.close();
			logger.debug("Retrieved {} geonodes for level {} and id {}", 
					lines != null ? lines.size() : 0, numberLevel, id);
			if (lines != null && !lines.isEmpty()) {
				logger.debug("Processing {} nodes at level {}", lines.size(), numberLevel);
				for (Geonode head : lines) {
					String noFirstLevelId = "";
					if (numberLevel > 0)
						noFirstLevelId = idStr + ID_SEPARATOR;
					Node node = new Node();
					node.setId(noFirstLevelId + head.getGeonameId());
					node.setLevel(numberLevel);
					setName(caseSensitive, duplicatedNames, head.getName(), zones, node);
					zones.add(node);
					logger.debug("Added node: {} (level: {})", head.getName(), numberLevel);
					addNodes(node.getZones(), numberLevel + 1, node.getId());
				}
			} else {
				logger.debug("No nodes found for level {} and id {}", numberLevel, id);
			}
		} else {
			logger.debug("Max level ({}) reached, stopping recursion", MAX_LEVEL);
		}
	}

	/**
	 * Generate
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		logger.info("Starting GeoNames generation for country: {} with language: {}", country, language.getCode());
		Nodes nodes = new Nodes();
		Response response = getPageCountry(country);
		Countrynodes countries = response.readEntity(Countrynodes.class);
		if (countries != null && countries.getGeonames() != null && !countries.getGeonames().isEmpty()) {
			int rootGeonameId = countries.getGeonames().get(0).getGeonameId();
			logger.info("Found root geonameId: {} for country: {}", rootGeonameId, country);
			addNodes(nodes.getZones(), firstLevel, rootGeonameId + "");
			logger.info("GeoNames generation completed - total zones: {}", nodes.getZones() != null ? nodes.getZones().size() : 0);
		} else {
			logger.warn("No countries found for country code: {}", country);
		}
		return new ResultNodes(OK, nodes, this);
	}

}
