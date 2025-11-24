import React, { useState, useEffect } from 'react';
import { DropdownConfig } from './types';
import i18nCountries from 'i18n-iso-countries';
import enLocale from 'i18n-iso-countries/langs/en.json';
import { getContinentFromCountry } from './continentUtils';
import './DropdownConfig.css';

// Register English locale for country names
i18nCountries.registerLocale(enLocale);

// Maximum available model number (0 = default, 1 = cascading dropdowns, etc.)
const MAX_MODEL = 1;

interface DropdownConfigProps {
  config: DropdownConfig;
  onConfigChange: (config: DropdownConfig) => void;
}

interface CountryOption {
  code: string;
  name: string;
}

interface LanguageOption {
  code: string;
  name: string;
}

export const DropdownConfigComponent: React.FC<DropdownConfigProps> = ({
  config,
  onConfigChange,
}) => {
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [countries, setCountries] = useState<CountryOption[]>([]);
  const [languages, setLanguages] = useState<LanguageOption[]>([]);
  const [loadingCountries, setLoadingCountries] = useState(false);
  const [loadingLanguages, setLoadingLanguages] = useState(false);
  const [countriesError, setCountriesError] = useState<string | null>(null);
  const [languagesError, setLanguagesError] = useState<string | null>(null);

  const updateConfig = (updates: Partial<DropdownConfig>) => {
    onConfigChange({ ...config, ...updates });
  };

  // Load available countries by reading directories from the data URL
  // Structure: {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
  useEffect(() => {
    const loadCountries = async () => {
      const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db';
      const dataUrl = config.dataUrl || DEFAULT_GITHUB_URL;
      const baseUrl = dataUrl.replace(/\.json$/, '').replace(/\/$/, '');

      setLoadingCountries(true);
      setCountriesError(null);

      try {
        // Check if it's a GitHub URL (more robust check)
        const isGitHub = baseUrl.includes('github.com') || baseUrl.includes('raw.githubusercontent.com');
        
        let countryCodes: string[] = [];
        
        if (isGitHub) {
          // Use GitHub API to list continent directories, then country directories within each continent
          // Convert raw.githubusercontent.com URL to api.github.com URL
          // Example: https://raw.githubusercontent.com/user/repo/branch/path
          // To: https://api.github.com/repos/user/repo/contents/path
          const githubMatch = baseUrl.match(/https?:\/\/raw\.githubusercontent\.com\/([^\/]+)\/([^\/]+)\/([^\/]+)\/?(.*)/);
          if (githubMatch) {
            const [, user, repo, branch, path] = githubMatch;
            // Remove trailing slash from path if present
            const cleanPath = path.replace(/\/$/, '');
            const apiUrl = `https://api.github.com/repos/${user}/${repo}/contents/${cleanPath || ''}?ref=${branch}`;
            
            try {
              const response = await fetch(apiUrl);
              if (response.ok) {
                const contents = await response.json();
                // Filter for continent directories (type === 'dir')
                const continentDirs = Array.isArray(contents) 
                  ? contents.filter((item: any) => item.type === 'dir')
                  : [];
                
                // For each continent, get country directories
                const countryPromises = continentDirs.map(async (continentDir: any) => {
                  const continentApiUrl = `https://api.github.com/repos/${user}/${repo}/contents/${cleanPath ? `${cleanPath}/` : ''}${continentDir.name}?ref=${branch}`;
                  try {
                    const continentResponse = await fetch(continentApiUrl);
                    if (continentResponse.ok) {
                      const countryContents = await continentResponse.json();
                      const countryDirs = Array.isArray(countryContents)
                        ? countryContents.filter((item: any) => item.type === 'dir')
                        : [];
                      return countryDirs.map((dir: any) => dir.name.toUpperCase());
                    }
                  } catch {
                    // Skip if continent directory can't be read
                  }
                  return [];
                });
                
                const countryArrays = await Promise.all(countryPromises);
                countryCodes = countryArrays.flat();
              } else {
                console.error('GitHub API error:', response.status, response.statusText);
              }
            } catch (err) {
              console.error('Error fetching GitHub API:', err);
            }
          } else {
            console.warn('GitHub URL detected but regex did not match:', baseUrl);
          }
        } else {
          // Fallback for non-GitHub servers: try to verify which directories exist
          // IMPORTANT: Never use this for GitHub URLs - they don't support directory listing
          if (baseUrl.includes('github.com') || baseUrl.includes('raw.githubusercontent.com')) {
            console.warn('Skipping fallback directory check for GitHub URL:', baseUrl);
            setCountries([]);
            setLoadingCountries(false);
            return;
          }
          
          // Get all ISO 3166-1 alpha-2 country codes
          const allCountryCodes = i18nCountries.getAlpha2Codes();
          const allCodes = Object.keys(allCountryCodes).map(code => code.toUpperCase());
          
          // Try to verify which directories exist by checking if they respond
          const checkPromises = allCodes.map(async (code) => {
            try {
              // Try HEAD request to directory (some servers return 200/403 for existing directories)
              const testUrl = `${baseUrl}/${code}/`;
              const testResponse = await fetch(testUrl, { method: 'HEAD' });
              // Accept 200, 403 (forbidden but exists), or 405 (method not allowed but exists)
              if (testResponse.ok || testResponse.status === 403 || testResponse.status === 405) {
                return code;
              }
            } catch {
              // Try GET as fallback
              try {
                const testUrl = `${baseUrl}/${code}/`;
                const testResponse = await fetch(testUrl, { method: 'GET' });
                if (testResponse.ok || testResponse.status === 403 || testResponse.status === 405) {
                  return code;
                }
              } catch {
                // Skip if directory doesn't exist
              }
            }
            return null;
          });
          
          const results = await Promise.all(checkPromises);
          countryCodes = results.filter((c): c is string => c !== null);
        }
        
        // Map country codes to CountryOption with names
        const countryList = countryCodes.map((code) => ({
          code: code.toUpperCase(),
          name: i18nCountries.getName(code.toLowerCase(), 'en') || code.toUpperCase(),
        }));
        
        setCountries(countryList);
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

  // Load available languages for the selected country
  // Structure: {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
  useEffect(() => {
    const loadLanguages = async () => {
      if (!config.country) {
        setLanguages([]);
        return;
      }

      const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db';
      const baseUrl = (config.dataUrl || DEFAULT_GITHUB_URL).replace(/\.json$/, '').replace(/\/$/, '');
      const countryCode = config.country.toUpperCase();
      const continent = getContinentFromCountry(countryCode);

      setLoadingLanguages(true);
      setLanguagesError(null);

      try {
        // Common language codes to check
        const languageCodes = [
          { code: 'it', name: 'Italian' },
          { code: 'en', name: 'English' },
          { code: 'fr', name: 'French' },
          { code: 'de', name: 'German' },
          { code: 'es', name: 'Spanish' },
          { code: 'pt', name: 'Portuguese' },
        ];
        
        // Check which languages are available for the selected country
        // Structure: {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
        const isGitHub = baseUrl.includes('github.com') || baseUrl.includes('raw.githubusercontent.com');
        
        let foundLanguages: LanguageOption[] = [];
        
        if (isGitHub) {
          // Use GitHub API to list files in the country directory
          // Convert raw.githubusercontent.com URL to api.github.com URL
          // Example: https://raw.githubusercontent.com/user/repo/branch/path
          // To: https://api.github.com/repos/user/repo/contents/path/countryCode
          const githubMatch = baseUrl.match(/https?:\/\/raw\.githubusercontent\.com\/([^\/]+)\/([^\/]+)\/([^\/]+)\/?(.*)/);
          if (githubMatch) {
            const [, user, repo, branch, path] = githubMatch;
            // Remove trailing slash from path if present
            const cleanPath = path.replace(/\/$/, '');
            const apiUrl = `https://api.github.com/repos/${user}/${repo}/contents/${cleanPath ? `${cleanPath}/` : ''}${continent}/${countryCode}?ref=${branch}`;
            
            try {
              const response = await fetch(apiUrl);
              if (response.ok) {
                const contents = await response.json();
                // Filter for JSON files (type === 'file' and name ends with .json)
                const files = Array.isArray(contents) 
                  ? contents.filter((item: any) => item.type === 'file' && item.name.endsWith('.json'))
                  : [];
                // Extract language codes from filenames (e.g., "it.json" -> "it")
                const availableLangCodes = files.map((file: any) => file.name.replace('.json', '').toLowerCase());
                foundLanguages = languageCodes.filter(lang => availableLangCodes.includes(lang.code));
              }
            } catch (err) {
              console.error('Error fetching GitHub API for languages:', err);
            }
          } else {
            console.warn('GitHub URL detected but regex did not match for languages:', baseUrl);
          }
        } else {
          // Fallback for non-GitHub servers: check each language file
          // IMPORTANT: Never use this for GitHub URLs - use GitHub API instead
          if (baseUrl.includes('github.com') || baseUrl.includes('raw.githubusercontent.com')) {
            console.warn('Skipping fallback language check for GitHub URL:', baseUrl);
            setLanguages([]);
            setLoadingLanguages(false);
            return;
          }
          
          const checkPromises = languageCodes.map(async (lang) => {
              try {
                const testUrl = `${baseUrl}/${continent}/${countryCode}/${lang.code}.json`;
                const testResponse = await fetch(testUrl, { method: 'HEAD' });
              if (testResponse.ok) {
                return lang;
              }
            } catch {
              // Try GET with Range header as fallback
              try {
                const testUrl = `${baseUrl}/${continent}/${countryCode}/${lang.code}.json`;
                const testResponse = await fetch(testUrl, { method: 'GET', headers: { 'Range': 'bytes=0-0' } });
                if (testResponse.ok || testResponse.status === 206) {
                  return lang;
                }
              } catch {
                // Skip if file doesn't exist (404 is normal, don't log as error)
              }
            }
            return null;
          });
          
          const results = await Promise.all(checkPromises);
          foundLanguages = results.filter((l): l is LanguageOption => l !== null);
        }
        
        setLanguages(foundLanguages);
        
        // If current language is not available, reset to first available or 'it'
        if (foundLanguages.length > 0 && (!config.language || !foundLanguages.find((l: LanguageOption) => l.code === config.language))) {
          updateConfig({ language: foundLanguages[0].code });
        }
      } catch (err) {
        console.error('Error loading languages:', err);
        setLanguagesError('Could not load languages list');
        setLanguages([]);
      } finally {
        setLoadingLanguages(false);
      }
    };

    if (showAdvanced && config.country) {
      loadLanguages();
    }
  }, [config.dataUrl, config.country, showAdvanced]);

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
              <div style={{ flex: '1' }}>
                <label style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', gap: '8px' }}>
                  Username:
                  <input
                    type="text"
                    value={config.username || ''}
                    onChange={(e) => updateConfig({ username: e.target.value })}
                    placeholder="username"
                  />
                </label>
                <small>Username for HTTP basic authentication (optional)</small>
              </div>
              <div style={{ flex: '1' }}>
                <label style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', gap: '8px' }}>
                  Password:
                  <input
                    type="password"
                    value={config.password || ''}
                    onChange={(e) => updateConfig({ password: e.target.value })}
                    placeholder="password"
                  />
                </label>
                <small>Password for HTTP basic authentication (optional)</small>
              </div>
            </div>
          </div>

          <div className="dropdown-config-section" style={{ display: 'flex', flexDirection: 'row', gap: '20px', flexWrap: 'wrap' }}>
            <div style={{ flex: '1', minWidth: '200px' }}>
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

            <div style={{ flex: '1', minWidth: '200px' }}>
              <label>
                Language:
                {!config.country ? (
                  <select disabled>
                    <option>Select a country first</option>
                  </select>
                ) : loadingLanguages ? (
                  <select disabled>
                    <option>Loading languages...</option>
                  </select>
                ) : languages.length === 0 ? (
                  <div style={{ color: '#d32f2f', padding: '8px', border: '1px solid #d32f2f', borderRadius: '4px' }}>
                    No languages available for {config.country}. Check the remote URL.
                  </div>
                ) : (
                  <select
                    value={config.language || languages[0]?.code || 'it'}
                    onChange={(e) => updateConfig({ language: e.target.value })}
                  >
                    {languages.map((lang) => (
                      <option key={lang.code} value={lang.code}>
                        {lang.name} ({lang.code})
                      </option>
                    ))}
                  </select>
                )}
              </label>
              {languagesError && (
                <small style={{ color: '#d32f2f' }}>{languagesError}</small>
              )}
              {!languagesError && !loadingLanguages && languages.length > 0 && (
                <small>Select language from available JSON files (format: {config.country}/{config.language}.json)</small>
              )}
            </div>
          </div>

          <div className="dropdown-config-section">
            <label>
              Placeholder:
              <input
                type="text"
                className="dropdown-config-remote-url"
                value={Array.isArray(config.placeholder) ? config.placeholder.join(', ') : (config.placeholder || '')}
                onChange={(e) => {
                  // Store as string while typing to allow commas
                  const value = e.target.value;
                  updateConfig({ placeholder: value || undefined });
                }}
                onBlur={(e) => {
                  // Parse into array only when user leaves the field
                  const value = e.target.value.trim();
                  if (!value) {
                    updateConfig({ placeholder: undefined });
                    return;
                  }
                  if (value.includes(',')) {
                    // Multiple placeholders separated by commas
                    const placeholders = value.split(',').map(p => p.trim()).filter(p => p.length > 0);
                    updateConfig({ placeholder: placeholders.length > 1 ? placeholders : placeholders[0] || undefined });
                  } else {
                    updateConfig({ placeholder: value || undefined });
                  }
                }}
                placeholder="Select location... or Level 0, Level 1, Level 2"
              />
            </label>
            <small>Placeholder text displayed in the dropdown. For multiple placeholders (e.g., cascading dropdowns), separate with commas: "Level 0, Level 1, Level 2"</small>
          </div>

          <div className="dropdown-config-section">
            <label>
              Model:
              <input
                type="number"
                min="0"
                max={MAX_MODEL}
                value={config.model !== undefined ? config.model : 0}
                onChange={(e) => {
                  const value = parseInt(e.target.value) || 0;
                  const newModel = Math.min(Math.max(0, value), MAX_MODEL);
                  const updates: Partial<DropdownConfig> = { model: newModel };
                  // If model is 1, disable search
                  if (newModel === 1) {
                    updates.enableSearch = false;
                    updates.searchPlaceholder = undefined;
                  }
                  updateConfig(updates);
                }}
                style={{ width: '100px' }}
              />
            </label>
            <small>Template model code (0 = default template, 1 = cascading dropdowns, max: {MAX_MODEL})</small>
          </div>

          <div className="dropdown-config-section" style={{ display: 'flex', flexDirection: 'row', gap: '20px', flexWrap: 'wrap' }}>
            <div style={{ flex: '1', minWidth: '200px' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                <input
                  type="checkbox"
                  checked={config.popup || false}
                  onChange={(e) => updateConfig({ popup: e.target.checked })}
                />
                <span>Show popup on selection</span>
              </label>
              <small>Show alert popup when a location is selected</small>
            </div>

            <div style={{ flex: '1', minWidth: '200px' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: config.model === 1 ? 'not-allowed' : 'pointer' }}>
                <input
                  type="checkbox"
                  checked={config.enableSearch || false}
                  onChange={(e) => updateConfig({ enableSearch: e.target.checked })}
                  disabled={config.model === 1}
                />
                <span>Enable search</span>
              </label>
              {config.enableSearch && config.model !== 1 && (
                <label style={{ display: 'flex', alignItems: 'center', gap: '8px', marginTop: '10px' }}>
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
              <small>Enable text search to find locations by name {config.model === 1 && '(not for model 1)'}</small>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

