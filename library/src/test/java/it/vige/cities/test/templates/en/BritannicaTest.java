package it.vige.cities.test.templates.en;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.Countries;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.Result;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.en.Providers;

/**
 * 
 * @author lucastancapiano
 *
 *         Britannica tests
 */
public class BritannicaTest extends FileGenerator {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(Countries.uk.name());
		configuration.setProvider(Providers.BRITANNICA.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		Result result = generator.generateFile();
		Assertions.assertTrue(result == Result.OK);
		Nodes nodes = readFile(Countries.uk.name());
		Assertions.assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node liguria = northWest.getZones().get(0);
		Node genova = liguria.getZones().get(0);
		Node arenzano = genova.getZones().get(0);
		Assertions.assertEquals(northWest.getId(), 0);
		Assertions.assertEquals(northWest.getLevel(), 0);
		Assertions.assertEquals(northWest.getName(), "ITALIA NORD OCCIDENTALE");
		Assertions.assertEquals(liguria.getId(), 1);
		Assertions.assertEquals(liguria.getLevel(), 1);
		Assertions.assertEquals(liguria.getName(), "LIGURIA");
		Assertions.assertEquals(genova.getId(), 2);
		Assertions.assertEquals(genova.getLevel(), 2);
		Assertions.assertEquals(genova.getName(), "GENOVA");
		Assertions.assertEquals(arenzano.getId(), 3);
		Assertions.assertEquals(arenzano.getLevel(), 3);
		Assertions.assertEquals(arenzano.getName(), "ARENZANO");
	}
}
