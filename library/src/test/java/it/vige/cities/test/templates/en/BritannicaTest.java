package it.vige.cities.test.templates.en;

import static it.vige.cities.Countries.GB;
import static it.vige.cities.Languages.EN;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.en.Providers.BRITANNICA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.Languages;
import it.vige.cities.ResultNodes;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.en.Britannica;

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
		configuration.setCountry(GB.name());
		configuration.setProvider(BRITANNICA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(GB.name());
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

	/**
	 * Test that Britannica template only supports English
	 */
	@Test
	public void testLanguageSupport() {
		Britannica britannica = new Britannica(false, false);
		
		// Should support English
		assertTrue(britannica.isLanguageSupported(EN), "Britannica should support English");
		
		// Should not support other languages
		assertFalse(britannica.isLanguageSupported(Languages.IT), "Britannica should not support Italian");
		assertFalse(britannica.isLanguageSupported(Languages.FR), "Britannica should not support French");
		assertFalse(britannica.isLanguageSupported(Languages.DE), "Britannica should not support German");
		assertFalse(britannica.isLanguageSupported(Languages.ES), "Britannica should not support Spanish");
		assertFalse(britannica.isLanguageSupported(Languages.PT), "Britannica should not support Portuguese");
	}

	/**
	 * Test that Britannica template only supports Great Britain (GB)
	 */
	@Test
	public void testCountrySupport() {
		Britannica britannica = new Britannica(false, false);
		
		// Should support Great Britain
		assertTrue(britannica.isCountrySupported(GB.name()), "Britannica should support Great Britain");
		assertTrue(britannica.isCountrySupported("gb"), "Britannica should support Great Britain (lowercase)");
		assertTrue(britannica.isCountrySupported("GB"), "Britannica should support Great Britain (uppercase)");
		
		// Should not support other countries
		assertFalse(britannica.isCountrySupported("IT"), "Britannica should not support Italy");
		assertFalse(britannica.isCountrySupported("US"), "Britannica should not support United States");
		assertFalse(britannica.isCountrySupported("FR"), "Britannica should not support France");
		assertFalse(britannica.isCountrySupported("DE"), "Britannica should not support Germany");
	}
}
