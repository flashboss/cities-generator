/**
 * Utility functions for continent mapping
 */

/**
 * Get continent code from country code (ISO 3166-1 alpha-2)
 * @param countryCode the country code (e.g., "IT", "US", "GB")
 * @returns the continent code (EU, AS, NA, SA, OC, AF, AN), defaults to EU
 */
export function getContinentFromCountry(countryCode: string): string {
  const countryToContinent: Record<string, string> = {
    // Europe (EU)
    'IT': 'EU', 'GB': 'EU', 'FR': 'EU', 'DE': 'EU', 'ES': 'EU', 'PT': 'EU',
    'NL': 'EU', 'BE': 'EU', 'CH': 'EU', 'AT': 'EU', 'SE': 'EU', 'NO': 'EU',
    'DK': 'EU', 'FI': 'EU', 'PL': 'EU', 'GR': 'EU', 'IE': 'EU', 'CZ': 'EU',
    'HU': 'EU', 'RO': 'EU', 'BG': 'EU', 'HR': 'EU', 'SK': 'EU', 'SI': 'EU',
    'LT': 'EU', 'LV': 'EU', 'EE': 'EU', 'LU': 'EU', 'MT': 'EU', 'CY': 'EU',
    'IS': 'EU', 'LI': 'EU', 'MC': 'EU', 'SM': 'EU', 'VA': 'EU', 'AD': 'EU',
    'AL': 'EU', 'BA': 'EU', 'ME': 'EU', 'MK': 'EU', 'RS': 'EU', 'XK': 'EU',
    'UA': 'EU', 'BY': 'EU', 'MD': 'EU', 'RU': 'EU',
    // North America (NA)
    'US': 'NA', 'CA': 'NA', 'MX': 'NA', 'GT': 'NA', 'BZ': 'NA', 'SV': 'NA',
    'HN': 'NA', 'NI': 'NA', 'CR': 'NA', 'PA': 'NA', 'CU': 'NA', 'JM': 'NA',
    'HT': 'NA', 'DO': 'NA', 'BS': 'NA', 'BB': 'NA', 'TT': 'NA',
    // South America (SA)
    'BR': 'SA', 'AR': 'SA', 'CL': 'SA', 'CO': 'SA', 'PE': 'SA', 'VE': 'SA',
    'EC': 'SA', 'BO': 'SA', 'PY': 'SA', 'UY': 'SA', 'GY': 'SA', 'SR': 'SA',
    'GF': 'SA', 'FK': 'SA',
    // Asia (AS)
    'CN': 'AS', 'JP': 'AS', 'IN': 'AS', 'KR': 'AS', 'KP': 'AS', 'ID': 'AS',
    'TH': 'AS', 'VN': 'AS', 'PH': 'AS', 'MY': 'AS', 'SG': 'AS', 'BD': 'AS',
    'PK': 'AS', 'AF': 'AS', 'IR': 'AS', 'IQ': 'AS', 'SA': 'AS', 'AE': 'AS',
    'IL': 'AS', 'TR': 'AS', 'KZ': 'AS', 'UZ': 'AS', 'MM': 'AS', 'LK': 'AS',
    'NP': 'AS', 'KH': 'AS', 'LA': 'AS', 'MN': 'AS', 'TW': 'AS', 'HK': 'AS',
    'MO': 'AS',
    // Africa (AF)
    'ZA': 'AF', 'EG': 'AF', 'NG': 'AF', 'KE': 'AF', 'ET': 'AF', 'GH': 'AF',
    'TZ': 'AF', 'UG': 'AF', 'DZ': 'AF', 'MA': 'AF', 'SD': 'AF', 'AO': 'AF',
    'MZ': 'AF', 'ZM': 'AF', 'ZW': 'AF', 'MW': 'AF', 'MG': 'AF', 'CM': 'AF',
    'CI': 'AF', 'SN': 'AF', 'ML': 'AF', 'BF': 'AF', 'NE': 'AF', 'TD': 'AF',
    'LY': 'AF', 'TN': 'AF', 'RW': 'AF', 'BJ': 'AF', 'TG': 'AF', 'GN': 'AF',
    'SL': 'AF', 'LR': 'AF', 'MR': 'AF', 'GM': 'AF', 'GW': 'AF', 'CV': 'AF',
    'ST': 'AF', 'GQ': 'AF', 'GA': 'AF', 'CG': 'AF', 'CD': 'AF', 'CF': 'AF',
    'SS': 'AF', 'ER': 'AF', 'DJ': 'AF', 'SO': 'AF', 'KM': 'AF', 'MU': 'AF',
    'SC': 'AF', 'BW': 'AF', 'NA': 'AF', 'LS': 'AF', 'SZ': 'AF',
    // Oceania (OC)
    'AU': 'OC', 'NZ': 'OC', 'PG': 'OC', 'FJ': 'OC', 'NC': 'OC', 'PF': 'OC',
    'SB': 'OC', 'VU': 'OC', 'WS': 'OC', 'TO': 'OC', 'KI': 'OC', 'FM': 'OC',
    'MH': 'OC', 'PW': 'OC', 'TV': 'OC', 'NR': 'OC',
    // Antarctica (AN)
    'AQ': 'AN',
  };
  
  const upperCode = countryCode.toUpperCase();
  return countryToContinent[upperCode] || 'EU'; // Default to Europe if not found
}

