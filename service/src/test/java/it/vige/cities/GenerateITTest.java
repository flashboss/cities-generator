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
		// Test with default values (IT, it)
		Nodes nodes = citiesController.getResult(null, null);
		assertNotNull(nodes);
		Node firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);

		String id = firstNode.getId();
		nodes = citiesController.getResult(id, null, null, null);
		assertNotNull(nodes);
		firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);

		nodes = citiesController.getResult("3-4252-4287-4346,3-4252-4287-6543031", null, null, null);
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode);
		assertEquals("3-4252-4287-4346", firstNode.getId());

		nodes = citiesController.getResult("3-4252-4287-4346,3-4252-4287", "all", null, null);
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertEquals("3", firstNode.getId());
		Node secondNode = firstNode.getZones().get(0);
		assertEquals("3-4252", secondNode.getId());
		Node thirdNode = secondNode.getZones().get(0);
		assertEquals(1, thirdNode.getZones().size());
		assertEquals("3-4252-4287", thirdNode.getId());
		Node forthNode = thirdNode.getZones().get(0);
		assertEquals("3-4252-4287-4346", forthNode.getId());

		nodes = citiesController.getResult("3-4252-4287-4346,3-4252-4287-4293", "all", null, null);
		assertNotNull(nodes);
		assertEquals(1, nodes.getZones().size());
		firstNode = nodes.getZones().get(0);
		assertEquals("3", firstNode.getId());
		secondNode = firstNode.getZones().get(0);
		assertEquals("3-4252", secondNode.getId());
		thirdNode = secondNode.getZones().get(0);
		assertEquals("3-4252-4287", thirdNode.getId());
		assertEquals(2, thirdNode.getZones().size());
		forthNode = thirdNode.getZones().get(0);
		assertEquals("3-4252-4287-4346", forthNode.getId());
		Node fifthNode = thirdNode.getZones().get(1);
		assertEquals("3-4252-4287-4293", fifthNode.getId());
	}

	@Test
	public void generateWithCountryAndLanguage() throws Exception {
		logger.info("start generation test with country and language parameters");
		// Test with explicit country and language parameters
		Nodes nodes = citiesController.getResult("IT", "it");
		assertNotNull(nodes);
		assertNotNull(nodes.getZones());
		
		// Test with different country
		nodes = citiesController.getResult("GB", "en");
		assertNotNull(nodes);
		
		// Test with path parameter and query parameters
		if (!nodes.getZones().isEmpty()) {
			String id = nodes.getZones().get(0).getId();
			nodes = citiesController.getResult(id, null, "GB", "en");
			assertNotNull(nodes);
		}
	}

}
