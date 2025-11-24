import React, { useState, useEffect, useRef } from 'react';
import { Node, Nodes, DropdownConfig } from './types';
import { getContinentFromCountry } from './continentUtils';
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
  language = 'it',
  placeholder = 'Select location...',
  username,
  password,
  enableSearch = false,
  searchPlaceholder = 'Search location...',
  model = 0, // Template model: 0 = default template, 1+ = alternative templates (to be implemented)
  popup = false, // Show popup (alert) on final selection
}) => {
  // Model 1: Cascading dropdowns - each level has its own dropdown
  const [nodes, setNodes] = useState<Nodes | null>(data || null);
  const [selectedPath, setSelectedPath] = useState<Node[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [searchResults, setSearchResults] = useState<Array<{ node: Node; path: Node[] }>>([]);
  const dropdownRef = useRef<HTMLDivElement>(null);
  
  // For model 1: array of selected nodes per level
  const [cascadingSelections, setCascadingSelections] = useState<Node[]>([]);

  useEffect(() => {
    if (data) {
      setNodes(data);
      return;
    }

    if (country) {
      // Reset state when URL, country, or language changes
      setSelectedPath([]);
      setError(null);
      setNodes(null);
      
      // Build URL: format is {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)
      const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db';
      const baseUrl = (dataUrl || DEFAULT_GITHUB_URL).replace(/\.json$/, '').replace(/\/$/, '');
      const lang = (language || 'it').toLowerCase();
      const countryCode = country.toUpperCase();
      // Determine continent from country code
      const continent = getContinentFromCountry(countryCode);
      const url = `${baseUrl}/${continent}/${countryCode}/${lang}.json`;
      
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
  }, [data, country, language, dataUrl, username, password]);

  // Reset cascading selections when nodes change
  useEffect(() => {
    if (model === 1 && nodes) {
      setCascadingSelections([]);
    }
  }, [nodes, model]);

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

  // For model 1: Get nodes for a specific level based on cascading selections
  const getLevelNodes = (level: number): Node[] => {
    if (!nodes) return [];
    
    if (level === 0) {
      return nodes.zones;
    }
    
    // Navigate through the hierarchy based on selections
    let current: Node | undefined = cascadingSelections[level - 1];
    if (!current) return [];
    
    return current.zones || [];
  };

  // For model 1: Handle selection change at a specific level
  const handleCascadingSelection = (level: number, node: Node | null) => {
    if (!node) {
      // Clear this level and all levels below
      setCascadingSelections(prev => prev.slice(0, level));
      return;
    }

    const newSelections = [...cascadingSelections];
    newSelections[level] = node;
    
    // If changing a level, clear all levels below
    if (level < newSelections.length) {
      newSelections.splice(level + 1);
    }
    
    // Auto-select first item in next level if available
    if (node.zones && node.zones.length > 0) {
      const firstChild = node.zones[0];
      newSelections[level + 1] = firstChild;
      
      // Recursively auto-select first items in deeper levels
      let current = firstChild;
      let nextLevel = level + 2;
      while (current.zones && current.zones.length > 0) {
        newSelections[nextLevel] = current.zones[0];
        current = current.zones[0];
        nextLevel++;
      }
    }
    
    setCascadingSelections(newSelections);
    
    // Call onSelect with the deepest selected node
    const deepestNode = newSelections[newSelections.length - 1];
    if (deepestNode && (!deepestNode.zones || deepestNode.zones.length === 0)) {
      if (popup) {
        alert(`Selected: ${deepestNode.name} (ID: ${deepestNode.id})`);
      }
      if (onSelect) {
        onSelect(deepestNode);
      }
    }
  };

  // For model 1: Get the maximum depth of the tree
  const getMaxDepth = (nodeList: Node[], currentDepth: number = 0): number => {
    if (!nodeList || nodeList.length === 0) return currentDepth;
    
    let maxDepth = currentDepth;
    for (const node of nodeList) {
      if (node.zones && node.zones.length > 0) {
        const depth = getMaxDepth(node.zones, currentDepth + 1);
        maxDepth = Math.max(maxDepth, depth);
      }
    }
    return maxDepth;
  };

  const getDisplayText = (): string => {
    if (selectedPath.length === 0) {
      if (Array.isArray(placeholder)) {
        return placeholder[0] || 'Select location...';
      }
      return placeholder;
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

  // Model 1: Cascading dropdowns
  if (model === 1) {
    if (!nodes) {
      return <div className={`cities-dropdown loading ${className}`}>Loading...</div>;
    }

    // Determine how many levels to show based on selections
    // Always show at least level 0, and show next level if current level has a selection with children
    const getLevelsToShow = (): number => {
      let levels = 1; // Always show level 0
      
      for (let i = 0; i < cascadingSelections.length; i++) {
        const selected = cascadingSelections[i];
        if (selected && selected.zones && selected.zones.length > 0) {
          levels = i + 2; // Show next level
        } else {
          break; // Stop if we hit a leaf node
        }
      }
      
      return levels;
    };

    const levelsToShow = getLevelsToShow();

    // Get placeholder text for a specific level
    const getPlaceholderForLevel = (level: number): string => {
      if (Array.isArray(placeholder) && placeholder[level]) {
        return placeholder[level];
      }
      return `Select level ${level}...`;
    };

    return (
      <div className={`cities-dropdown cities-dropdown-cascading ${className}`}>
        {Array.from({ length: levelsToShow }, (_, level) => {
          const levelNodes = getLevelNodes(level);
          const selectedNode = cascadingSelections[level];
          const isDisabled = level > 0 && !cascadingSelections[level - 1];
          const levelPlaceholder = getPlaceholderForLevel(level);

          return (
            <div key={`${level}-${levelPlaceholder}`} className="cities-dropdown-cascading-level">
              <label className="cities-dropdown-cascading-label">
                Level {level}:
              </label>
              <select
                key={`select-${level}-${levelPlaceholder}`}
                className="cities-dropdown-cascading-select"
                value={selectedNode?.id || ''}
                onChange={(e) => {
                  const selectedId = e.target.value;
                  if (!selectedId) {
                    handleCascadingSelection(level, null);
                    return;
                  }
                  const node = levelNodes.find(n => n.id === selectedId);
                  if (node) {
                    handleCascadingSelection(level, node);
                  }
                }}
                disabled={isDisabled || levelNodes.length === 0}
              >
                <option value="">
                  {isDisabled ? 'Select previous level first' : levelNodes.length === 0 ? 'No options' : levelPlaceholder}
                </option>
                {levelNodes.map((node) => (
                  <option key={node.id} value={node.id}>
                    {node.name}
                  </option>
                ))}
              </select>
            </div>
          );
        })}
      </div>
    );
  }

  // Model 0: Default template (current implementation)
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
