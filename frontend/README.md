---
layout: guide
title: Cities Generator Frontend
permalink: /frontend/
---

# Cities Generator Frontend

A portable, hierarchical dropdown menu component for displaying cities data from cities-generator JSON files. The component is designed to work in any platform (WordPress, Drupal, Liferay, Joomla, etc.) with minimal integration effort.

## Features

- 🌳 **Hierarchical Navigation**: Navigate through nested location data
- 📦 **Highly Portable**: Works as Web Component, React component, or vanilla JS
- 🎨 **Customizable**: Easy to style and integrate
- 📱 **Responsive**: Works on desktop and mobile
- ♿ **Accessible**: ARIA labels and keyboard navigation

## Installation

Navigate to the frontend directory and install dependencies:

```bash
cd frontend
npm install
```

## Development

Start the development server:

```bash
npm run dev
```

## Build

Build all variants (UMD, Standalone, Web Component):

```bash
npm run build
```

Or build specific variants:

```bash
npm run build:umd          # UMD bundle (requires React external)
npm run build:standalone   # Standalone bundle (includes React)
npm run build:webcomponent # Web Component ES module
```

The build outputs are in the `dist/` directory:

- `cities-generator.umd.js` - UMD bundle (~18KB, requires React)
- `cities-generator-standalone.iife.js` - Standalone bundle (~52KB, includes React)
- `style.css` - Component styles

## Usage

### Method 1: Web Component (Most Portable - Recommended)

Works in **any** platform without framework dependencies. Simply include the scripts and use the custom HTML element:

```html
<!-- Polyfill for process (required for React) -->
<script>
    if (typeof process === 'undefined') {
        window.process = {
            env: {
                NODE_ENV: 'production'
            }
        };
    }
</script>

<!-- Load React (if not already loaded) -->
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>

<!-- Load Cities Generator -->
<script src="https://cdn.jsdelivr.net/gh/flashboss/cities-generator@master/frontend/dist/cities-generator.umd.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/flashboss/cities-generator@master/frontend/dist/style.css">

<!-- Use the component -->
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
</cities-dropdown>

<script>
  document.querySelector('cities-dropdown').addEventListener('select', (e) => {
    console.log('Selected:', e.detail);
    // e.detail contains: { id, name, level, zones }
  });
</script>
```

### Method 2: Standalone Bundle (Zero Dependencies)

Use when you can't include React separately. The standalone bundle includes React:

```html
<!-- Polyfill for process (required for React) -->
<script>
    if (typeof process === 'undefined') {
        window.process = {
            env: {
                NODE_ENV: 'production'
            }
        };
    }
</script>

<!-- Load standalone bundle (includes React) -->
<script src="https://cdn.jsdelivr.net/gh/flashboss/cities-generator@master/frontend/dist/cities-generator-standalone.iife.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/flashboss/cities-generator@master/frontend/dist/style.css">

<!-- Use via JavaScript API -->
<div id="my-dropdown"></div>
<script>
  // Using default parameters
  CitiesGenerator.render('#my-dropdown');

  // Using default URL
  CitiesGenerator.render('#my-dropdown', {
    country: 'IT',
    language: 'it',
    placeholder: 'Select location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
  
  // Using custom base URL (automatically appends /IT/it.json)
  CitiesGenerator.render('#my-dropdown', {
    country: 'IT',
    language: 'it',
    dataUrl: 'https://example.com/cities',
    placeholder: 'Select location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
  
  // Using search functionality
  CitiesGenerator.render('#my-dropdown', {
    country: 'IT',
    language: 'it',
    placeholder: 'Select location...',
    enableSearch: true,
    searchPlaceholder: 'Search location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
</script>
```

### Method 3: React Component

For React applications:

```tsx
import { CitiesDropdown } from 'cities-generator-frontend';

function MyComponent() {
  return (
    <>
      {/* Using default parameters */}
      <CitiesDropdown />

      {/* Using default URL */}
      <CitiesDropdown
        country="IT"
        language="it"
        placeholder="Select location..."
        onSelect={(node) => console.log(node)}
      />
      
      {/* Using custom base URL (automatically appends /IT/it.json) */}
      <CitiesDropdown
        country="IT"
        language="it"
        dataUrl="https://example.com/cities"
        placeholder="Select location..."
        onSelect={(node) => console.log(node)}
      />
      
      {/* Using search functionality */}
      <CitiesDropdown
        country="IT"
        language="it"
        placeholder="Select location..."
        enableSearch={true}
        searchPlaceholder="Search location..."
        onSelect={(node) => console.log(node)}
      />
    </>
  );
}
```

## Component Props

- `data` (Nodes, optional): Direct data object (optional)
- `onSelect` (function, optional): Callback when a leaf node is selected
- `className` (string, optional): Additional CSS classes
- `dataUrl` (string, optional): Base URL for remote data source. If not specified, uses default GitHub URL: <https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU/{country}/{language}.json>. If specified, treated as base URL and automatically appends `/{country}/{language}.json` (any `.json` extension in the URL is automatically removed)
- `country` (string, optional): Country code, e.g., "IT", "GB" (default: "IT")
- `language` (string, optional): Language code, e.g., "it", "en", "fr", "de", "es", "pt". Supported languages: IT (Italian, default), EN (English), FR (French), DE (German), ES (Spanish), PT (Portuguese). In Java API, you can use `Languages` enum: `Languages.IT`, `Languages.EN`, etc.
- `placeholder` (string, optional): Placeholder text (default: "Select location...")
- `username` (string, optional): Username for HTTP Basic Authentication
- `password` (string, optional): Password for HTTP Basic Authentication
- `enableSearch` (boolean, optional): Enable text search functionality (default: false)
- `searchPlaceholder` (string, optional): Placeholder text for the search input field (default: "Search location...")

