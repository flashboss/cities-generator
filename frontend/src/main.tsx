import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import { CitiesDropdown } from './CitiesDropdown';
import { DropdownConfigComponent } from './DropdownConfig';
import { DropdownConfig } from './types';
import './index.css';

// Default GitHub URL for cities data
const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/europe';

// Standalone demo app
const App: React.FC = () => {
  const [config, setConfig] = useState<DropdownConfig>({
    remoteUrl: DEFAULT_GITHUB_URL,
  });

  const handleSelect = (node: any) => {
    if (node) {
      console.log('Selected:', node);
      alert(`Selected: ${node.name} (ID: ${node.id})`);
    }
  };


  return (
    <div style={{ padding: '40px', maxWidth: '1000px', margin: '0 auto' }}>
      <h1>Cities Generator - Dropdown Menu</h1>
      
      <DropdownConfigComponent
        config={config}
        onConfigChange={setConfig}
      />

      <div style={{ marginTop: '30px', marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
          Location:
        </label>
        <CitiesDropdown
          country="it"
          placeholder={config.placeholder || "Select a location..."}
          onSelect={handleSelect}
          config={config}
        />
      </div>

      <div style={{ marginTop: '40px', padding: '20px', background: '#f5f5f5', borderRadius: '4px' }}>
        <h2>Usage Examples</h2>
        <h3>1. Web Component (Most Portable)</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<script src="./dist/cities-generator.umd.js"></script>

<!-- Using default URL (no data-url needed) -->
<cities-dropdown 
  country="it" 
  placeholder="Select location...">
</cities-dropdown>

<!-- Using custom base URL (automatically appends /it.json) -->
<cities-dropdown 
  country="it" 
  data-url="https://example.com/cities"
  placeholder="Select location...">
</cities-dropdown>`}
        </pre>

        <h3>2. React Component</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`import { CitiesDropdown } from 'cities-generator-frontend';

// Using default URL
<CitiesDropdown
  country="it"
  placeholder="Select location..."
  onSelect={(node) => console.log(node)}
/>

// Using custom base URL (automatically appends /it.json)
<CitiesDropdown
  country="it"
  dataUrl="https://example.com/cities"
  placeholder="Select location..."
  onSelect={(node) => console.log(node)}
/>`}
        </pre>

        <h3>3. Vanilla JavaScript</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<div id="cities-dropdown"></div>
<script>
  CitiesGenerator.render('#cities-dropdown', {
    country: 'it',
    placeholder: 'Select location...',
    // dataUrl: 'https://example.com/cities', // Optional: base URL
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
