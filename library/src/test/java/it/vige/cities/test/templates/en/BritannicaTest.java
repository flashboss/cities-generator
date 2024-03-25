package it.vige.cities.test.templates.en;

import static it.vige.cities.Countries.uk;
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
		configuration.setCountry(uk.name());
		configuration.setProvider(BRITANNICA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(uk.name());
		assertNotNull(nodes);
		Node england = nodes.getZones().get(0);
		Node northIreland = nodes.getZones().get(1);
		Node scotland = nodes.getZones().get(2);
		Node wales = nodes.getZones().get(3);
		Node duplicatedBedford = england.getZones().get(1).getZones().get(0);
		assertEquals(england.getId(), "1");
		assertEquals(england.getLevel(), 0);
		assertEquals(england.getName(), "ENGLAND");
		assertEquals(northIreland.getId(), "634");
		assertEquals(northIreland.getLevel(), 0);
		assertEquals(northIreland.getName(), "NORTHERN IRELAND");
		assertEquals(scotland.getId(), "1267");
		assertEquals(scotland.getLevel(), 0);
		assertEquals(scotland.getName(), "SCOTLAND");
		assertEquals(wales.getId(), "1900");
		assertEquals(wales.getLevel(), 0);
		assertEquals(wales.getName(), "WALES");
		assertEquals(duplicatedBedford.getId(), "1-3-4");
		assertEquals(duplicatedBedford.getLevel(), 2);
		assertEquals(duplicatedBedford.getName(), "BEDFORD (1)");
	}
}
