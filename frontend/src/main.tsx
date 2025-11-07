import React from 'react';
import ReactDOM from 'react-dom/client';
import { CitiesDropdown } from './CitiesDropdown';
import './index.css';

// Standalone demo app
const App: React.FC = () => {
  const handleSelect = (node: any) => {
    if (node) {
      console.log('Selected:', node);
      alert(`Selected: ${node.name} (ID: ${node.id})`);
    }
  };

  return (
    <div style={{ padding: '40px', maxWidth: '800px', margin: '0 auto' }}>
      <h1>Cities Generator - Dropdown Menu</h1>
      <p>Select a location from the hierarchical menu:</p>
      
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
          Location:
        </label>
        <CitiesDropdown
          country="it"
          placeholder="Select a location..."
          onSelect={handleSelect}
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

