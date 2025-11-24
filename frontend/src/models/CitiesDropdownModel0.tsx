import React, { useState, useRef } from 'react';
import { Node, Nodes } from '../types';
import { useCitiesData } from './useCitiesData';
import { useSearch } from './useSearch';

interface CitiesDropdownModel0Props {
  data?: Nodes;
  onSelect?: (node: Node) => void;
  className?: string;
  dataUrl?: string;
  country?: string;
  language?: string;
  placeholder?: string | string[];
  username?: string;
  password?: string;
  enableSearch?: boolean;
  searchPlaceholder?: string;
  popup?: boolean;
}

export const CitiesDropdownModel0: React.FC<CitiesDropdownModel0Props> = ({
  data,
  onSelect,
  className = '',
  dataUrl,
  country = 'IT',
  language = 'it',
  placeholder = 'Select location...',
  username,
  password,
  enableSearch = false,
  searchPlaceholder = 'Search location...',
  popup = false,
}) => {
  const { nodes, loading, error } = useCitiesData({
    data,
    dataUrl,
    country,
    language,
    username,
    password,
  });

  const { searchQuery, setSearchQuery, searchResults } = useSearch(enableSearch, nodes);

  const [selectedPath, setSelectedPath] = useState<Node[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const handleNodeClick = (node: Node, level: number) => {
    const newPath = selectedPath.slice(0, level);
    newPath[level] = node;
    setSelectedPath(newPath);

    if (!node.zones || node.zones.length === 0) {
      // Leaf node selected
      if (popup) {
        alert(`Selected: ${node.name} (ID: ${node.id})`);
      }
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
    if (selectedPath.length === 0) {
      if (Array.isArray(placeholder)) {
        return placeholder[0] || 'Select location...';
      }
      return placeholder || 'Select location...';
    }
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
        onClick={() => {
          const newIsOpen = !isOpen;
          setIsOpen(newIsOpen);
          if (!newIsOpen) {
            // Reset search when closing
            setSearchQuery('');
          }
        }}
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
          {enableSearch && (
            <div className="cities-dropdown-search">
              <input
                type="text"
                className="cities-dropdown-search-input"
                placeholder={searchPlaceholder}
                value={searchQuery}
                onChange={(e) => {
                  setSearchQuery(e.target.value);
                  if (e.target.value) {
                    setSelectedPath([]);
                  }
                }}
                onClick={(e) => e.stopPropagation()}
                onKeyDown={(e) => e.stopPropagation()}
              />
            </div>
          )}

          {enableSearch && searchQuery ? (
            // Show search results
            searchResults.length === 0 ? (
              <div className="cities-dropdown-empty">No results found</div>
            ) : (
              <ul className="cities-dropdown-list">
                {searchResults.map(({ node, path }) => (
                  <li
                    key={node.id}
                    className={`cities-dropdown-item ${
                      node.zones && node.zones.length > 0 ? 'has-children' : 'leaf'
                    }`}
                    role="option"
                    onClick={() => {
                      setSelectedPath(path);
                      setSearchQuery('');
                      if (!node.zones || node.zones.length === 0) {
                        // Leaf node - select and close
                        if (popup) {
                          alert(`Selected: ${node.name} (ID: ${node.id})`);
                        }
                        if (onSelect) {
                          onSelect(node);
                        }
                        setIsOpen(false);
                      }
                      // If node has children, keep dropdown open and navigate to that level
                    }}
                  >
                    <span className="cities-dropdown-item-name">
                      {path.map(n => n.name).join(' > ')}
                    </span>
                    {node.zones && node.zones.length > 0 && (
                      <span className="cities-dropdown-item-arrow">▶</span>
                    )}
                  </li>
                ))}
              </ul>
            )
          ) : (
            // Show normal hierarchical navigation
            <>
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
            </>
          )}
        </div>
      )}
    </div>
  );
};

