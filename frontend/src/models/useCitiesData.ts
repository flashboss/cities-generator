import { useState, useEffect } from 'react';
import { Nodes } from '../types';
import { getContinentFromCountry } from '../continentUtils';

interface UseCitiesDataProps {
  data?: Nodes;
  dataUrl?: string;
  country?: string;
  language?: string;
  username?: string;
  password?: string;
}

export const useCitiesData = ({
  data,
  dataUrl,
  country,
  language,
  username,
  password,
}: UseCitiesDataProps) => {
  const [nodes, setNodes] = useState<Nodes | null>(data || null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (data) {
      setNodes(data);
      return;
    }

    if (country) {
      // Reset state when URL, country, or language changes
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

  return { nodes, loading, error };
};

