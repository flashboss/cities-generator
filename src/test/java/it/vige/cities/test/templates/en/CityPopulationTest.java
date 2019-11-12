package it.vige.cities.test.templates.en;

import it.vige.cities.Countries;
import it.vige.cities.Generator;
import it.vige.cities.templates.en.Providers;
import org.junit.jupiter.api.Test;

public class CityPopulationTest {

	@Test
	public void testCities() {
		Generator generator = new Generator(Countries.EN.name(), Providers.CITYPOPULATION.name(), false);
		generator.generate();
	}

}
