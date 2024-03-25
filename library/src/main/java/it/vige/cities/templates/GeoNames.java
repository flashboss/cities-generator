package it.vige.cities.templates;

import static it.vige.cities.Normalizer.setName;
import static it.vige.cities.Result.OK;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static java.lang.Integer.parseInt;

import java.util.List;

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
		this.caseSensitive = caseSensitive;
		this.duplicatedNames = duplicatedNames;
		this.country = country;
		if (username != null)
			this.username = username;
		else
			this.username = DEFAULT_USERNAME;
	}

	/**
	 * Page country
	 * 
	 * @param country the country
	 * @return the response
	 * @throws Exception if there is a problem
	 */
	protected Response getPageCountry(String country) throws Exception {
		client = newClient();
		WebTarget target = client.target(URL_COUNTRY);
		Response response = target.queryParam("country", country).queryParam("username", username).request().get();
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
		client = newClient();
		WebTarget target = client.target(URL_CHILDREN);
		Response response = target.queryParam("geonameId", id).queryParam("username", username).request().get();
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
		if (numberLevel <= MAX_LEVEL) {
			String[] splittedIds = idStr.split("-");
			int id = parseInt(splittedIds[splittedIds.length - 1]);
			Response level = getPageChildren(id);
			List<Geonode> lines = level.readEntity(Geonodes.class).getGeonames();
			client.close();
			if (lines != null && !lines.isEmpty())
				for (Geonode head : lines) {
					String noFirstLevelId = "";
					if (numberLevel > 0)
						noFirstLevelId = idStr + ID_SEPARATOR;
					Node node = new Node();
					node.setId(noFirstLevelId + head.getGeonameId());
					node.setLevel(numberLevel);
					setName(caseSensitive, duplicatedNames, head.getToponymName(), zones, node);
					zones.add(node);
					addNodes(node.getZones(), numberLevel + 1, node.getId());
				}
		}
	}

	/**
	 * Generate
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Response response = getPageCountry(country);
		Countrynodes countries = response.readEntity(Countrynodes.class);
		addNodes(nodes.getZones(), firstLevel, countries.getGeonames().get(0).getGeonameId() + "");
		return new ResultNodes(OK, nodes, this);
	}

}
