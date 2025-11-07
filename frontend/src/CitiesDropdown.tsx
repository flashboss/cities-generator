import React, { useState, useEffect, useRef } from 'react';
import { Node, Nodes } from './types';
import './CitiesDropdown.css';

interface CitiesDropdownProps {
  dataUrl?: string;
  data?: Nodes;
  country?: string;
  placeholder?: string;
  onSelect?: (node: Node) => void;
  className?: string;
}

export const CitiesDropdown: React.FC<CitiesDropdownProps> = ({
  dataUrl,
  data,
  country = 'it',
  placeholder = 'Select location...',
  onSelect,
  className = '',
}) => {
  const [nodes, setNodes] = useState<Nodes | null>(data || null);
  const [selectedPath, setSelectedPath] = useState<Node[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (data) {
      setNodes(data);
      return;
    }

    if (dataUrl) {
      loadData(dataUrl);
    } else {
      // Try to load from default location
      const defaultUrl = `/cities-generator/${country}.json`;
      loadData(defaultUrl);
    }
  }, [dataUrl, data, country]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const loadData = async (url: string) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(url);
      if (!response.ok) {
        // Check if response is HTML (404 page) instead of JSON
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('text/html')) {
          // In development, try to load from $HOME/cities-generator via Vite middleware
          // In production/remote, suggest using dataUrl prop
          const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
          if (isLocalhost) {
            throw new Error(`File not found at ${url}. Make sure the file exists in $HOME/cities-generator/${url.split('/').pop()}`);
          } else {
            throw new Error(`File not found at ${url}. Please provide a dataUrl prop.`);
          }
        }
        throw new Error(`Failed to load data: ${response.statusText}`);
      }
      const text = await response.text();
      // Check if response is HTML (like a 404 page)
      if (text.trim().startsWith('<!DOCTYPE') || text.trim().startsWith('<!doctype')) {
        const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
        if (isLocalhost) {
          throw new Error(`File not found at ${url}. Make sure the file exists in $HOME/cities-generator/${url.split('/').pop()}`);
        } else {
          throw new Error(`File not found at ${url}. Please provide a dataUrl prop.`);
        }
      }
      const jsonData: Nodes = JSON.parse(text);
      setNodes(jsonData);
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

  if (loading) {
    return <div className={`cities-dropdown loading ${className}`}>Loading...</div>;
  }

  if (error) {
    return <div className={`cities-dropdown error ${className}`}>Error: {error}</div>;
  }

  if (!nodes) {
    return <div className={`cities-dropdown error ${className}`}>No data available</div>;
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
        {selectedPath.length > 0 && (
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
                  onClick={() => handleNodeClick(node, selectedPath.length)}
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

