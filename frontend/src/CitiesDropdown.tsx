import React, { useState, useEffect, useRef } from 'react';
import { Node, Nodes, DropdownConfig, CountryInfo } from './types';
import './CitiesDropdown.css';

interface CitiesDropdownProps {
  dataUrl?: string;
  data?: Nodes;
  country?: string;
  placeholder?: string;
  onSelect?: (node: Node) => void;
  className?: string;
  config?: DropdownConfig;
  onCountrySelect?: (countryCode: string) => void;
  availableCountries?: CountryInfo[];
  onLoadCountries?: () => Promise<CountryInfo[]>;
}

export const CitiesDropdown: React.FC<CitiesDropdownProps> = ({
  dataUrl,
  data,
  country: initialCountry = 'it',
  placeholder = 'Select location...',
  onSelect,
  className = '',
  config,
  onCountrySelect,
  availableCountries = [],
  onLoadCountries,
}) => {
  const [nodes, setNodes] = useState<Nodes | null>(data || null);
  const [selectedPath, setSelectedPath] = useState<Node[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedCountry, setSelectedCountry] = useState<string | null>(
    config?.startFromCountry ? null : initialCountry
  );
  const [countries, setCountries] = useState<CountryInfo[]>(availableCountries);
  const [showCountrySelection, setShowCountrySelection] = useState(
    config?.startFromCountry || false
  );
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (config?.startFromCountry && onLoadCountries) {
      loadCountries();
    } else if (config?.startFromCountry && availableCountries.length > 0) {
      setCountries(availableCountries);
      setShowCountrySelection(true);
    }
  }, [config?.startFromCountry, onLoadCountries, availableCountries]);

  useEffect(() => {
    if (data) {
      setNodes(data);
      return;
    }

    if (!showCountrySelection && selectedCountry) {
      loadCountryData(selectedCountry);
    }
  }, [data, selectedCountry, showCountrySelection]);

  const loadCountries = async () => {
    if (!onLoadCountries) return;
    setLoading(true);
    try {
      const loadedCountries = await onLoadCountries();
      setCountries(loadedCountries);
      setShowCountrySelection(true);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load countries');
      console.error('Error loading countries:', err);
    } finally {
      setLoading(false);
    }
  };

  const buildDataUrl = (countryCode: string): string => {
    if (dataUrl) {
      return dataUrl;
    }

    if (config) {
      if (config.dataSource === 'local') {
        // For local, use the Vite middleware path
        return `/cities-generator/${countryCode}.json`;
      } else if (config.dataSource === 'remote' && config.remoteUrl) {
        // For remote, construct URL
        const baseUrl = config.remoteUrl.replace(/\/$/, '');
        return `${baseUrl}/${countryCode}.json`;
      }
    }

    // Default: use Vite middleware
    return `/cities-generator/${countryCode}.json`;
  };

  const loadCountryData = async (countryCode: string) => {
    setLoading(true);
    setError(null);
    const url = buildDataUrl(countryCode);

    try {
      const headers: HeadersInit = {
        'Content-Type': 'application/json',
      };

      // Add authentication if configured
      if (config?.dataSource === 'remote' && config.username && config.password) {
        const credentials = btoa(`${config.username}:${config.password}`);
        headers['Authorization'] = `Basic ${credentials}`;
      }

      const response = await fetch(url, { headers });

      if (!response.ok) {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('text/html')) {
          const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
          if (isLocalhost) {
            throw new Error(`File not found at ${url}. Make sure the file exists in $HOME/cities-generator/${countryCode}.json`);
          } else {
            throw new Error(`File not found at ${url}. Please check the remote URL configuration.`);
          }
        }
        throw new Error(`Failed to load data: ${response.statusText}`);
      }

      const text = await response.text();
      if (text.trim().startsWith('<!DOCTYPE') || text.trim().startsWith('<!doctype')) {
        const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
        if (isLocalhost) {
          throw new Error(`File not found at ${url}. Make sure the file exists in $HOME/cities-generator/${countryCode}.json`);
        } else {
          throw new Error(`File not found at ${url}. Please check the remote URL configuration.`);
        }
      }

      const jsonData = JSON.parse(text);
      
      // Check if the response contains an error object
      if (jsonData && typeof jsonData === 'object' && 'error' in jsonData) {
        const errorMessage = jsonData.message || jsonData.error || 'Failed to load cities data';
        throw new Error(errorMessage);
      }
      
      // Validate it's a valid Nodes object
      if (!jsonData || typeof jsonData !== 'object' || !('zones' in jsonData)) {
        throw new Error('Invalid response: expected a valid cities data structure');
      }
      
      setNodes(jsonData as Nodes);
      setShowCountrySelection(false);
      if (onCountrySelect) {
        onCountrySelect(countryCode);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load cities data');
      console.error('Error loading cities data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCountryClick = (country: CountryInfo) => {
    setSelectedCountry(country.code);
    setSelectedPath([]);
  };

  const handleNodeClick = (node: Node, level: number) => {
    const newPath = selectedPath.slice(0, level);
    newPath[level] = node;
    setSelectedPath(newPath);

    if (!node.zones || node.zones.length === 0) {
      // Leaf node selected
      if (onSelect) {
        onSelect(node);
      }
      setIsOpen(false);
    }
  };

  const getCurrentLevelNodes = (): Node[] => {
    if (showCountrySelection) {
      // Return countries as nodes for selection
      return countries.map(country => ({
        id: country.code,
        name: `${country.code.toUpperCase()} - ${country.name}`,
        level: 0,
        zones: [],
      }));
    }

    if (!nodes) return [];
    if (selectedPath.length === 0) return nodes.zones;

    let current: Node | undefined = selectedPath[selectedPath.length - 1];
    return current?.zones || [];
  };

  const getDisplayText = (): string => {
    if (showCountrySelection) {
      return selectedCountry ? `${selectedCountry.toUpperCase()} - ${countries.find(c => c.code === selectedCountry)?.name || ''}` : 'Select country...';
    }
    if (selectedPath.length === 0) return placeholder;
    return selectedPath.map(n => n.name).join(' > ');
  };

  const clearSelection = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (showCountrySelection) {
      setSelectedCountry(null);
      setNodes(null);
    } else {
      setSelectedPath([]);
    }
    if (onSelect) {
      onSelect(null as any);
    }
  };

  if (loading && !nodes) {
    return <div className={`cities-dropdown loading ${className}`}>Loading...</div>;
  }

  if (error && !nodes) {
    return <div className={`cities-dropdown error ${className}`}>Error: {error}</div>;
  }

  const currentLevelNodes = getCurrentLevelNodes();

  return (
    <div className={`cities-dropdown ${className}`} ref={dropdownRef}>
      <div
        className="cities-dropdown-trigger"
        onClick={() => setIsOpen(!isOpen)}
        role="button"
        tabIndex={0}
        aria-expanded={isOpen}
        aria-haspopup="listbox"
      >
        <span className="cities-dropdown-text">{getDisplayText()}</span>
        {(selectedPath.length > 0 || selectedCountry) && (
          <button
            className="cities-dropdown-clear"
            onClick={clearSelection}
            aria-label="Clear selection"
          >
            ×
          </button>
        )}
        <span className="cities-dropdown-arrow">{isOpen ? '▲' : '▼'}</span>
      </div>

      {isOpen && (
        <div className="cities-dropdown-menu" role="listbox">
          {showCountrySelection && selectedCountry && (
            <div className="cities-dropdown-breadcrumb">
              <button
                className="cities-dropdown-breadcrumb-item"
                onClick={() => {
                  setSelectedCountry(null);
                  setNodes(null);
                  setSelectedPath([]);
                }}
              >
                ← Back to countries
              </button>
            </div>
          )}

          {!showCountrySelection && selectedPath.length > 0 && (
            <div className="cities-dropdown-breadcrumb">
              {selectedPath.map((node, index) => (
                <button
                  key={node.id}
                  className="cities-dropdown-breadcrumb-item"
                  onClick={() => {
                    const newPath = selectedPath.slice(0, index + 1);
                    setSelectedPath(newPath);
                  }}
                >
                  {node.name}
                  {index < selectedPath.length - 1 && ' > '}
                </button>
              ))}
            </div>
          )}

          {currentLevelNodes.length === 0 ? (
            <div className="cities-dropdown-empty">No options available</div>
          ) : (
            <ul className="cities-dropdown-list">
              {currentLevelNodes.map((node) => (
                <li
                  key={node.id}
                  className={`cities-dropdown-item ${
                    node.zones && node.zones.length > 0 ? 'has-children' : 'leaf'
                  } ${showCountrySelection ? 'country-item' : ''}`}
                  role="option"
                  onClick={() => {
                    if (showCountrySelection) {
                      handleCountryClick(countries.find(c => c.code === node.id) || { code: node.id, name: node.name, file: `${node.id}.json` });
                    } else {
                      handleNodeClick(node, selectedPath.length);
                    }
                  }}
                >
                  <span className="cities-dropdown-item-name">{node.name}</span>
                  {node.zones && node.zones.length > 0 && !showCountrySelection && (
                    <span className="cities-dropdown-item-arrow">▶</span>
                  )}
                  {showCountrySelection && (
                    <span className="cities-dropdown-item-arrow">▶</span>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
};
