package it.vige.cities.test.templates.it;

import static it.vige.cities.Countries.IT;
import static it.vige.cities.Languages.EN;
import static it.vige.cities.Result.OK;
import static it.vige.cities.templates.it.Providers.COMUNI_ITALIANI;
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
import it.vige.cities.templates.it.ComuniItaliani;

/**
 * Comuni italiani tests
 * @author lucastancapiano
 */
public class ComuniItalianiTest extends FileGenerator {

	/**
	 * Cities
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(IT.name());
		configuration.setProvider(COMUNI_ITALIANI.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		ResultNodes result = generator.generate();
		assertTrue(result.getResult() == OK);
		Nodes nodes = readFile(IT.name());
		assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node liguria = northWest.getZones().get(0);
		Node genova = liguria.getZones().get(0);
		Node arenzano = genova.getZones().get(0);
		assertEquals("1", northWest.getId());
		assertEquals(0, northWest.getLevel());
		assertEquals("ITALIA NORD OCCIDENTALE", northWest.getName());
		assertEquals("1-2", liguria.getId());
		assertEquals(1, liguria.getLevel());
		assertEquals("LIGURIA", liguria.getName());
		assertEquals("1-2-3", genova.getId());
		assertEquals(2, genova.getLevel());
		assertEquals("GENOVA", genova.getName());
		assertEquals("1-2-3-4", arenzano.getId());
		assertEquals(3, arenzano.getLevel());
		assertEquals("ARENZANO", arenzano.getName());

	}

	/**
	 * Test that ComuniItaliani template only supports Italian
	 */
	@Test
	public void testLanguageSupport() {
		ComuniItaliani comuniItaliani = new ComuniItaliani(false, false);
		
		// Should support Italian
		assertTrue(comuniItaliani.isLanguageSupported(Languages.IT), "ComuniItaliani should support Italian");
		
		// Should not support other languages
		assertFalse(comuniItaliani.isLanguageSupported(EN), "ComuniItaliani should not support English");
		assertFalse(comuniItaliani.isLanguageSupported(Languages.FR), "ComuniItaliani should not support French");
		assertFalse(comuniItaliani.isLanguageSupported(Languages.DE), "ComuniItaliani should not support German");
		assertFalse(comuniItaliani.isLanguageSupported(Languages.ES), "ComuniItaliani should not support Spanish");
		assertFalse(comuniItaliani.isLanguageSupported(Languages.PT), "ComuniItaliani should not support Portuguese");
	}

	/**
	 * Test that ComuniItaliani template only supports Italy (IT)
	 */
	@Test
	public void testCountrySupport() {
		ComuniItaliani comuniItaliani = new ComuniItaliani(false, false);
		
		// Should support Italy
		assertTrue(comuniItaliani.isCountrySupported(IT.name()), "ComuniItaliani should support Italy");
		assertTrue(comuniItaliani.isCountrySupported("it"), "ComuniItaliani should support Italy (lowercase)");
		assertTrue(comuniItaliani.isCountrySupported("IT"), "ComuniItaliani should support Italy (uppercase)");
		
		// Should not support other countries
		assertFalse(comuniItaliani.isCountrySupported("GB"), "ComuniItaliani should not support Great Britain");
		assertFalse(comuniItaliani.isCountrySupported("US"), "ComuniItaliani should not support United States");
		assertFalse(comuniItaliani.isCountrySupported("FR"), "ComuniItaliani should not support France");
		assertFalse(comuniItaliani.isCountrySupported("DE"), "ComuniItaliani should not support Germany");
	}

}
