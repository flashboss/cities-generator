package it.vige.cities.templates;

/**
 * The providers actually supported for all countries
 * Generic providers that work for any country
 * 
 * @author lucastancapiano
 */
public enum Providers {
	/**
	 * NONE - Empty provider used for tests
	 */
	NONE, 
	
	/**
	 * GEONAMES - GeoNames API provider
	 */
	GEONAMES,
	
	/**
	 * OPENSTREETMAP - OpenStreetMap Overpass API provider
	 */
	OPENSTREETMAP
}
