package it.vige.cities;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import it.vige.cities.rest.CitiesController;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class GenerateTest {

	private Logger logger = LoggerFactory.getLogger(GenerateTest.class);

	@Autowired
	private CitiesController citiesController;

	@Test
	public void generateOk() throws Exception {
		logger.info("start generation test");
		Nodes nodes = citiesController.getResult();
		Assertions.assertNotNull(nodes);
		Node firstNode = nodes.getZones().get(0);
		Assertions.assertNotNull(firstNode);

		int id = firstNode.getId();
		nodes = citiesController.getResult(id);
		Assertions.assertNotNull(nodes);
		firstNode = nodes.getZones().get(0);
		Assertions.assertNotNull(firstNode);
	}

}
