package it.vige.cities.rest;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.vige.cities.Generator;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@RestController
@CrossOrigin(origins = "*")
public class CitiesController {

	private Nodes nodes;

	@PostConstruct
	public void init() {
		Generator generator = new Generator(false, false);
		nodes = generator.generate();
	}

	private Node find(Node node, int id) {
		if (node.getId() == id)
			return node;
		else {
			Node found = null;
			for (Node nodeToScan : node.getZones()) {
				return find(nodeToScan, id);
			}
			return found;
		}
	}

	@GetMapping(value = "/cities")
	public Nodes getResult() {
		return nodes;
	}

	@GetMapping(value = "/cities/{id}")
	public Nodes getResult(@PathVariable("id") int id) {
		Node found = nodes.getZones().parallelStream().filter(e -> find(e, id) != null).findFirst().get();
		Nodes nodes = new Nodes();
		nodes.getZones().add(found);
		return nodes;
	}

	@GetMapping(value = "/update")
	public void update() {
		init();
	}

}