## Events

When using the Web Component, listen for the `select` event:

```javascript
document.querySelector('cities-dropdown').addEventListener('select', (e) => {
  const node = e.detail; // { id, name, level, zones }
  console.log('Selected:', node.name, 'ID:', node.id);
});
```

## Platform Integration Examples

### WordPress

**1. Upload files to theme:**

- Upload `cities-generator.umd.js` to `wp-content/themes/your-theme/js/`
- Upload JSON files to `wp-content/themes/your-theme/data/`

**2. Enqueue scripts in `functions.php`:**

```php
function enqueue_cities_dropdown() {
    wp_enqueue_script('react', 'https://unpkg.com/react@18/umd/react.production.min.js', [], '18.2.0', true);
    wp_enqueue_script('react-dom', 'https://unpkg.com/react-dom@18/umd/react-dom.production.min.js', ['react'], '18.2.0', true);
    wp_enqueue_script('cities-generator', get_template_directory_uri() . '/js/cities-generator.umd.js', ['react', 'react-dom'], '1.0.0', true);
    wp_enqueue_style('cities-generator', get_template_directory_uri() . '/js/style.css', [], '1.0.0');
}
add_action('wp_enqueue_scripts', 'enqueue_cities_dropdown');
```

**3. Use in template:**

```php
<!-- Using default parameters -->
<cities-dropdown />

<!-- Using default URL -->
<cities-dropdown country="IT" language="it"></cities-dropdown>

<!-- Using custom base URL (automatically appends /IT/it.json) -->
<cities-dropdown 
  country="IT" 
  language="it"
  data-url="<?php echo get_template_directory_uri(); ?>/data">
</cities-dropdown>
```

### Drupal

**1. Create module or add to theme:**

In `your_theme.libraries.yml`:

```yaml
cities_dropdown:
  js:
    https://unpkg.com/react@18/umd/react.production.min.js: { type: external, minified: true }
    https://unpkg.com/react-dom@18/umd/react-dom.production.min.js: { type: external, minified: true, dependencies: [react] }
    js/cities-generator.umd.js: {}
  css:
    theme:
      js/style.css: {}
  dependencies:
    - core/drupal
```

**2. Attach to template:**

```twig
{{ attach_library('your_theme/cities_dropdown') }}

{# Using default parameters #}
<cities-dropdown />

{# Using default URL #}
<cities-dropdown country="IT" language="it"></cities-dropdown>

{# Using custom base URL (automatically appends /IT/it.json) #}
<cities-dropdown country="IT" language="it" data-url="/sites/default/files/cities"></cities-dropdown>
```

### Liferay

**1. Add to module or theme:**

In `liferay-plugin-package.properties`:

```properties
js.fast.load=true
```

**2. Include in JSP:**

```jsp
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
<script src="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/js/cities-generator.umd.js"></script>
<link rel="stylesheet" href="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/js/style.css">

<%-- Using default parameters --%>
<cities-dropdown />

<%-- Using default URL --%>
<cities-dropdown country="IT" language="it"></cities-dropdown>

<%-- Using custom base URL (automatically appends /IT/it.json) --%>
<cities-dropdown country="IT" language="it" data-url="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/data"></cities-dropdown>
```

### Joomla

**1. Add to template:**

In `index.php`:

```php
$document = JFactory::getDocument();
$document->addScript('https://unpkg.com/react@18/umd/react.production.min.js');
$document->addScript('https://unpkg.com/react-dom@18/umd/react-dom.production.min.js');
$document->addScript(JURI::root() . 'templates/your-template/js/cities-generator.umd.js');
$document->addStyleSheet(JURI::root() . 'templates/your-template/js/style.css');
```

**2. Use in template:**

```php
<!-- Using default parameters -->
<cities-dropdown />

<!-- Using default URL -->
<cities-dropdown country="IT" language="it"></cities-dropdown>

<!-- Using custom base URL (automatically appends /IT/it.json) -->
<cities-dropdown country="IT" language="it" data-url="<?php echo JURI::root(); ?>data"></cities-dropdown>
```

## Data Format

The component expects JSON in this format (matching the cities-generator output):

```json
{
  "zones": [
    {
      "id": "1",
      "name": "I: ITALIA NORD-OCCIDENTALE",
      "level": 0,
      "zones": [
        {
          "id": "1-12345",
          "name": "Piemonte",
          "level": 1,
          "zones": [
            {
              "id": "1-12345-67890",
              "name": "Torino",
              "level": 2,
              "zones": []
            }
          ]
        }
      ]
    }
  ]
}
```

## Styling

The component uses CSS classes prefixed with `cities-dropdown-`. You can override styles:

```css
.cities-dropdown-trigger {
  border-color: #your-color;
  border-radius: 8px;
}

.cities-dropdown-item:hover {
  background-color: #your-hover-color;
}
```

## NPM Package

This package is also available on npm:

```bash
npm install cities-generator-frontend
```

See the [npm package page](https://www.npmjs.com/package/cities-generator-frontend) for more information.

