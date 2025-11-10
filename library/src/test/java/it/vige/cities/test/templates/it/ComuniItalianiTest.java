package it.vige.cities.test.templates.it;

import static it.vige.cities.Countries.IT;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.COMUNI_ITALIANI;
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
 * Comuni italiani tests
 * @author lucastancapiano
 */
public class ComuniItalianiTest extends FileGenerator {

	/**
	 * Cities
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(IT.name());
		configuration.setProvider(COMUNI_ITALIANI.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(IT.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node liguria = northWest.getZones().get(0);
		Node genova = liguria.getZones().get(0);
		Node arenzano = genova.getZones().get(0);
		assertEquals("1", northWest.getId());
		assertEquals(0, northWest.getLevel());
		assertEquals("ITALIA NORD OCCIDENTALE", northWest.getName());
		assertEquals("1-2", liguria.getId());
		assertEquals(1, liguria.getLevel());
		assertEquals("LIGURIA", liguria.getName());
		assertEquals("1-2-3", genova.getId());
		assertEquals(2, genova.getLevel());
		assertEquals("GENOVA", genova.getName());
		assertEquals("1-2-3-4", arenzano.getId());
		assertEquals(3, arenzano.getLevel());
		assertEquals("ARENZANO", arenzano.getName());

	}

}
