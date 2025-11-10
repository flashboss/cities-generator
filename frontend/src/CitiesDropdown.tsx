import React, { useState, useEffect, useRef } from 'react';
import { Node, Nodes, DropdownConfig } from './types';
import './CitiesDropdown.css';

interface CitiesDropdownProps extends DropdownConfig {
  data?: Nodes;
  onSelect?: (node: Node) => void;
  className?: string;
}

export const CitiesDropdown: React.FC<CitiesDropdownProps> = ({
  data,
  onSelect,
  className = '',
  dataUrl,
  country = 'IT',
  placeholder = 'Select location...',
  username,
  password,
  enableSearch = false,
  searchPlaceholder = 'Search location...',
}) => {
  const [nodes, setNodes] = useState<Nodes | null>(data || null);
  const [selectedPath, setSelectedPath] = useState<Node[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [searchResults, setSearchResults] = useState<Array<{ node: Node; path: Node[] }>>([]);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (data) {
      setNodes(data);
      return;
    }

    if (country) {
      // Reset state when URL or country changes
      setSelectedPath([]);
      setError(null);
      setNodes(null);
      
      // Build URL
      const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU';
      const baseUrl = (dataUrl || DEFAULT_GITHUB_URL).replace(/\.json$/, '').replace(/\/$/, '');
      const url = `${baseUrl}/${country}.json`;
      
      // Load data with current config
      const loadData = async () => {
        setLoading(true);
        setError(null);

        try {
          const headers: HeadersInit = {};

          // Add authentication if configured
          if (username && password) {
            const credentials = btoa(`${username}:${password}`);
            headers['Authorization'] = `Basic ${credentials}`;
          }

          const response = await fetch(url, { headers });

          if (!response.ok) {
            throw new Error(`Failed to load data: ${response.status} ${response.statusText}`);
          }

          const jsonData: Nodes = await response.json();
          setNodes(jsonData);
        } catch (err) {
          const errorMessage = err instanceof Error ? err.message : 'Failed to load country data';
          setError(errorMessage);
          setNodes(null);
        } finally {
          setLoading(false);
        }
      };
      
      loadData();
    }
  }, [data, country, dataUrl, username, password]);

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

  // Search function that recursively searches all nodes
  const searchNodes = (query: string, nodeList: Node[], currentPath: Node[] = []): Array<{ node: Node; path: Node[] }> => {
    if (!query || !nodeList) return [];
    
    const results: Array<{ node: Node; path: Node[] }> = [];
    const lowerQuery = query.toLowerCase();
    
    for (const node of nodeList) {
      const newPath = [...currentPath, node];
      
      // Check if node name matches
      if (node.name.toLowerCase().includes(lowerQuery)) {
        results.push({ node, path: newPath });
      }
      
      // Recursively search children
      if (node.zones && node.zones.length > 0) {
        results.push(...searchNodes(query, node.zones, newPath));
      }
    }
    
    return results;
  };

  // Update search results when search query changes
  useEffect(() => {
    if (enableSearch && searchQuery && nodes) {
      const results = searchNodes(searchQuery, nodes.zones);
      setSearchResults(results);
    } else {
      setSearchResults([]);
      if (!enableSearch) {
        setSearchQuery('');
      }
    }
  }, [searchQuery, nodes, enableSearch]);

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
