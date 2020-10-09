package it.vige.cities.templates;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import it.vige.cities.Normalizer;
import it.vige.cities.Template;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.result.geonames.Countrynodes;
import it.vige.cities.result.geonames.Geonode;
import it.vige.cities.result.geonames.Geonodes;

/**
 * 
 * @author lucastancapiano
 *
 *         The geonames generator
 */
public class GeoNames extends Template {

	private final static String URL_CHILDREN = "http://api.geonames.org/childrenJSON";
	private final static String URL_COUNTRY = "http://api.geonames.org/countryInfoJSON";
	private final static String DEFAULT_USERNAME = "vota";

	/**
	 * 
	 */
	protected boolean caseSensitive;
	private boolean duplicatedNames;
	private String username;
	
	/**
	 * 
	 */
	protected int firstLevel = 0;
	private Client client;

	/**
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
	 * 
	 * @param country the country
	 * @return the response
	 * @throws Exception if there is a problem
	 */
	protected Response getPageCountry(String country) throws Exception {
		client = ClientBuilder.newClient();
		WebTarget target = client.target(URL_COUNTRY);
		Response response = target.queryParam("country", country).queryParam("username", username).request().get();
		return response;
	}

	/**
	 * 
	 * @param id the id
	 * @return the response
	 * @throws Exception if there is a problem
	 */
	protected Response getPageChildren(int id) throws Exception {
		client = ClientBuilder.newClient();
		WebTarget target = client.target(URL_CHILDREN);
		Response response = target.queryParam("geonameId", id).queryParam("username", username).request().get();
		return response;
	}

	/**
	 * 
	 * @param zones       the zones
	 * @param numberLevel the number level
	 * @param id          the id
	 * @throws Exception if there is a problem
	 */
	private void addNodes(List<Node> zones, int numberLevel, int id) throws Exception {
		if (numberLevel <= MAX_LEVEL) {
			Response level = getPageChildren(id);
			List<Geonode> lines = level.readEntity(Geonodes.class).getGeonames();
			client.close();
			if (lines != null && !lines.isEmpty())
				for (Geonode head : lines) {
					Node node = new Node();
					node.setId(head.getGeonameId());
					node.setLevel(numberLevel);
					node.setName(Normalizer.execute(caseSensitive, duplicatedNames, head.getToponymName(),
							zones.parallelStream().map(e -> e.getName()).collect(Collectors.toList())));
					zones.add(node);
					addNodes(node.getZones(), numberLevel + 1, node.getId());
				}
		}
	}

	/**
	 * 
	 */
	@Override
	protected Nodes generate() throws Exception {
		Nodes nodes = new Nodes();
		Response response = getPageCountry(country);
		Countrynodes countries = response.readEntity(Countrynodes.class);
		addNodes(nodes.getZones(), firstLevel, countries.getGeonames().get(0).getGeonameId());
		return nodes;
	}

}
