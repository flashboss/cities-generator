# cities-generator-frontend

Portable hierarchical dropdown menu component for cities-generator JSON data. Works with React, vanilla JavaScript, and as a Web Component.

## Installation

```bash
npm install cities-generator-frontend
```

## Usage

### React Component

```tsx
import { CitiesDropdown } from 'cities-generator-frontend';
import 'cities-generator-frontend/src/CitiesDropdown.css';

function App() {
  return (
    <CitiesDropdown
      country="IT"
      language="it"
      dataUrl="https://raw.githubusercontent.com/flashboss/cities-generator/master/_db"
      onSelect={(node) => console.log('Selected:', node)}
    />
  );
}
```

### Web Component

```html
<!DOCTYPE html>
<html>
<head>
  <script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
  <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
  <script src="https://unpkg.com/cities-generator-frontend/dist/cities-generator-wc.js"></script>
</head>
<body>
  <cities-dropdown 
    country="IT" 
    language="it"
    data-url="https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU"
  ></cities-dropdown>
  
  <script>
    document.querySelector('cities-dropdown').addEventListener('select', (e) => {
      console.log('Selected:', e.detail);
    });
  </script>
</body>
</html>
```

### Standalone Bundle (IIFE)

```html
<!DOCTYPE html>
<html>
<head>
  <script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
  <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
  <script src="https://unpkg.com/cities-generator-frontend/dist/cities-generator-standalone.iife.js"></script>
</head>
<body>
  <div id="cities-dropdown"></div>
  
  <script>
    CitiesGenerator.render('#cities-dropdown', {
      country: 'IT',
      language: 'it',
      dataUrl: 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU',
      onSelect: (node) => console.log('Selected:', node)
    });
  </script>
</body>
</html>
```

### UMD Bundle

```html
<!DOCTYPE html>
<html>
<head>
  <script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
  <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
  <script src="https://unpkg.com/cities-generator-frontend/dist/cities-generator.umd.js"></script>
</head>
<body>
  <div id="cities-dropdown"></div>
  
  <script>
    const { CitiesDropdown } = CitiesGenerator;
    const React = window.React;
    const ReactDOM = window.ReactDOM;
    
    const root = ReactDOM.createRoot(document.getElementById('cities-dropdown'));
    root.render(React.createElement(CitiesDropdown, {
      country: 'IT',
      language: 'it',
      dataUrl: 'https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU',
      onSelect: (node) => console.log('Selected:', node)
    }));
  </script>
</body>
</html>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `country` | `string` | `'IT'` | Country code (e.g., 'IT', 'GB') |
| `language` | `string` | `'it'` | Language code (e.g., 'it', 'en', 'fr', 'de', 'es', 'pt') |
| `dataUrl` | `string` | GitHub URL | Base URL for JSON data files |
| `data` | `Nodes` | `undefined` | Pre-loaded data (optional) |
| `placeholder` | `string` | `'Select location...'` | Placeholder text |
| `enableSearch` | `boolean` | `false` | Enable search functionality |
| `searchPlaceholder` | `string` | `'Search location...'` | Search placeholder text |
| `username` | `string` | `undefined` | Username for authenticated requests |
| `password` | `string` | `undefined` | Password for authenticated requests |
| `onSelect` | `(node: Node) => void` | `undefined` | Callback when a location is selected |
| `className` | `string` | `''` | Additional CSS classes |

## Web Component Attributes

When using the Web Component, use kebab-case for attributes:

- `country` → `country`
- `language` → `language`
- `data-url` → `data-url`
- `placeholder` → `placeholder`

## Data Format

The component expects JSON data in the following format:

```json
{
  "copyright": "...",
  "nodes": [
    {
      "name": "Region Name",
      "children": [
        {
          "name": "Province Name",
          "children": [
            {
              "name": "City Name"
            }
          ]
        }
      ]
    }
  ]
}
```

## Supported Countries

- `IT` - Italy
- `GB` - Great Britain

## Supported Languages

- `it` - Italian (default)
- `en` - English
- `fr` - French
- `de` - German
- `es` - Spanish
- `pt` - Portuguese

## License

Apache License 2.0

## Links

- [GitHub Repository](https://github.com/flashboss/cities-generator)
- [Documentation](https://github.com/flashboss/cities-generator#readme)

