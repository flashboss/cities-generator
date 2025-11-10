package it.vige.cities.test.templates.it;

import static it.vige.cities.Countries.it;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.WIKIPEDIA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * Wikipedia tests
 * 
 * @author lucastancapiano
 */
public class WikipediaTest extends FileGenerator {

	/**
	 * Cities
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(it.name());
		configuration.setProvider(WIKIPEDIA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(it.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node liguria = northWest.getZones().get(0);
		Node genova = liguria.getZones().get(3);
		Node arenzano = genova.getZones().get(0);
		assertEquals("1", northWest.getId());
		assertEquals(0, northWest.getLevel());
		assertEquals("I: ITALIA NORD-OCCIDENTALE", northWest.getName());
		assertEquals("1-6", liguria.getId());
		assertEquals(1, liguria.getLevel());
		assertEquals("LIGURIA", liguria.getName());
		assertEquals("1-6-177", genova.getId());
		assertEquals(2, genova.getLevel());
		assertEquals("GENOVA", genova.getName());
		assertEquals("1-6-177-178", arenzano.getId());
		assertEquals(3, arenzano.getLevel());
		assertEquals("ARENZANO", arenzano.getName());
	}

}
