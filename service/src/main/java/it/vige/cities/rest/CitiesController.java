package it.vige.cities.rest;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import it.vige.cities.Configuration;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * REST controller for cities data API
 * Provides endpoints to retrieve and update cities data
 * 
 * @author lucastancapiano
 */
@RestController
@CrossOrigin(origins = "*")
public class CitiesController {

	/**
	 * Logger for controller operations
	 */
	private Logger logger = getLogger(FileGenerator.class);

	/**
	 * Shared nodes cache for all requests
	 */
	public final static Nodes nodes = new Nodes();
	
	/**
	 * Track last used country for regeneration logic
	 */
	private String lastUsedCountry = null;
	
	/**
	 * Track last used language for regeneration logic
	 */
	private String lastUsedLanguage = null;

	/**
	 * Default country from configuration
	 */
	@Value("${country}")
	private String country;

	/**
	 * Provider from configuration (optional)
	 */
	@Value("${provider:#{null}}")
	private String provider;

	/**
	 * Case sensitive flag from configuration
	 */
	@Value("${caseSensitive:false}")
	private boolean caseSensitive;

	/**
	 * Duplicated names flag from configuration
	 */
	@Value("${duplicatedNames:false}")
	private boolean duplicatedNames;

	/**
	 * Username from configuration (optional)
	 */
	@Value("${username:#{null}}")
	private String username;

	/**
	 * Language from configuration (default: "it")
	 */
	@Value("${language:it}")
	private String language;

	/**
	 * Initialize nodes with default country and language
	 * Uses default values: IT and it
	 */
	public void init() {
		init(null, null);
	}

	/**
	 * Initialize nodes with specified country and language
	 * Regenerates data only if nodes are empty or country/language changed
	 * 
	 * @param countryParam the country code (ISO 3166-1 alpha-2), null to use default
	 * @param languageParam the language code (e.g., "it", "en"), null to use default
	 */
	private void init(String countryParam, String languageParam) {
		// Use parameters if provided, otherwise use default values: IT and it
		String effectiveCountry = countryParam != null ? countryParam : "IT";
		String effectiveLanguage = languageParam != null ? languageParam : "it";
		
		// Check if we need to regenerate (empty nodes or different country/language)
		boolean needsRegeneration = nodes.getZones().isEmpty() ||
			!effectiveCountry.equals(lastUsedCountry) ||
			!effectiveLanguage.equals(lastUsedLanguage);
		
		if (needsRegeneration) {
			try {
				Configuration configuration = new Configuration();
				configuration.setCountry(effectiveCountry);
				configuration.setCaseSensitive(caseSensitive);
				configuration.setDuplicatedNames(duplicatedNames);
				configuration.setProvider(provider);
				configuration.setUsername(username);
				configuration.setLanguage(effectiveLanguage);
				Generator generator = new Generator(configuration, false);
				nodes.setZones(generator.generate().getNodes().getZones());
				// Update last used values
				lastUsedCountry = effectiveCountry;
				lastUsedLanguage = effectiveLanguage;
			} catch (Exception ex) {
				logger.warn(ex.getMessage());
			}
		}
	}

	/**
	 * Recursively find a node by ID in the node tree
	 * 
	 * @param node the root node to search from
	 * @param id the node ID to find
	 * @return the found node, or null if not found
	 */
	private Node find(Node node, String id) {
		if (node.getId().equals(id))
			return node;
		List<Node> children = node.getZones();
		Node res = null;
		for (int i = 0; res == null && i < children.size(); i++) {
			res = find(children.get(i), id);
		}
		return res;
	}

	/**
	 * Filter nodes to include only the path to a specific node ID
	 * Removes all nodes that are not in the path to the target node
	 * 
	 * @param nodes the list of nodes to filter (modified in place)
	 * @param node the current node being processed
	 * @param id the target node ID
	 * @return the target node if found, null otherwise
	 */
	private Node filter(List<Node> nodes, Node node, String id) {
		if (node.getId().equals(id)) {
			return node;
		}
		List<Node> children = node.getZones();
		for (Node child : children) {
			nodes.add(child);
			Node filteredNode = filter(nodes, child, id);
			if (filteredNode != null)
				return node;
			else
				nodes.remove(child);
		}
		return null;
	}

	/**
	 * Parse comma-separated IDs string into a list
	 * 
	 * @param ids comma-separated string of IDs (e.g., "1,2,3")
	 * @return list of ID strings
	 */
	private List<String> getIds(String ids) {
		List<String> result = new ArrayList<String>();
		String[] splittedIds = ids.split(",");
		for (String splittedId : splittedIds)
			result.add(splittedId);
		return result;
	}

