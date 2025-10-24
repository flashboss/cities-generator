package it.vige.cities.test.templates.it;

import static it.vige.cities.Countries.it;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.EXTRA_GEONAMES;
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
 * Extra geonames tests
 * 
 * @author lucastancapiano
 */
public class ExtraGeoNamesTest extends FileGenerator {

	/**
	 * Cities
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(it.name());
		configuration.setProvider(EXTRA_GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(it.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node regioneAutonomaValleDaosta = northWest.getZones().get(0);
		Node valleDaosta = regioneAutonomaValleDaosta.getZones().get(0);
		Node allein = valleDaosta.getZones().get(0);
		assertEquals("1", northWest.getId());
		assertEquals(0, northWest.getLevel());
		assertEquals("I: ITALIA NORD-OCCIDENTALE", northWest.getName());
		assertEquals("1-3164857", regioneAutonomaValleDaosta.getId());
		assertEquals(1, regioneAutonomaValleDaosta.getLevel());
		assertEquals("REGIONE AUTONOMA VALLE D'AOSTA", regioneAutonomaValleDaosta.getName());
		assertEquals("1-3164857-3182996", valleDaosta.getId());
		assertEquals(2, valleDaosta.getLevel());
		assertEquals("VALLE D'AOSTA", valleDaosta.getName());
		assertEquals("1-3164857-3182996-6543055", allein.getId());
		assertEquals(3, allein.getLevel());
		assertEquals("ALLEIN", allein.getName());
	}

}
