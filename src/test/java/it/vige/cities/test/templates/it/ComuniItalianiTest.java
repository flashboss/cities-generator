package it.vige.cities.test.templates.it;

import it.vige.cities.Countries;
import it.vige.cities.Generator;
import it.vige.cities.templates.it.Providers;
import org.junit.jupiter.api.Test;

public class ComuniItalianiTest {

	@Test
	public void testCities() {
		Generator generator = new Generator(Countries.IT.name(), Providers.TUTTITALIA.name(), false);
		generator.generate();
	}

}
