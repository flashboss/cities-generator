package it.vige.cities.test.templates.it;

import static it.vige.cities.Countries.IT;
import static it.vige.cities.Languages.EN;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.WIKIPEDIA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.Configuration;
import it.vige.cities.Continents;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.it.Wikipedia;

/**
 * Wikipedia tests
 * 
 * @author lucastancapiano
 */
public class WikipediaTest extends FileGenerator {

	/**
	 * Cities
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(IT.name());
		configuration.setProvider(WIKIPEDIA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(IT.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node liguria = northWest.getZones().get(0);
		Node genova = liguria.getZones().get(3);
		Node arenzano = genova.getZones().get(0);
		assertEquals("1", northWest.getId());
		assertEquals(0, northWest.getLevel());
		assertEquals("I: ITALIA NORD-OCCIDENTALE", northWest.getName());
		assertEquals("1-6", liguria.getId());
		assertEquals(1, liguria.getLevel());
		assertEquals("LIGURIA", liguria.getName());
		assertEquals("1-6-177", genova.getId());
		assertEquals(2, genova.getLevel());
		assertEquals("GENOVA", genova.getName());
		assertEquals("1-6-177-178", arenzano.getId());
		assertEquals(3, arenzano.getLevel());
		assertEquals("ARENZANO", arenzano.getName());
	}

	/**
	 * Copyright
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCopyright() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(IT.name());
		configuration.setProvider(WIKIPEDIA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);

		// Read JSON file as JsonNode to access copyright field
		// File structure is now: {continent}/{country}/{language}.json
		Continents continent = Continents.fromCountryCode(IT.name());
		File jsonFile = new File(FileGenerator.CITIES_HOME + continent.getCode() + File.separator + IT.name() + File.separator + Languages.IT.getCode() + ".json");
		assertTrue(jsonFile.exists(), "JSON file should exist");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonFile);
		assertNotNull(rootNode, "JSON should be readable");

		// Verify copyright field exists
		JsonNode copyrightNode = rootNode.get("copyright");
		assertNotNull(copyrightNode, "Copyright field should exist in JSON");
		assertTrue(copyrightNode.isTextual(), "Copyright should be a string");

		String copyright = copyrightNode.asText();
		assertNotNull(copyright, "Copyright should not be null");
		assertTrue(!copyright.isEmpty(), "Copyright should not be empty");

		// Read version from version.properties
		String expectedVersion = null;
		try {
			InputStream is = FileGenerator.class.getResourceAsStream("/it/vige/cities/version.properties");
			if (is != null) {
				Properties props = new Properties();
				props.load(is);
				expectedVersion = props.getProperty("version");
				is.close();
			}
		} catch (Exception e) {
			// Ignore
		}
		assertNotNull(expectedVersion, "Version should be readable from version.properties");
		assertTrue(!expectedVersion.isEmpty(), "Version should not be empty");

		// Verify copyright contains required information
		assertTrue(copyright.contains("cities-generator"), "Copyright should contain 'cities-generator'");
		assertTrue(copyright.contains("Vige community"), "Copyright should contain 'Vige community'");
		assertTrue(copyright.contains("version " + expectedVersion), 
				"Copyright should contain 'version " + expectedVersion + "'");
		assertTrue(copyright.contains("template: Wikipedia"), "Copyright should contain 'template: Wikipedia'");
		assertTrue(copyright.contains("Copyright (c) Vige"), "Copyright should contain 'Copyright (c) Vige'");
		assertTrue(copyright.contains("Apache License"), "Copyright should contain 'Apache License'");
	}

	/**
	 * Test that Wikipedia template only supports Italian
	 */
	@Test
	public void testLanguageSupport() {
		Wikipedia wikipedia = new Wikipedia(false, false);
		
		// Should support Italian
		assertTrue(wikipedia.isLanguageSupported(Languages.IT), "Wikipedia should support Italian");
		
		// Should not support other languages
		assertFalse(wikipedia.isLanguageSupported(EN), "Wikipedia should not support English");
		assertFalse(wikipedia.isLanguageSupported(Languages.FR), "Wikipedia should not support French");
		assertFalse(wikipedia.isLanguageSupported(Languages.DE), "Wikipedia should not support German");
		assertFalse(wikipedia.isLanguageSupported(Languages.ES), "Wikipedia should not support Spanish");
		assertFalse(wikipedia.isLanguageSupported(Languages.PT), "Wikipedia should not support Portuguese");
	}

	/**
	 * Test that Wikipedia template only supports Italy (IT)
	 */
	@Test
	public void testCountrySupport() {
		Wikipedia wikipedia = new Wikipedia(false, false);
		
		// Should support Italy
		assertTrue(wikipedia.isCountrySupported(IT.name()), "Wikipedia should support Italy");
		assertTrue(wikipedia.isCountrySupported("it"), "Wikipedia should support Italy (lowercase)");
		assertTrue(wikipedia.isCountrySupported("IT"), "Wikipedia should support Italy (uppercase)");
		
		// Should not support other countries
		assertFalse(wikipedia.isCountrySupported("GB"), "Wikipedia should not support Great Britain");
		assertFalse(wikipedia.isCountrySupported("US"), "Wikipedia should not support United States");
		assertFalse(wikipedia.isCountrySupported("FR"), "Wikipedia should not support France");
		assertFalse(wikipedia.isCountrySupported("DE"), "Wikipedia should not support Germany");
	}

}
