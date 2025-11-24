package it.vige.cities.templates.it;

/**
 * The Italian-specific providers actually supported
 * Providers that are specific to Italy and provide Italian administrative data
 * 
 * @author lucastancapiano
 */
public enum Providers {
	
	/**
	 * WIKIPEDIA - Wikipedia-based provider for Italian cities
	 */
	WIKIPEDIA, 
	
	/**
	 * COMUNI_ITALIANI - Official Italian municipalities database provider
	 */
	COMUNI_ITALIANI, 
	
	/**
	 * EXTRA_GEONAMES - GeoNames provider with Italian macroregions (level 0)
	 */
	EXTRA_GEONAMES,
	
	/**
	 * EXTRA_OPENSTREETMAP - OpenStreetMap provider with Italian macroregions (level 0)
	 */
	EXTRA_OPENSTREETMAP
}
