package it.vige.cities.test.templates.it;

import static it.vige.cities.Countries.it;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.TUTTITALIA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.Result;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * 
 * @author lucastancapiano
 *
 *         Tuttitalia tests
 */
public class TuttitaliaTest extends FileGenerator {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(it.name());
		configuration.setProvider(TUTTITALIA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		Result result = generator.generateFile();
		assertTrue(result == OK);
		Nodes nodes = readFile(it.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node liguria = northWest.getZones().get(0);
		Node genova = liguria.getZones().get(0);
		Node arenzano = genova.getZones().get(0);
		assertEquals(northWest.getId(), 0);
		assertEquals(northWest.getLevel(), 0);
		assertEquals(northWest.getName(), "ITALIA NORD OCCIDENTALE");
		assertEquals(liguria.getId(), 1);
		assertEquals(liguria.getLevel(), 1);
		assertEquals(liguria.getName(), "LIGURIA");
		assertEquals(genova.getId(), 2);
		assertEquals(genova.getLevel(), 2);
		assertEquals(genova.getName(), "GENOVA");
		assertEquals(arenzano.getId(), 3);
		assertEquals(arenzano.getLevel(), 3);
		assertEquals(arenzano.getName(), "ARENZANO");
	}

}
