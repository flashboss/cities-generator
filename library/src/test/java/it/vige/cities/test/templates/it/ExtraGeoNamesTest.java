package it.vige.cities.test.templates.it;

import static it.vige.cities.Continents.EU;
import static it.vige.cities.Countries.IT;
import static it.vige.cities.Languages.EN;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.EXTRA_GEONAMES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.Continents;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.Languages;
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
		configuration.setCountry(IT.name());
		configuration.setProvider(EXTRA_GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(IT.name());
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

	/**
	 * Test continent-based file structure
	 * 
	 * @throws Exception
	 */
	@Test
	public void testContinentFileStructure() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(IT.name());
		configuration.setProvider(EXTRA_GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		
		// Verify file is created in continent-based structure: EU/IT/it.json
		Continents continent = Continents.fromCountryCode(IT.name());
		assertEquals(EU, continent);
		String expectedPath = FileGenerator.CITIES_HOME + continent.getCode() + File.separator + IT.name() + File.separator + Languages.IT.getCode() + ".json";
		File expectedFile = new File(expectedPath);
		assertTrue(expectedFile.exists(), "File should exist at: " + expectedPath);
	}

	/**
	 * Test with different language
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWithEnglishLanguage() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(IT.name());
		configuration.setLanguage(EN);
		configuration.setProvider(EXTRA_GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		
		// Verify file is created with English language: EU/IT/en.json
		Continents continent = Continents.fromCountryCode(IT.name());
		String expectedPath = FileGenerator.CITIES_HOME + continent.getCode() + File.separator + IT.name() + File.separator + EN.getCode() + ".json";
		File expectedFile = new File(expectedPath);
		assertTrue(expectedFile.exists(), "File should exist at: " + expectedPath);
		
		// Verify we can read it back
		Nodes nodes = readFile(IT.name(), EN);
		assertNotNull(nodes);
	}

}
