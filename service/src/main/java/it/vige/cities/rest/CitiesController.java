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

import it.vige.cities.Configuration;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@RestController
@CrossOrigin(origins = "*")
public class CitiesController {

	private Logger logger = getLogger(FileGenerator.class);

	public final static Nodes nodes = new Nodes();

	@Value("${country}")
	private String country;

	@Value("${provider:#{null}}")
	private String provider;

	@Value("${caseSensitive:false}")
	private boolean caseSensitive;

	@Value("${duplicatedNames:false}")
	private boolean duplicatedNames;

	@Value("${username:#{null}}")
	private String username;

	@Value("${language:it}")
	private String language;

	public void init() {
		if (nodes.getZones().isEmpty())
			try {
				Configuration configuration = new Configuration();
				configuration.setCountry(country);
				configuration.setCaseSensitive(caseSensitive);
				configuration.setDuplicatedNames(duplicatedNames);
				configuration.setProvider(provider);
				configuration.setUsername(username);
				configuration.setLanguage(language);
				Generator generator = new Generator(configuration, false);
				nodes.setZones(generator.generate().getNodes().getZones());
			} catch (Exception ex) {
				logger.warn(ex.getMessage());
			}
	}

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

	private List<String> getIds(String ids) {
		List<String> result = new ArrayList<String>();
		String[] splittedIds = ids.split(",");
		for (String splittedId : splittedIds)
			result.add(splittedId);
		return result;
	}

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

	@GetMapping(value = "/cities")
	public Nodes getResult() {
		init();
		return nodes;
	}

	@GetMapping(value = "/cities/{ids}")
	public Nodes getResult(@PathVariable("ids") String ids, @RequestParam(required = false) String all) {
		init();
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

	@PostMapping(value = "/update")
	public void update(@RequestBody Configuration configuration) throws Exception {
		Generator generator = new Generator(configuration, true);
		nodes.setZones(generator.generate().getNodes().getZones());
	}

}