	/**
	 * Merge a found node into the list of all found nodes
	 * If a node with the same ID already exists, merges their children
	 * Otherwise, adds the node to the list
	 * 
	 * @param allFound the list of all found nodes (modified in place)
	 * @param found the node to merge
	 */
	private void merge(List<Node> allFound, Node found) {
		if (found != null)
			if (allFound.isEmpty())
				allFound.add(found);
			else
				for (Node node : allFound)
					if (node.getId().equals(found.getId()))
						for (Node found1 : found.getZones())
							merge(node.getZones(), found1);
					else {
						allFound.add(found);
						break;
					}
	}

	/**
	 * Get all cities data
	 * Returns the complete cities hierarchy for the specified country and language
	 * 
	 * @param country the country code (ISO 3166-1 alpha-2), optional (uses default if not provided)
	 * @param language the language code (e.g., "it", "en", "fr"), optional (uses default if not provided)
	 * @return the complete nodes hierarchy
	 */
	@GetMapping(value = "/cities")
	public Nodes getResult(
			@Parameter(description = "Country code (e.g., 'IT', 'GB', 'FR')", example = "IT", schema = @Schema(type = "string"))
			@RequestParam(required = false) String country,
			@Parameter(description = "Language code (e.g., 'it', 'en', 'fr', 'de', 'es', 'pt')", example = "it", schema = @Schema(type = "string"))
			@RequestParam(required = false) String language) {
		init(country, language);
		return nodes;
	}

	/**
	 * Get cities data filtered by IDs
	 * Returns nodes matching the specified IDs, optionally including all hierarchy levels
	 * 
	 * @param ids comma-separated list of node IDs to retrieve
	 * @param all if "all", includes all hierarchy levels for each ID; otherwise returns only the first ID
	 * @param country the country code (ISO 3166-1 alpha-2), optional (uses default if not provided)
	 * @param language the language code (e.g., "it", "en", "fr"), optional (uses default if not provided)
	 * @return nodes matching the specified IDs
	 */
	@GetMapping(value = "/cities/{ids}")
	public Nodes getResult(
			@PathVariable("ids") String ids,
			@Parameter(description = "Include all hierarchy levels", example = "all", schema = @Schema(type = "string"))
			@RequestParam(required = false) String all,
			@Parameter(description = "Country code (e.g., 'IT', 'GB', 'FR')", example = "IT", schema = @Schema(type = "string"))
			@RequestParam(required = false) String country,
			@Parameter(description = "Language code (e.g., 'it', 'en', 'fr', 'de', 'es', 'pt')", example = "it", schema = @Schema(type = "string"))
			@RequestParam(required = false) String language) {
		init(country, language);
		List<String> iIds = getIds(ids);
		List<Node> allFound = new ArrayList<Node>();
		for (int j = 0; j < iIds.size(); j++) {
			String id = iIds.get(j);
			if (all == null && j > 0)
				break;
			Node found = null;
			for (Node originalNode : nodes.getZones()) {
				if (id.startsWith(originalNode.getId())) {
					Node node = (Node) originalNode.clone();
					if (all == null) {
						found = find(node, id);
					} else {
						List<Node> newNodes = new ArrayList<Node>();
						newNodes.add(node);
						filter(newNodes, node, id);
						for (int i = 0; i < newNodes.size() - 1; i++) {
							newNodes.get(i).getZones().clear();
							newNodes.get(i).getZones().add(newNodes.get(i + 1));
						}
						newNodes.get(newNodes.size() - 1).getZones().clear();
						found = newNodes.get(0);
					}
					merge(allFound, found);
					if (found != null)
						break;
				}
			}
		}
		Nodes nodes = new Nodes();
		nodes.getZones().addAll(allFound);
		return nodes;
	}

	/**
	 * Update cities data by regenerating with new configuration
	 * Requires "admin" role. Regenerates cities data using the provided configuration
	 * 
	 * @param configuration the configuration to use for generation
	 * @throws Exception if there is a problem generating the data
	 */
	@PostMapping(value = "/update")
	public void update(@RequestBody Configuration configuration) throws Exception {
		Generator generator = new Generator(configuration, true);
		Nodes resultNodes = generator.generate().getNodes();
		if (resultNodes != null)
			nodes.setZones(resultNodes.getZones());
	}

}
