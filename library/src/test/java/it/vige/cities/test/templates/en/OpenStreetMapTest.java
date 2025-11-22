package it.vige.cities.test.templates.en;

import static it.vige.cities.Continents.EU;
import static it.vige.cities.Countries.GB;
import static it.vige.cities.Languages.EN;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.Providers.OPENSTREETMAP;
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
 * OpenStreetMap tests
 * 
 * @author lucastancapiano
 */
public class OpenStreetMapTest extends FileGenerator {

	/**
	 * Init
	 */
	@BeforeEach
	public void init() {
		language = EN;
	}

	/**
	 * Test OpenStreetMap generation for GB
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(GB.name());
		configuration.setProvider(OPENSTREETMAP.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		configuration.setLanguage(EN);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK, "Generation should succeed");
		Nodes nodes = readFile(GB.name());
		assertNotNull(nodes, "Nodes should not be null");
		assertTrue(nodes.getZones() != null && !nodes.getZones().isEmpty(), "Zones should not be empty");
		
		// Verify structure: should have at least one top-level node
		Node firstNode = nodes.getZones().get(0);
		assertNotNull(firstNode, "First node should not be null");
		assertEquals(0, firstNode.getLevel(), "First node should be at level 0");
		assertNotNull(firstNode.getName(), "First node should have a name");
		assertNotNull(firstNode.getId(), "First node should have an ID");
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
		configuration.setProvider(OPENSTREETMAP.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		configuration.setLanguage(EN);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK, "Generation should succeed");
		
		// Verify GB is mapped to EU continent
		Continents continent = Continents.fromCountryCode(GB.name());
		assertEquals(EU, continent, "GB should be mapped to EU continent");
		
		// Verify file structure includes continent
		String expectedPath = FileGenerator.CITIES_HOME + continent.getCode() + File.separator + GB.name() + File.separator + EN.getCode() + ".json";
		File expectedFile = new File(expectedPath);
		assertTrue(expectedFile.exists(), "File should exist at: " + expectedPath);
	}

	/**
	 * Test language support
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLanguageSupport() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(GB.name());
		configuration.setProvider(OPENSTREETMAP.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		configuration.setLanguage(EN);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK, "Generation should succeed with English language");
		
		// Verify file exists with correct language code
		Continents continent = Continents.fromCountryCode(GB.name());
		String expectedPath = FileGenerator.CITIES_HOME + continent.getCode() + File.separator + GB.name() + File.separator + EN.getCode() + ".json";
		File expectedFile = new File(expectedPath);
		assertTrue(expectedFile.exists(), "File should exist with language code: " + EN.getCode());
	}
}

