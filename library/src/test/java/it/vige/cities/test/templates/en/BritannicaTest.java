package it.vige.cities.test.templates.en;

import static it.vige.cities.Countries.gb;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.en.Providers.BRITANNICA;
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
 * Britannica tests
 * 
 * @author lucastancapiano
 */
public class BritannicaTest extends FileGenerator {

	/**
	 * Cities
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(gb.name());
		configuration.setProvider(BRITANNICA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(gb.name());
		assertNotNull(nodes);
		Node england = nodes.getZones().get(0);
		Node northIreland = nodes.getZones().get(1);
		Node scotland = nodes.getZones().get(2);
		Node wales = nodes.getZones().get(3);
		Node duplicatedBedford = england.getZones().get(1).getZones().get(0);
		assertEquals("1", england.getId());
		assertEquals(0, england.getLevel());
		assertEquals("ENGLAND", england.getName());
		assertEquals("634", northIreland.getId());
		assertEquals(0, northIreland.getLevel());
		assertEquals("NORTHERN IRELAND", northIreland.getName());
		assertEquals("676", scotland.getId());
		assertEquals(0, scotland.getLevel());
		assertEquals("SCOTLAND", scotland.getName());
		assertEquals("801", wales.getId());
		assertEquals(0, wales.getLevel());
		assertEquals("WALES", wales.getName());
		assertEquals("1-3-4", duplicatedBedford.getId());
		assertEquals(2, duplicatedBedford.getLevel());
		assertEquals("BEDFORD (CITY)", duplicatedBedford.getName());
	}
}
