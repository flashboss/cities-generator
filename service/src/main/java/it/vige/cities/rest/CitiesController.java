package it.vige.cities.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vige.cities.result.Nodes;

@RestController
@CrossOrigin(origins = "*")
public class CitiesController {

	@GetMapping(value = "/cities")
	public Nodes getResult() {
		Nodes nodes = new Nodes();
		return nodes;
	}

}
