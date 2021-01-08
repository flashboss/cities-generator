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
import it.vige.cities.Result;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;

/**
 * 
 * @author lucastancapiano
 *
 *         Extra geonames tests
 */
public class ExtraGeoNamesTest extends FileGenerator {

	/**
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
		Result result = generator.generateFile();
		assertTrue(result == OK);
		Nodes nodes = readFile(it.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node regioneAutonomaValleDaosta = northWest.getZones().get(0);
		Node valleDaosta = regioneAutonomaValleDaosta.getZones().get(0);
		Node allein = valleDaosta.getZones().get(0);
		assertEquals(northWest.getId(), 0);
		assertEquals(northWest.getLevel(), 0);
		assertEquals(northWest.getName(), "I: ITALIA NORD-OCCIDENTALE");
		assertEquals(regioneAutonomaValleDaosta.getId(), 3164857);
		assertEquals(regioneAutonomaValleDaosta.getLevel(), 1);
		assertEquals(regioneAutonomaValleDaosta.getName(), "REGIONE AUTONOMA VALLE D'AOSTA");
		assertEquals(valleDaosta.getId(), 3182996);
		assertEquals(valleDaosta.getLevel(), 2);
		assertEquals(valleDaosta.getName(), "VALLE D'AOSTA");
		assertEquals(allein.getId(), 6543055);
		assertEquals(allein.getLevel(), 3);
		assertEquals(allein.getName(), "ALLEIN");
	}

}
