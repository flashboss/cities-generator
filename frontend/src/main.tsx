import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import { CitiesDropdown } from './CitiesDropdown';
import { DropdownConfigComponent } from './DropdownConfig';
import { DropdownConfig } from './types';
import './index.css';

// Default GitHub URL for cities data
const DEFAULT_GITHUB_URL = 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db';

// Default values
const DEFAULT_COUNTRY = 'IT';
const DEFAULT_LANGUAGE = 'it';
const DEFAULT_PLACEHOLDER = 'Select location...';
const DEFAULT_SEARCH_PLACEHOLDER = 'Search location...';
const DEFAULT_MODEL = 0;

// Standalone demo app
const App: React.FC = () => {
  const [config, setConfig] = useState<DropdownConfig>({
    dataUrl: DEFAULT_GITHUB_URL,
    country: DEFAULT_COUNTRY,
    language: DEFAULT_LANGUAGE,
  });

  const handleSelect = (node: any) => {
    if (node) {
      console.log('Selected:', node);
    }
  };


  return (
    <div style={{ padding: '40px', maxWidth: '1000px', margin: '0 auto' }}>
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
        <h3>1. React Component</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`import { CitiesDropdown } from 'cities-generator-frontend';

<CitiesDropdown${(config.country && config.country !== DEFAULT_COUNTRY) ? `\n  country="${config.country}"` : ''}${(config.language && config.language !== DEFAULT_LANGUAGE) ? `\n  language="${config.language}"` : ''}${(config.dataUrl && config.dataUrl !== DEFAULT_GITHUB_URL) ? `\n  dataUrl="${config.dataUrl}"` : ''}${config.username ? `\n  username="${config.username}"` : ''}${config.password ? `\n  password="${config.password}"` : ''}${(config.placeholder && config.placeholder !== DEFAULT_PLACEHOLDER) ? `\n  placeholder="${config.placeholder}"` : ''}${config.enableSearch === true ? `\n  enableSearch={true}` : ''}${(config.searchPlaceholder && config.searchPlaceholder !== DEFAULT_SEARCH_PLACEHOLDER) ? `\n  searchPlaceholder="${config.searchPlaceholder}"` : ''}${(config.model !== undefined && config.model !== DEFAULT_MODEL) ? `\n  model={${config.model}}` : ''}${config.popup === true ? `\n  popup={true}` : ''}
  onSelect={(node) => console.log(node)}
/>`}
        </pre>

        <h3>2. Web Component (Most Portable)</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<!-- Polyfill for process (required for React) -->
<script>
    if (typeof process === 'undefined') {
        window.process = {
            env: {
                NODE_ENV: 'production'
            }
        };
    }
</script>
    
<!-- React and ReactDOM -->
<script crossorigin src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script crossorigin src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>

<!-- cities-generator script -->
<script src="https://cdn.jsdelivr.net/gh/flashboss/cities-generator@master/frontend/dist/cities-generator.umd.js"></script>

<cities-dropdown${(config.country && config.country !== DEFAULT_COUNTRY) ? `\n  country="${config.country}"` : ''}${(config.language && config.language !== DEFAULT_LANGUAGE) ? `\n  language="${config.language}"` : ''}${(config.dataUrl && config.dataUrl !== DEFAULT_GITHUB_URL) ? `\n  data-url="${config.dataUrl}"` : ''}${config.username ? `\n  username="${config.username}"` : ''}${config.password ? `\n  password="${config.password}"` : ''}${(config.placeholder && config.placeholder !== DEFAULT_PLACEHOLDER) ? `\n  placeholder="${config.placeholder}"` : ''}${config.enableSearch === true ? '\n  enable-search' : ''}${(config.searchPlaceholder && config.searchPlaceholder !== DEFAULT_SEARCH_PLACEHOLDER) ? `\n  search-placeholder="${config.searchPlaceholder}"` : ''}${(config.model !== undefined && config.model !== DEFAULT_MODEL) ? `\n  model="${config.model}"` : ''}>
</cities-dropdown>`}
        </pre>

        <h3>3. Vanilla JavaScript</h3>
        <pre style={{ background: 'white', padding: '10px', borderRadius: '4px', overflow: 'auto' }}>
{`<!-- Polyfill for process (required for React) -->
<script>
    if (typeof process === 'undefined') {
        window.process = {
            env: {
                NODE_ENV: 'production'
            }
        };
    }
</script>

<!-- React and ReactDOM -->
<script crossorigin src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script crossorigin src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>

<!-- cities-generator script -->
<script src="https://cdn.jsdelivr.net/gh/flashboss/cities-generator@master/frontend/dist/cities-generator.umd.js"></script>

<div id="cities-dropdown"></div>
<script>
  CitiesGenerator.render('#cities-dropdown', {${(config.country && config.country !== DEFAULT_COUNTRY) ? `\n    country: '${config.country}',` : ''}${(config.language && config.language !== DEFAULT_LANGUAGE) ? `\n    language: '${config.language}',` : ''}${(config.dataUrl && config.dataUrl !== DEFAULT_GITHUB_URL) ? `\n    dataUrl: '${config.dataUrl}',` : ''}${config.username ? `\n    username: '${config.username}',` : ''}${config.password ? `\n    password: '${config.password}',` : ''}${(config.placeholder && config.placeholder !== DEFAULT_PLACEHOLDER) ? `\n    placeholder: '${config.placeholder}',` : ''}${config.enableSearch === true ? `\n    enableSearch: true,` : ''}${(config.searchPlaceholder && config.searchPlaceholder !== DEFAULT_SEARCH_PLACEHOLDER) ? `\n    searchPlaceholder: '${config.searchPlaceholder}',` : ''}${(config.model !== undefined && config.model !== DEFAULT_MODEL) ? `\n    model: ${config.model},` : ''}${config.popup === true ? `\n    popup: true,` : ''}
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
