package it.vige.cities;

import java.util.HashMap;
import java.util.Map;

/**
 * Continents according to ISO standards
 * @author lucastancapiano
 */
public enum Continents {

	/**
	 * AF - Africa
	 */
	AF("AF", "Africa"),

	/**
	 * AS - Asia
	 */
	AS("AS", "Asia"),

	/**
	 * EU - Europe
	 */
	EU("EU", "Europe"),

	/**
	 * NA - North America
	 */
	NA("NA", "North America"),

	/**
	 * SA - South America
	 */
	SA("SA", "South America"),

	/**
	 * OC - Oceania
	 */
	OC("OC", "Oceania"),

	/**
	 * AN - Antarctica
	 */
	AN("AN", "Antarctica");

	private String code;
	private String name;

	private Continents(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * Get the continent code (e.g., "EU", "AS")
	 * @return the continent code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Get the continent name
	 * @return the continent name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Map of country codes to continents (ISO 3166-1 alpha-2)
	 */
	private static final Map<String, Continents> COUNTRY_TO_CONTINENT = new HashMap<>();

	static {
		// Europe (EU)
		COUNTRY_TO_CONTINENT.put("IT", EU); // Italy
		COUNTRY_TO_CONTINENT.put("GB", EU); // United Kingdom
		COUNTRY_TO_CONTINENT.put("FR", EU); // France
		COUNTRY_TO_CONTINENT.put("DE", EU); // Germany
		COUNTRY_TO_CONTINENT.put("ES", EU); // Spain
		COUNTRY_TO_CONTINENT.put("PT", EU); // Portugal
		COUNTRY_TO_CONTINENT.put("NL", EU); // Netherlands
		COUNTRY_TO_CONTINENT.put("BE", EU); // Belgium
		COUNTRY_TO_CONTINENT.put("CH", EU); // Switzerland
		COUNTRY_TO_CONTINENT.put("AT", EU); // Austria
		COUNTRY_TO_CONTINENT.put("SE", EU); // Sweden
		COUNTRY_TO_CONTINENT.put("NO", EU); // Norway
		COUNTRY_TO_CONTINENT.put("DK", EU); // Denmark
		COUNTRY_TO_CONTINENT.put("FI", EU); // Finland
		COUNTRY_TO_CONTINENT.put("PL", EU); // Poland
		COUNTRY_TO_CONTINENT.put("GR", EU); // Greece
		COUNTRY_TO_CONTINENT.put("IE", EU); // Ireland
		COUNTRY_TO_CONTINENT.put("CZ", EU); // Czech Republic
		COUNTRY_TO_CONTINENT.put("HU", EU); // Hungary
		COUNTRY_TO_CONTINENT.put("RO", EU); // Romania
		COUNTRY_TO_CONTINENT.put("BG", EU); // Bulgaria
		COUNTRY_TO_CONTINENT.put("HR", EU); // Croatia
		COUNTRY_TO_CONTINENT.put("SK", EU); // Slovakia
		COUNTRY_TO_CONTINENT.put("SI", EU); // Slovenia
		COUNTRY_TO_CONTINENT.put("LT", EU); // Lithuania
		COUNTRY_TO_CONTINENT.put("LV", EU); // Latvia
		COUNTRY_TO_CONTINENT.put("EE", EU); // Estonia
		COUNTRY_TO_CONTINENT.put("LU", EU); // Luxembourg
		COUNTRY_TO_CONTINENT.put("MT", EU); // Malta
		COUNTRY_TO_CONTINENT.put("CY", EU); // Cyprus
		COUNTRY_TO_CONTINENT.put("IS", EU); // Iceland
		COUNTRY_TO_CONTINENT.put("LI", EU); // Liechtenstein
		COUNTRY_TO_CONTINENT.put("MC", EU); // Monaco
		COUNTRY_TO_CONTINENT.put("SM", EU); // San Marino
		COUNTRY_TO_CONTINENT.put("VA", EU); // Vatican City
		COUNTRY_TO_CONTINENT.put("AD", EU); // Andorra
		COUNTRY_TO_CONTINENT.put("AL", EU); // Albania
		COUNTRY_TO_CONTINENT.put("BA", EU); // Bosnia and Herzegovina
		COUNTRY_TO_CONTINENT.put("ME", EU); // Montenegro
		COUNTRY_TO_CONTINENT.put("MK", EU); // North Macedonia
		COUNTRY_TO_CONTINENT.put("RS", EU); // Serbia
		COUNTRY_TO_CONTINENT.put("XK", EU); // Kosovo
		COUNTRY_TO_CONTINENT.put("UA", EU); // Ukraine
		COUNTRY_TO_CONTINENT.put("BY", EU); // Belarus
		COUNTRY_TO_CONTINENT.put("MD", EU); // Moldova
		COUNTRY_TO_CONTINENT.put("RU", EU); // Russia (European part, but commonly included)

		// North America (NA)
		COUNTRY_TO_CONTINENT.put("US", NA); // United States
		COUNTRY_TO_CONTINENT.put("CA", NA); // Canada
		COUNTRY_TO_CONTINENT.put("MX", NA); // Mexico
		COUNTRY_TO_CONTINENT.put("GT", NA); // Guatemala
		COUNTRY_TO_CONTINENT.put("BZ", NA); // Belize
		COUNTRY_TO_CONTINENT.put("SV", NA); // El Salvador
		COUNTRY_TO_CONTINENT.put("HN", NA); // Honduras
		COUNTRY_TO_CONTINENT.put("NI", NA); // Nicaragua
		COUNTRY_TO_CONTINENT.put("CR", NA); // Costa Rica
		COUNTRY_TO_CONTINENT.put("PA", NA); // Panama
		COUNTRY_TO_CONTINENT.put("CU", NA); // Cuba
		COUNTRY_TO_CONTINENT.put("JM", NA); // Jamaica
		COUNTRY_TO_CONTINENT.put("HT", NA); // Haiti
		COUNTRY_TO_CONTINENT.put("DO", NA); // Dominican Republic
		COUNTRY_TO_CONTINENT.put("BS", NA); // Bahamas
		COUNTRY_TO_CONTINENT.put("BB", NA); // Barbados
		COUNTRY_TO_CONTINENT.put("TT", NA); // Trinidad and Tobago

		// South America (SA)
		COUNTRY_TO_CONTINENT.put("BR", SA); // Brazil
		COUNTRY_TO_CONTINENT.put("AR", SA); // Argentina
		COUNTRY_TO_CONTINENT.put("CL", SA); // Chile
		COUNTRY_TO_CONTINENT.put("CO", SA); // Colombia
		COUNTRY_TO_CONTINENT.put("PE", SA); // Peru
		COUNTRY_TO_CONTINENT.put("VE", SA); // Venezuela
		COUNTRY_TO_CONTINENT.put("EC", SA); // Ecuador
		COUNTRY_TO_CONTINENT.put("BO", SA); // Bolivia
		COUNTRY_TO_CONTINENT.put("PY", SA); // Paraguay
		COUNTRY_TO_CONTINENT.put("UY", SA); // Uruguay
		COUNTRY_TO_CONTINENT.put("GY", SA); // Guyana
		COUNTRY_TO_CONTINENT.put("SR", SA); // Suriname
		COUNTRY_TO_CONTINENT.put("GF", SA); // French Guiana
		COUNTRY_TO_CONTINENT.put("FK", SA); // Falkland Islands

		// Asia (AS)
		COUNTRY_TO_CONTINENT.put("CN", AS); // China
		COUNTRY_TO_CONTINENT.put("JP", AS); // Japan
		COUNTRY_TO_CONTINENT.put("IN", AS); // India
		COUNTRY_TO_CONTINENT.put("KR", AS); // South Korea
		COUNTRY_TO_CONTINENT.put("KP", AS); // North Korea
		COUNTRY_TO_CONTINENT.put("ID", AS); // Indonesia
		COUNTRY_TO_CONTINENT.put("TH", AS); // Thailand
		COUNTRY_TO_CONTINENT.put("VN", AS); // Vietnam
		COUNTRY_TO_CONTINENT.put("PH", AS); // Philippines
		COUNTRY_TO_CONTINENT.put("MY", AS); // Malaysia
		COUNTRY_TO_CONTINENT.put("SG", AS); // Singapore
		COUNTRY_TO_CONTINENT.put("BD", AS); // Bangladesh
		COUNTRY_TO_CONTINENT.put("PK", AS); // Pakistan
		COUNTRY_TO_CONTINENT.put("AF", AS); // Afghanistan
		COUNTRY_TO_CONTINENT.put("IR", AS); // Iran
		COUNTRY_TO_CONTINENT.put("IQ", AS); // Iraq
		COUNTRY_TO_CONTINENT.put("SA", AS); // Saudi Arabia
		COUNTRY_TO_CONTINENT.put("AE", AS); // United Arab Emirates
		COUNTRY_TO_CONTINENT.put("IL", AS); // Israel
		COUNTRY_TO_CONTINENT.put("TR", AS); // Turkey
		COUNTRY_TO_CONTINENT.put("KZ", AS); // Kazakhstan
		COUNTRY_TO_CONTINENT.put("UZ", AS); // Uzbekistan
		COUNTRY_TO_CONTINENT.put("MM", AS); // Myanmar
		COUNTRY_TO_CONTINENT.put("LK", AS); // Sri Lanka
		COUNTRY_TO_CONTINENT.put("NP", AS); // Nepal
		COUNTRY_TO_CONTINENT.put("KH", AS); // Cambodia
		COUNTRY_TO_CONTINENT.put("LA", AS); // Laos
		COUNTRY_TO_CONTINENT.put("MN", AS); // Mongolia
		COUNTRY_TO_CONTINENT.put("TW", AS); // Taiwan
		COUNTRY_TO_CONTINENT.put("HK", AS); // Hong Kong
		COUNTRY_TO_CONTINENT.put("MO", AS); // Macau

		// Africa (AF)
		COUNTRY_TO_CONTINENT.put("ZA", AF); // South Africa
		COUNTRY_TO_CONTINENT.put("EG", AF); // Egypt
		COUNTRY_TO_CONTINENT.put("NG", AF); // Nigeria
		COUNTRY_TO_CONTINENT.put("KE", AF); // Kenya
		COUNTRY_TO_CONTINENT.put("ET", AF); // Ethiopia
		COUNTRY_TO_CONTINENT.put("GH", AF); // Ghana
		COUNTRY_TO_CONTINENT.put("TZ", AF); // Tanzania
		COUNTRY_TO_CONTINENT.put("UG", AF); // Uganda
		COUNTRY_TO_CONTINENT.put("DZ", AF); // Algeria
		COUNTRY_TO_CONTINENT.put("MA", AF); // Morocco
		COUNTRY_TO_CONTINENT.put("SD", AF); // Sudan
		COUNTRY_TO_CONTINENT.put("AO", AF); // Angola
		COUNTRY_TO_CONTINENT.put("MZ", AF); // Mozambique
		COUNTRY_TO_CONTINENT.put("ZM", AF); // Zambia
		COUNTRY_TO_CONTINENT.put("ZW", AF); // Zimbabwe
		COUNTRY_TO_CONTINENT.put("MW", AF); // Malawi
		COUNTRY_TO_CONTINENT.put("MG", AF); // Madagascar
		COUNTRY_TO_CONTINENT.put("CM", AF); // Cameroon
		COUNTRY_TO_CONTINENT.put("CI", AF); // Côte d'Ivoire
		COUNTRY_TO_CONTINENT.put("SN", AF); // Senegal
		COUNTRY_TO_CONTINENT.put("ML", AF); // Mali
		COUNTRY_TO_CONTINENT.put("BF", AF); // Burkina Faso
		COUNTRY_TO_CONTINENT.put("NE", AF); // Niger
		COUNTRY_TO_CONTINENT.put("TD", AF); // Chad
		COUNTRY_TO_CONTINENT.put("LY", AF); // Libya
		COUNTRY_TO_CONTINENT.put("TN", AF); // Tunisia
		COUNTRY_TO_CONTINENT.put("RW", AF); // Rwanda
		COUNTRY_TO_CONTINENT.put("BJ", AF); // Benin
		COUNTRY_TO_CONTINENT.put("TG", AF); // Togo
		COUNTRY_TO_CONTINENT.put("GN", AF); // Guinea
		COUNTRY_TO_CONTINENT.put("SL", AF); // Sierra Leone
		COUNTRY_TO_CONTINENT.put("LR", AF); // Liberia
		COUNTRY_TO_CONTINENT.put("MR", AF); // Mauritania
		COUNTRY_TO_CONTINENT.put("GM", AF); // Gambia
		COUNTRY_TO_CONTINENT.put("GW", AF); // Guinea-Bissau
		COUNTRY_TO_CONTINENT.put("CV", AF); // Cape Verde
		COUNTRY_TO_CONTINENT.put("ST", AF); // São Tomé and Príncipe
		COUNTRY_TO_CONTINENT.put("GQ", AF); // Equatorial Guinea
		COUNTRY_TO_CONTINENT.put("GA", AF); // Gabon
		COUNTRY_TO_CONTINENT.put("CG", AF); // Republic of the Congo
		COUNTRY_TO_CONTINENT.put("CD", AF); // Democratic Republic of the Congo
		COUNTRY_TO_CONTINENT.put("CF", AF); // Central African Republic
		COUNTRY_TO_CONTINENT.put("SS", AF); // South Sudan
		COUNTRY_TO_CONTINENT.put("ER", AF); // Eritrea
		COUNTRY_TO_CONTINENT.put("DJ", AF); // Djibouti
		COUNTRY_TO_CONTINENT.put("SO", AF); // Somalia
		COUNTRY_TO_CONTINENT.put("KM", AF); // Comoros
		COUNTRY_TO_CONTINENT.put("MU", AF); // Mauritius
		COUNTRY_TO_CONTINENT.put("SC", AF); // Seychelles
		COUNTRY_TO_CONTINENT.put("BW", AF); // Botswana
		COUNTRY_TO_CONTINENT.put("NA", AF); // Namibia
		COUNTRY_TO_CONTINENT.put("LS", AF); // Lesotho
		COUNTRY_TO_CONTINENT.put("SZ", AF); // Eswatini

		// Oceania (OC)
		COUNTRY_TO_CONTINENT.put("AU", OC); // Australia
		COUNTRY_TO_CONTINENT.put("NZ", OC); // New Zealand
		COUNTRY_TO_CONTINENT.put("PG", OC); // Papua New Guinea
		COUNTRY_TO_CONTINENT.put("FJ", OC); // Fiji
		COUNTRY_TO_CONTINENT.put("NC", OC); // New Caledonia
		COUNTRY_TO_CONTINENT.put("PF", OC); // French Polynesia
		COUNTRY_TO_CONTINENT.put("SB", OC); // Solomon Islands
		COUNTRY_TO_CONTINENT.put("VU", OC); // Vanuatu
		COUNTRY_TO_CONTINENT.put("WS", OC); // Samoa
		COUNTRY_TO_CONTINENT.put("TO", OC); // Tonga
		COUNTRY_TO_CONTINENT.put("KI", OC); // Kiribati
		COUNTRY_TO_CONTINENT.put("FM", OC); // Micronesia
		COUNTRY_TO_CONTINENT.put("MH", OC); // Marshall Islands
		COUNTRY_TO_CONTINENT.put("PW", OC); // Palau
		COUNTRY_TO_CONTINENT.put("TV", OC); // Tuvalu
		COUNTRY_TO_CONTINENT.put("NR", OC); // Nauru

		// Antarctica (AN)
		COUNTRY_TO_CONTINENT.put("AQ", AN); // Antarctica
	}

	/**
	 * Get continent from country code (ISO 3166-1 alpha-2)
	 * @param countryCode the country code (e.g., "IT", "US", "GB")
	 * @return the continent, defaults to EU if country not found
	 */
	public static Continents fromCountryCode(String countryCode) {
		if (countryCode == null || countryCode.isEmpty()) {
			return EU; // Default to Europe
		}
		String upperCode = countryCode.toUpperCase();
		Continents continent = COUNTRY_TO_CONTINENT.get(upperCode);
		return continent != null ? continent : EU; // Default to Europe if not found
	}
}
