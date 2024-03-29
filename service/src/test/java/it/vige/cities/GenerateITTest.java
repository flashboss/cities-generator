package it.vige.cities;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
@TestPropertySource(locations = "classpath:application-test.properties")
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

		nodes = citiesController.getResult("5-2523119-2523918-6540050,5-2523119-2523918-6537748", null);
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);
		assertEquals("5-2523119-2523918-6540050", firstNode.getId());

		nodes = citiesController.getResult("5-2523119-2523918-6540050,5-2523119-2523918", "all");
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertEquals("5", firstNode.getId());
		Node secondNode = firstNode.getZones().get(0);
		assertEquals("5-2523119", secondNode.getId());
		Node thirdNode = secondNode.getZones().get(0);
		assertEquals(1, thirdNode.getZones().size());
		assertEquals("5-2523119-2523918", thirdNode.getId());
		Node forthNode = thirdNode.getZones().get(0);
		assertEquals("5-2523119-2523918-6540050", forthNode.getId());

		nodes = citiesController.getResult("5-2523119-2523918-6540050,5-2523119-2523918-6541327", "all");
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertEquals("5", firstNode.getId());
		secondNode = firstNode.getZones().get(0);
		assertEquals("5-2523119", secondNode.getId());
		thirdNode = secondNode.getZones().get(0);
		assertEquals("5-2523119-2523918", thirdNode.getId());
		assertEquals(2, thirdNode.getZones().size());
		forthNode = thirdNode.getZones().get(0);
		assertEquals("5-2523119-2523918-6540050", forthNode.getId());
		Node fifthNode = thirdNode.getZones().get(1);
		assertEquals("5-2523119-2523918-6541327", fifthNode.getId());
	}

}
