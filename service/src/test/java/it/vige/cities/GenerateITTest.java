package it.vige.cities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.vige.cities.rest.CitiesController;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

@SpringBootTest
@ActiveProfiles("test")
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

		String id = firstNode.getId();
		nodes = citiesController.getResult(id, null);
		assertNotNull(nodes);
		firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);

		nodes = citiesController.getResult("3069-3931-4393-4503,3069-3931-4393-4394", null);
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);
		assertEquals("3069-3931-4393-4503", firstNode.getId());

		nodes = citiesController.getResult("3069-3931-4393-4503,3069-3931-4393", "all");
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertEquals("3069", firstNode.getId());
		Node secondNode = firstNode.getZones().get(0);
		assertEquals("3069-3931", secondNode.getId());
		Node thirdNode = secondNode.getZones().get(0);
		assertEquals(1, thirdNode.getZones().size());
		assertEquals("3069-3931-4393", thirdNode.getId());
		Node forthNode = thirdNode.getZones().get(0);
		assertEquals("3069-3931-4393-4503", forthNode.getId());

		nodes = citiesController.getResult("3069-3931-4393-4503,3069-3931-4393-4397", "all");
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertEquals("3069", firstNode.getId());
		secondNode = firstNode.getZones().get(0);
		assertEquals("3069-3931", secondNode.getId());
		thirdNode = secondNode.getZones().get(0);
		assertEquals("3069-3931-4393", thirdNode.getId());
		assertEquals(2, thirdNode.getZones().size());
		forthNode = thirdNode.getZones().get(0);
		assertEquals("3069-3931-4393-4503", forthNode.getId());
		Node fifthNode = thirdNode.getZones().get(1);
		assertEquals("3069-3931-4393-4397", fifthNode.getId());
	}

}
