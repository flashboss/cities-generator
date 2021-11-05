package it.vige.cities.test.templates.en;

import static it.vige.cities.Countries.uk;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.en.Providers.GEONAMES;
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
 *         Geonames tests
 */
public class GeoNamesTest extends FileGenerator {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(uk.name());
		configuration.setProvider(GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		Result result = generator.generateFile();
		assertTrue(result == OK);
		Nodes nodes = readFile(uk.name());
		assertNotNull(nodes);
		Node england = nodes.getZones().get(0);
		Node barnsley = england.getZones().get(0);
		Node billingley = barnsley.getZones().get(0);
		assertEquals(england.getId(), "6269131");
		assertEquals(england.getLevel(), 0);
		assertEquals(england.getName(), "ENGLAND");
		assertEquals(barnsley.getId(), "6269131-3333122");
		assertEquals(barnsley.getLevel(), 1);
		assertEquals(barnsley.getName(), "BARNSLEY");
		assertEquals(billingley.getId(), "6269131-3333122-7299739");
		assertEquals(billingley.getLevel(), 2);
		assertEquals(billingley.getName(), "BILLINGLEY");
	}

}
