import React, { useState } from 'react';
import { DropdownConfig } from './types';
import './DropdownConfig.css';

interface DropdownConfigProps {
  config: DropdownConfig;
  onConfigChange: (config: DropdownConfig) => void;
}

export const DropdownConfigComponent: React.FC<DropdownConfigProps> = ({
  config,
  onConfigChange,
}) => {
  const [showAdvanced, setShowAdvanced] = useState(false);

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
              Remote URL:
              <input
                type="text"
                value={config.remoteUrl || ''}
                onChange={(e) => updateConfig({ remoteUrl: e.target.value })}
                placeholder="https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/europe"
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
              <input
                type="text"
                value={config.country || ''}
                onChange={(e) => updateConfig({ country: e.target.value })}
                placeholder="it"
              />
            </label>
            <small>Country code (e.g., "it", "uk", "fr") - default: "it"</small>
          </div>

          <div className="dropdown-config-section">
            <label>
              Placeholder:
              <input
                type="text"
                value={config.placeholder || ''}
                onChange={(e) => updateConfig({ placeholder: e.target.value })}
                placeholder="Select location..."
              />
            </label>
            <small>Placeholder text displayed in the dropdown (default: "Select location...")</small>
          </div>
        </div>
      )}
    </div>
  );
};

