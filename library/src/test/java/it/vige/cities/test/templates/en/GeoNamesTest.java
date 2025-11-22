package it.vige.cities.test.templates.en;

import static it.vige.cities.Continents.EU;
import static it.vige.cities.Countries.GB;
import static it.vige.cities.Languages.EN;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.Providers.GEONAMES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.Continents;
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
	 * Init
	 */
	@BeforeEach
	public void init() {
		language = EN;
	}
	
	/**
	 * Cities
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(GB.name());
		configuration.setProvider(GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(GB.name());
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

	/**
	 * Test continent mapping for GB (should be EU)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testContinentMapping() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(GB.name());
		configuration.setProvider(GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		
		// Verify GB is mapped to EU continent
		Continents continent = Continents.fromCountryCode(GB.name());
		assertEquals(EU, continent);
		
		// Verify file structure includes continent
		String expectedPath = FileGenerator.CITIES_HOME + continent.getCode() + File.separator + GB.name() + File.separator + EN.getCode() + ".json";
		File expectedFile = new File(expectedPath);
		assertTrue(expectedFile.exists(), "File should exist at: " + expectedPath);
	}

}
