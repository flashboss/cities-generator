import React, { useState, useEffect } from 'react';
import { Node, Nodes } from '../types';
import { useCitiesData } from './useCitiesData';
import './CitiesDropdownModel1.css';

interface CitiesDropdownModel1Props {
  data?: Nodes;
  onSelect?: (node: Node) => void;
  className?: string;
  dataUrl?: string;
  country?: string;
  language?: string;
  placeholder?: string | string[];
  username?: string;
  password?: string;
  popup?: boolean;
}

export const CitiesDropdownModel1: React.FC<CitiesDropdownModel1Props> = ({
  data,
  onSelect,
  className = '',
  dataUrl,
  country = 'IT',
  language = 'it',
  placeholder = 'Select location...',
  username,
  password,
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

  // For model 1: array of selected nodes per level
  const [cascadingSelections, setCascadingSelections] = useState<Node[]>([]);

  // Reset cascading selections when nodes change
  useEffect(() => {
    if (nodes) {
      setCascadingSelections([]);
    }
  }, [nodes]);

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

  // Get placeholder text for a specific level
  const getPlaceholderForLevel = (level: number): string => {
    if (Array.isArray(placeholder) && placeholder[level]) {
      return placeholder[level];
    }
    return `Select level ${level}...`;
  };

  if (loading && !nodes) {
    return <div className={`cities-dropdown loading ${className}`}>Loading...</div>;
  }

  if (error && !nodes) {
    return <div className={`cities-dropdown error ${className}`}>Error: {error}</div>;
  }

  if (!nodes) {
    return <div className={`cities-dropdown loading ${className}`}>Loading...</div>;
  }

  const levelsToShow = getLevelsToShow();

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
};

