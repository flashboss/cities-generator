package it.vige.cities.test.templates.en;

import static it.vige.cities.Countries.gb;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.en.Providers.GEONAMES;
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
 * Geonames tests
 * 
 * @author lucastancapiano
 */
public class GeoNamesTest extends FileGenerator {

	/**
	 * Cities
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(gb.name());
		configuration.setProvider(GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(gb.name());
		assertNotNull(nodes);
		Node england = nodes.getZones().get(0);
		Node bedford = england.getZones().get(1);
		Node bedfordCity = bedford.getZones().get(0);
		assertEquals("1", england.getId());
		assertEquals(0, england.getLevel());
		assertEquals("ENGLAND", england.getName());
		assertEquals("1-3", bedford.getId());
		assertEquals(1, bedford.getLevel());
		assertEquals("BEDFORD", bedford.getName());
		assertEquals("1-3-4", bedfordCity.getId());
		assertEquals(2, bedfordCity.getLevel());
		assertEquals("BEDFORD (CITY)", bedfordCity.getName());
	}

}
