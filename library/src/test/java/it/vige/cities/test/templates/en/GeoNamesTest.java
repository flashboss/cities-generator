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

public class GeoNamesTest extends FileGenerator {

	@Test
	public void testCities() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setCountry(Countries.uk.name());
		configuration.setProvider(Providers.GEONAMES.name());
		configuration.setCaseSensitive(false);
		configuration.setDuplicatedNames(false);
		Generator generator = new Generator(configuration, true);
		Result result = generator.generateFile();
		Assertions.assertTrue(result == Result.OK);
		Nodes nodes = readFile(Countries.uk.name());
		Assertions.assertNotNull(nodes);
		Node england = nodes.getZones().get(0);
		Node barnsley = england.getZones().get(0);
		Node billingley = barnsley.getZones().get(0);
		Assertions.assertEquals(england.getId(), 6269131);
		Assertions.assertEquals(england.getLevel(), 0);
		Assertions.assertEquals(england.getName(), "ENGLAND");
		Assertions.assertEquals(barnsley.getId(), 3333122);
		Assertions.assertEquals(barnsley.getLevel(), 1);
		Assertions.assertEquals(barnsley.getName(), "BARNSLEY");
		Assertions.assertEquals(billingley.getId(), 7299739);
		Assertions.assertEquals(billingley.getLevel(), 2);
		Assertions.assertEquals(billingley.getName(), "BILLINGLEY");
	}

}
