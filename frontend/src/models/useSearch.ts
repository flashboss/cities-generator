import { useState, useEffect } from 'react';
import { Node, Nodes } from '../types';

export const useSearch = (
  enableSearch: boolean,
  nodes: Nodes | null
) => {
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [searchResults, setSearchResults] = useState<Array<{ node: Node; path: Node[] }>>([]);

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

  return { searchQuery, setSearchQuery, searchResults };
};

