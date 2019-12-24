package it.vige.cities.rest;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.vige.cities.Configuration;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@RestController
@CrossOrigin(origins = "*")
public class CitiesController {

	private Logger logger = LoggerFactory.getLogger(FileGenerator.class);

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

	@PostConstruct
	public void init() {
		try {
			Configuration configuration = new Configuration();
			configuration.setCountry(country);
			configuration.setCaseSensitive(caseSensitive);
			configuration.setDuplicatedNames(duplicatedNames);
			configuration.setProvider(provider);
			configuration.setUsername(username);
			Generator generator = new Generator(configuration, false);
			nodes.setZones(generator.generate().getZones());
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
		}
	}

	private Node find(Node node, int id) {
		if (node.getId() == id)
			return node;
		List<Node> children = node.getZones();
		Node res = null;
		for (int i = 0; res == null && i < children.size(); i++) {
			res = find(children.get(i), id);
		}
		return res;
	}

	@GetMapping(value = "/cities")
	public Nodes getResult() {
		return nodes;
	}

	@GetMapping(value = "/cities/{id}")
	public Nodes getResult(@PathVariable("id") int id) {
		Node found = null;
		for (Node node : nodes.getZones()) {
			found = find(node, id);
			if (found != null)
				break;
		}
		Nodes nodes = new Nodes();
		nodes.getZones().add(found);
		return nodes;
	}

	@PostMapping(value = "/update")
	public void update(@RequestBody Configuration configuration) throws Exception {
		Generator generator = new Generator(configuration, true);
		nodes.setZones(generator.generate().getZones());
	}

}
