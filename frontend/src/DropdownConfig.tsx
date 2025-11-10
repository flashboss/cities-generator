import React, { useState, useEffect } from 'react';
import { DropdownConfig } from './types';
import i18nCountries from 'i18n-iso-countries';
import enLocale from 'i18n-iso-countries/langs/en.json';
import './DropdownConfig.css';

// Register English locale for country names
i18nCountries.registerLocale(enLocale);

interface DropdownConfigProps {
  config: DropdownConfig;
  onConfigChange: (config: DropdownConfig) => void;
}

interface CountryOption {
  code: string;
  name: string;
}

export const DropdownConfigComponent: React.FC<DropdownConfigProps> = ({
  config,
  onConfigChange,
}) => {
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [countries, setCountries] = useState<CountryOption[]>([]);
  const [loadingCountries, setLoadingCountries] = useState(false);
  const [countriesError, setCountriesError] = useState<string | null>(null);

  const updateConfig = (updates: Partial<DropdownConfig>) => {
    onConfigChange({ ...config, ...updates });
  };

  // Load available countries from remote URL by checking which JSON files exist
  useEffect(() => {
    const loadCountries = async () => {
      const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU';
      const baseUrl = (config.dataUrl || DEFAULT_GITHUB_URL).replace(/\.json$/, '').replace(/\/$/, '');

      setLoadingCountries(true);
      setCountriesError(null);

      try {
        // Get all ISO 3166-1 alpha-2 country codes
        const allCountryCodes = i18nCountries.getAlpha2Codes();
        const countryList = Object.keys(allCountryCodes).map((code) => ({
          code: code.toLowerCase(),
          name: i18nCountries.getName(code, 'en') || code.toUpperCase(),
        }));
        
        // Try to fetch each country to see which ones exist
        const checkPromises = countryList.map(async (country) => {
          try {
            const testUrl = `${baseUrl}/${country.code}.json`;
            const testResponse = await fetch(testUrl, { method: 'HEAD' });
            if (testResponse.ok) {
              return country;
            }
          } catch {
            // Try GET with Range header as fallback
            try {
              const testUrl = `${baseUrl}/${country.code}.json`;
              const testResponse = await fetch(testUrl, { method: 'GET', headers: { 'Range': 'bytes=0-0' } });
              if (testResponse.ok || testResponse.status === 206) {
                return country;
              }
            } catch {
              // Skip if file doesn't exist
            }
          }
          return null;
        });
        
        const results = await Promise.all(checkPromises);
        const found = results.filter((c): c is CountryOption => c !== null);
        
        setCountries(found);
      } catch (err) {
        console.error('Error loading countries:', err);
        setCountriesError('Could not load countries list');
        setCountries([]);
      } finally {
        setLoadingCountries(false);
      }
    };

    if (showAdvanced) {
      loadCountries();
    }
  }, [config.dataUrl, showAdvanced]);

  return (
    <div className="dropdown-config">
      <div className="dropdown-config-header">
        <h3>Dropdown Configuration</h3>
        <button
          className="dropdown-config-toggle"
          onClick={() => setShowAdvanced(!showAdvanced)}
        >
          {showAdvanced ? '▼' : '▶'} {showAdvanced ? 'Hide' : 'Show'} Configuration
        </button>
      </div>

      {showAdvanced && (
        <div className="dropdown-config-content">
          <div className="dropdown-config-section">
            <label>
              Data URL:
              <input
                type="text"
                className="dropdown-config-remote-url"
                value={config.dataUrl || ''}
                onChange={(e) => updateConfig({ dataUrl: e.target.value })}
                placeholder="https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU"
              />
            </label>
            <small>Base URL for remote data source (default: GitHub repository)</small>

            <div className="dropdown-config-credentials">
              <label>
                Username (optional):
                <input
                  type="text"
                  value={config.username || ''}
                  onChange={(e) => updateConfig({ username: e.target.value })}
                  placeholder="username"
                />
              </label>
              <label>
                Password (optional):
                <input
                  type="password"
                  value={config.password || ''}
                  onChange={(e) => updateConfig({ password: e.target.value })}
                  placeholder="password"
                />
              </label>
            </div>
          </div>

          <div className="dropdown-config-section">
            <label>
              Country:
              {loadingCountries ? (
                <select disabled>
                  <option>Loading countries...</option>
                </select>
              ) : countries.length === 0 ? (
                <div style={{ color: '#d32f2f', padding: '8px', border: '1px solid #d32f2f', borderRadius: '4px' }}>
                  No countries available. Check the remote URL.
                </div>
              ) : (
                <select
                  value={config.country || countries[0]?.code || ''}
                  onChange={(e) => updateConfig({ country: e.target.value })}
                >
                  {countries.map((country) => (
                    <option key={country.code} value={country.code}>
                      {country.name} ({country.code})
                    </option>
                  ))}
                </select>
              )}
            </label>
            {countriesError && (
              <small style={{ color: '#d32f2f' }}>{countriesError}</small>
            )}
            {!countriesError && !loadingCountries && countries.length > 0 && (
              <small>Select country from available JSON files</small>
            )}
          </div>

          <div className="dropdown-config-section">
            <label>
              Placeholder:
              <input
                type="text"
                className="dropdown-config-remote-url"
                value={config.placeholder || ''}
                onChange={(e) => updateConfig({ placeholder: e.target.value })}
                placeholder="Select location..."
              />
            </label>
            <small>Placeholder text displayed in the dropdown (default: "Select location...")</small>
          </div>

          <div className="dropdown-config-section">
            <div style={{ display: 'flex', alignItems: 'center', gap: '15px', flexWrap: 'wrap' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                <input
                  type="checkbox"
                  checked={config.enableSearch || false}
                  onChange={(e) => updateConfig({ enableSearch: e.target.checked })}
                />
                <span>Enable search</span>
              </label>
              {config.enableSearch && (
                <label style={{ display: 'flex', alignItems: 'center', gap: '8px', flex: '1', minWidth: '200px' }}>
                  <span style={{ whiteSpace: 'nowrap' }}>Search placeholder:</span>
                  <input
                    type="text"
                    value={config.searchPlaceholder || ''}
                    onChange={(e) => updateConfig({ searchPlaceholder: e.target.value })}
                    placeholder="Search location..."
                    style={{ flex: '1', minWidth: '150px' }}
                  />
                </label>
              )}
            </div>
            <small>Enable text search to find locations by name</small>
          </div>
        </div>
      )}
    </div>
  );
};

