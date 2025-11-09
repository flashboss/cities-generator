import React, { useState, useEffect, useRef } from 'react';
import { Node, Nodes, DropdownConfig } from './types';
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
}) => {
  const [nodes, setNodes] = useState<Nodes | null>(data || null);
  const [selectedPath, setSelectedPath] = useState<Node[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedCountry] = useState<string | null>(initialCountry);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (data) {
      setNodes(data);
      return;
    }

    if (selectedCountry) {
      loadCountryData(selectedCountry);
    }
  }, [data, selectedCountry]);

  const buildDataUrl = (countryCode: string): string => {
    if (dataUrl) {
      // If dataUrl already ends with .json, use it as is
      // Otherwise, append /{countryCode}.json to the base URL
      if (dataUrl.endsWith('.json')) {
        return dataUrl;
      }
      // dataUrl is a base URL, append the country file
      const baseUrl = dataUrl.replace(/\/$/, '');
      return `${baseUrl}/${countryCode}.json`;
    }

    // Default GitHub URL
    const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/europe';
    const baseUrl = (config?.remoteUrl || DEFAULT_GITHUB_URL).replace(/\/$/, '');
    return `${baseUrl}/${countryCode}.json`;
  };

  const loadCountryData = async (countryCode: string) => {
    setLoading(true);
    setError(null);
    const url = buildDataUrl(countryCode);

    try {
      const headers: HeadersInit = {};

      // Add authentication if configured
      if (config?.username && config?.password) {
        const credentials = btoa(`${config.username}:${config.password}`);
        headers['Authorization'] = `Basic ${credentials}`;
      }

      const response = await fetch(url, { headers });

      if (!response.ok) {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('text/html')) {
          throw new Error(`File not found at ${url}. Please check the remote URL configuration.`);
        }
        throw new Error(`Failed to load data: ${response.statusText}`);
      }

      const text = await response.text();
      if (text.trim().startsWith('<!DOCTYPE') || text.trim().startsWith('<!doctype')) {
        throw new Error(`File not found at ${url}. Please check the remote URL configuration.`);
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
    if (!nodes) return [];
    if (selectedPath.length === 0) return nodes.zones;

    let current: Node | undefined = selectedPath[selectedPath.length - 1];
    return current?.zones || [];
  };

  const getDisplayText = (): string => {
    if (selectedPath.length === 0) return placeholder;
    return selectedPath.map(n => n.name).join(' > ');
  };

  const clearSelection = (e: React.MouseEvent) => {
    e.stopPropagation();
    setSelectedPath([]);
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
          {selectedPath.length > 0 && (
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
                  }`}
                  role="option"
                  onClick={() => {
                    handleNodeClick(node, selectedPath.length);
                  }}
                >
                  <span className="cities-dropdown-item-name">{node.name}</span>
                  {node.zones && node.zones.length > 0 && (
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
