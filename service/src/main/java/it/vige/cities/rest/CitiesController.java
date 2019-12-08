package it.vige.cities.rest;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.vige.cities.Countries;
import it.vige.cities.Generator;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@RestController
@CrossOrigin(origins = "*")
public class CitiesController {

	public final static Nodes nodes = new Nodes();

	@Value("${country}")
	private Countries country;

	@PostConstruct
	public void init() {
		Generator generator = new Generator(country.name(), false, false, true);
		if (!generator.isGenerated())
			nodes.setZones(generator.generate().getZones());
		else {
			generator.setOverwrite(false);
			nodes.setZones(generator.generate().getZones());
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
	public void update(@RequestBody Configuration configuration) {
		Generator generator = new Generator(configuration.getCountry().name(), configuration.getProvider(),
				configuration.isCaseSensitive(), configuration.isDuplicatedNames(), true);
		nodes.setZones(generator.generate().getZones());
	}

}
