package it.vige.cities;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import it.vige.cities.rest.CitiesController;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class GenerateITTest {

	private Logger logger = getLogger(GenerateITTest.class);

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
