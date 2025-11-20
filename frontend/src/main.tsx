import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import { CitiesDropdown } from './CitiesDropdown';
import { DropdownConfigComponent } from './DropdownConfig';
import { DropdownConfig } from './types';
import './index.css';

// Default GitHub URL for cities data
const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db';

// Standalone demo app
const App: React.FC = () => {
  const [config, setConfig] = useState<DropdownConfig>({
    dataUrl: DEFAULT_GITHUB_URL,
    country: 'IT',
    language: 'it',
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
          onSelect={handleSelect}
          {...config}
        />
      </div>

      <div style={{ marginTop: '40px', padding: '20px', background: '#f5f5f5', borderRadius: '4px' }}>
        <h2>Usage Examples</h2>
        <h3>1. Web Component (Most Portable)</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<script src="./dist/cities-generator.umd.js"></script>

<!-- Using default parameters -->
<cities-dropdown />

<!-- Using default URL -->
<cities-dropdown 
  country="IT" 
  language="it"
  placeholder="Select location...">
</cities-dropdown>

<!-- Using custom base URL (automatically appends /IT/it.json) -->
<cities-dropdown 
  country="IT" 
  language="it"
  data-url="https://example.com/cities"
  placeholder="Select location...">
</cities-dropdown>`}
        </pre>

        <h3>2. React Component</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`import { CitiesDropdown } from 'cities-generator-frontend';

// Using default parameters
<CitiesDropdown />

// Using default URL
<CitiesDropdown
  country="IT"
  language="it"
  placeholder="Select location..."
  onSelect={(node) => console.log(node)}
/>

// Using custom base URL (automatically appends /IT/it.json)
<CitiesDropdown
  country="IT"
  language="it"
  dataUrl="https://example.com/cities"
  placeholder="Select location..."
  onSelect={(node) => console.log(node)}
/>

// Using search functionality
<CitiesDropdown
  country="IT"
  language="it"
  placeholder="Select location..."
  enableSearch={true}
  searchPlaceholder="Search location..."
  onSelect={(node) => console.log(node)}
/>`}
        </pre>

        <h3>3. Vanilla JavaScript</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<div id="cities-dropdown"></div>
<script>

  // Using default parameters
  CitiesGenerator.render('#cities-dropdown');

  // Using default URL
  CitiesGenerator.render('#cities-dropdown', {
    country: 'IT',
    language: 'it',
    placeholder: 'Select location...',
    onSelect: (node) => console.log(node)
  });

  // Using custom base URL (automatically appends /IT/it.json)
  CitiesGenerator.render('#cities-dropdown', {
    country: 'IT',
    language: 'it',
    dataUrl: 'https://example.com/cities',
    placeholder: 'Select location...',
    onSelect: (node) => console.log(node)
  });

  // Using search functionality
  CitiesGenerator.render('#cities-dropdown', {
    country: 'IT',
    language: 'it',
    placeholder: 'Select location...',
    enableSearch: true,
    searchPlaceholder: 'Search location...',
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
