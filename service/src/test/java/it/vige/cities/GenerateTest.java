package it.vige.cities;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import it.vige.cities.rest.CitiesController;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class GenerateTest {

	private Logger logger = LoggerFactory.getLogger(GenerateTest.class);

	@Autowired
	private CitiesController citiesController;

	@Test
	public void generateOk() throws Exception {
		logger.info("start generation test");
		Nodes nodes = citiesController.getResult();
		assertNotNull(nodes);
		Node firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);

		int id = firstNode.getId();
		nodes = citiesController.getResult(id);
		assertNotNull(nodes);
		firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);
	}

}
