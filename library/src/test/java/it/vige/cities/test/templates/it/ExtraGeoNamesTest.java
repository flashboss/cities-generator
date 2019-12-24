package it.vige.cities.test.templates.it;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.vige.cities.Configuration;
import it.vige.cities.Countries;
import it.vige.cities.FileGenerator;
import it.vige.cities.Generator;
import it.vige.cities.Result;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.it.Providers;

public class ExtraGeoNamesTest extends FileGenerator {

	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(Countries.it.name());
		configuration.setProvider(Providers.EXTRA_GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		Result result = generator.generateFile();
		Assertions.assertTrue(result == Result.OK);
		Nodes nodes = readFile(Countries.it.name());
		Assertions.assertNotNull(nodes);
		Node northWest = nodes.getZones().get(0);
		Node regioneAutonomaValleDaosta = northWest.getZones().get(0);
		Node valleDaosta = regioneAutonomaValleDaosta.getZones().get(0);
		Node allein = valleDaosta.getZones().get(0);
		Assertions.assertEquals(northWest.getId(), 0);
		Assertions.assertEquals(northWest.getLevel(), 0);
		Assertions.assertEquals(northWest.getName(), "I: ITALIA NORD-OCCIDENTALE");
		Assertions.assertEquals(regioneAutonomaValleDaosta.getId(), 3164857);
		Assertions.assertEquals(regioneAutonomaValleDaosta.getLevel(), 1);
		Assertions.assertEquals(regioneAutonomaValleDaosta.getName(), "REGIONE AUTONOMA VALLE D'AOSTA");
		Assertions.assertEquals(valleDaosta.getId(), 3182996);
		Assertions.assertEquals(valleDaosta.getLevel(), 2);
		Assertions.assertEquals(valleDaosta.getName(), "VALLE D'AOSTA");
		Assertions.assertEquals(allein.getId(), 6543055);
		Assertions.assertEquals(allein.getLevel(), 3);
		Assertions.assertEquals(allein.getName(), "ALLEIN");
	}

}
