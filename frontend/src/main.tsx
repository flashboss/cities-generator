import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import { CitiesDropdown } from './CitiesDropdown';
import { DropdownConfigComponent } from './DropdownConfig';
import { DropdownConfig, CountryInfo } from './types';
import './index.css';

// Standalone demo app
const App: React.FC = () => {
  const [config, setConfig] = useState<DropdownConfig>({
    dataSource: 'local',
    localPath: '$HOME/cities-generator',
    startFromCountry: false,
  });

  const [selectedCountry, setSelectedCountry] = useState<string | null>(null);

  const handleSelect = (node: any) => {
    if (node) {
      console.log('Selected:', node);
      alert(`Selected: ${node.name} (ID: ${node.id})`);
    }
  };

  const loadCountries = async (): Promise<CountryInfo[]> => {
    try {
      let url = '/cities-generator/countries.json';
      
      if (config.dataSource === 'remote' && config.remoteUrl) {
        const baseUrl = config.remoteUrl.replace(/\/$/, '');
        url = `${baseUrl}/countries.json`;
      }

      const headers: HeadersInit = {
        'Content-Type': 'application/json',
      };

      if (config.dataSource === 'remote' && config.username && config.password) {
        const credentials = btoa(`${config.username}:${config.password}`);
        headers['Authorization'] = `Basic ${credentials}`;
      }

      const response = await fetch(url, { headers });
      
      if (!response.ok) {
        throw new Error(`Failed to load countries: ${response.statusText}`);
      }

      const data = await response.json();
      
      // Check if the response contains an error object
      if (data && typeof data === 'object' && 'error' in data) {
        const errorMessage = data.message || data.error || 'Failed to load countries';
        throw new Error(errorMessage);
      }
      
      // Ensure it's an array
      if (!Array.isArray(data)) {
        throw new Error('Invalid response: expected an array of countries');
      }
      
      const countries: CountryInfo[] = data;
      return countries;
    } catch (err) {
      console.error('Error loading countries:', err);
      throw err;
    }
  };

  const buildDataUrl = (): string | undefined => {
    if (config.dataSource === 'remote' && config.remoteUrl && selectedCountry) {
      const baseUrl = config.remoteUrl.replace(/\/$/, '');
      return `${baseUrl}/${selectedCountry}.json`;
    }
    return undefined; // Use default local path
  };

  return (
    <div style={{ padding: '40px', maxWidth: '1000px', margin: '0 auto' }}>
      <h1>Cities Generator - Dropdown Menu</h1>
      
      <DropdownConfigComponent
        config={config}
        onConfigChange={setConfig}
        onLoadCountries={loadCountries}
      />

      <div style={{ marginTop: '30px', marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
          Location:
        </label>
        <CitiesDropdown
          country={selectedCountry || 'it'}
          dataUrl={buildDataUrl()}
          placeholder={config.startFromCountry ? "Select a country first..." : "Select a location..."}
          onSelect={handleSelect}
          config={config}
          onCountrySelect={(code) => {
            setSelectedCountry(code);
            console.log('Country selected:', code);
          }}
          onLoadCountries={loadCountries}
        />
      </div>

      <div style={{ marginTop: '40px', padding: '20px', background: '#f5f5f5', borderRadius: '4px' }}>
        <h2>Usage Examples</h2>
        <h3>1. Web Component (Most Portable)</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<script src="./dist/cities-generator.umd.js"></script>
<cities-dropdown 
  country="it" 
  data-url="/path/to/it.json">
</cities-dropdown>`}
        </pre>

        <h3>2. React Component</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`import { CitiesDropdown } from 'cities-generator-frontend';

<CitiesDropdown
  country="it"
  dataUrl="/path/to/it.json"
  onSelect={(node) => console.log(node)}
/>`}
        </pre>

        <h3>3. Vanilla JavaScript</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<div id="cities-dropdown"></div>
<script>
  CitiesGenerator.render('#cities-dropdown', {
    country: 'it',
    onSelect: (node) => console.log(node)
  });
</script>`}
        </pre>
      </div>
    </div>
  );
};

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
