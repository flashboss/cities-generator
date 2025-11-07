import React, { useState, useEffect } from 'react';
import { DropdownConfig, CountryInfo } from './types';
import './DropdownConfig.css';

interface DropdownConfigProps {
  config: DropdownConfig;
  onConfigChange: (config: DropdownConfig) => void;
  availableCountries?: CountryInfo[];
  onLoadCountries?: () => Promise<CountryInfo[]>;
}

export const DropdownConfigComponent: React.FC<DropdownConfigProps> = ({
  config,
  onConfigChange,
  availableCountries = [],
  onLoadCountries,
}) => {
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [loadingCountries, setLoadingCountries] = useState(false);
  const [countries, setCountries] = useState<CountryInfo[]>(availableCountries);

  useEffect(() => {
    if (config.startFromCountry && onLoadCountries) {
      loadCountries();
    }
  }, [config.startFromCountry]);

  const loadCountries = async () => {
    if (!onLoadCountries) return;
    setLoadingCountries(true);
    try {
      const loadedCountries = await onLoadCountries();
      setCountries(loadedCountries);
    } catch (err) {
      console.error('Error loading countries:', err);
    } finally {
      setLoadingCountries(false);
    }
  };

  const updateConfig = (updates: Partial<DropdownConfig>) => {
    onConfigChange({ ...config, ...updates });
  };

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
              <input
                type="radio"
                name="dataSource"
                checked={config.dataSource === 'local'}
                onChange={() => updateConfig({ dataSource: 'local' })}
              />
              Local Data Source
            </label>
            {config.dataSource === 'local' && (
              <div className="dropdown-config-subsection">
                <label>
                  Local Path:
                  <input
                    type="text"
                    value={config.localPath || '$HOME/cities-generator'}
                    onChange={(e) => updateConfig({ localPath: e.target.value })}
                    placeholder="$HOME/cities-generator"
                  />
                </label>
                <small>Path to the local cities-generator directory</small>
              </div>
            )}
          </div>

          <div className="dropdown-config-section">
            <label>
              <input
                type="radio"
                name="dataSource"
                checked={config.dataSource === 'remote'}
                onChange={() => updateConfig({ dataSource: 'remote' })}
              />
              Remote Data Source
            </label>
            {config.dataSource === 'remote' && (
              <div className="dropdown-config-subsection">
                <label>
                  Remote URL:
                  <input
                    type="text"
                    value={config.remoteUrl || ''}
                    onChange={(e) => updateConfig({ remoteUrl: e.target.value })}
                    placeholder="https://example.com/api/cities-generator"
                  />
                </label>
                <small>Base URL for remote data source</small>

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
            )}
          </div>

          <div className="dropdown-config-section">
            <label>
              <input
                type="checkbox"
                checked={config.startFromCountry || false}
                onChange={(e) => {
                  updateConfig({ startFromCountry: e.target.checked });
                  if (e.target.checked && onLoadCountries) {
                    loadCountries();
                  }
                }}
              />
              Start from country selection
            </label>
            <small>Show available countries first, then load the selected country's data</small>

            {config.startFromCountry && (
              <div className="dropdown-config-countries">
                {loadingCountries ? (
                  <div className="dropdown-config-loading">Loading countries...</div>
                ) : countries.length > 0 ? (
                  <div className="dropdown-config-countries-list">
                    <strong>Available countries:</strong>
                    <ul>
                      {countries.map((country) => (
                        <li key={country.code}>
                          <strong>{country.code.toUpperCase()}</strong> - {country.name} ({country.file})
                        </li>
                      ))}
                    </ul>
                  </div>
                ) : (
                  <div className="dropdown-config-no-countries">
                    No countries found. Make sure JSON files exist in the data source.
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

