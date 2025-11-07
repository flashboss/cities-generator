# Integration Guide - Cities Generator Dropdown

This guide shows how to integrate the Cities Generator dropdown component into various platforms.

## Build the Component

First, build all variants:

```bash
cd frontend
npm install
npm run build
```

This creates three builds in `dist/`:
- `cities-generator.umd.js` - Requires React external (smaller, ~50KB)
- `cities-generator-standalone.iife.js` - Includes React (larger, ~150KB)
- `cities-generator-wc.es.js` - ES module for Web Components

## Integration Methods

### Method 1: Web Component (Recommended - Most Portable)

Works in **ANY** platform without framework dependencies.

**HTML:**
```html
<!-- Load React (if not already loaded) -->
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>

<!-- Load Cities Generator -->
<script src="./dist/cities-generator.umd.js"></script>

<!-- Use the component -->
<cities-dropdown 
  country="it" 
  data-url="/path/to/it.json"
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

Use when you can't include React separately.

**HTML:**
```html
<!-- Load standalone bundle (includes React) -->
<script src="./dist/cities-generator-standalone.iife.js"></script>

<!-- Use via JavaScript API -->
<div id="my-dropdown"></div>
<script>
  CitiesGenerator.render('#my-dropdown', {
    country: 'it',
    dataUrl: '/path/to/it.json',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
</script>
```

### Method 3: React Component

For React applications.

```tsx
import { CitiesDropdown } from './CitiesDropdown';

function MyComponent() {
  return (
    <CitiesDropdown
      country="it"
      dataUrl="/path/to/it.json"
      onSelect={(node) => console.log(node)}
    />
  );
}
```

## Platform-Specific Examples

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
}
add_action('wp_enqueue_scripts', 'enqueue_cities_dropdown');
```

**3. Use in template:**
```php
<cities-dropdown 
  country="it" 
  data-url="<?php echo get_template_directory_uri(); ?>/data/it.json">
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
  dependencies:
    - core/drupal
```

**2. Attach to template:**
```twig
{{ attach_library('your_theme/cities_dropdown') }}

<cities-dropdown country="it" data-url="/sites/default/files/cities/it.json"></cities-dropdown>
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

<cities-dropdown country="it" data-url="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/data/it.json"></cities-dropdown>
```

### Joomla

**1. Add to template:**

In `index.php`:
```php
$document = JFactory::getDocument();
$document->addScript('https://unpkg.com/react@18/umd/react.production.min.js');
$document->addScript('https://unpkg.com/react-dom@18/umd/react-dom.production.min.js');
$document->addScript(JURI::root() . 'templates/your-template/js/cities-generator.umd.js');
```

**2. Use in template:**
```php
<cities-dropdown country="it" data-url="<?php echo JURI::root(); ?>data/it.json"></cities-dropdown>
```

### Vanilla HTML/Any Platform

**Simple HTML page:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>Cities Dropdown</title>
    <script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
    <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
    <script src="./dist/cities-generator.umd.js"></script>
</head>
<body>
    <h1>Select Location</h1>
    <cities-dropdown country="it" data-url="./data/it.json"></cities-dropdown>
    
    <script>
        document.querySelector('cities-dropdown').addEventListener('select', (e) => {
            alert('Selected: ' + e.detail.name);
        });
    </script>
</body>
</html>
```

## Styling Customization

Override CSS classes:

```css
.cities-dropdown-trigger {
    border: 2px solid #your-color;
    border-radius: 8px;
}

.cities-dropdown-item:hover {
    background-color: #your-hover-color;
}
```

## API Reference

### Web Component Attributes

- `country` (string): Country code, e.g., "it", "uk"
- `data-url` (string): URL to JSON file
- `placeholder` (string): Placeholder text

### Events

- `select`: Fired when a leaf node is selected
  - `event.detail`: Selected node object `{ id, name, level, zones }`

### JavaScript API

```javascript
CitiesGenerator.render(container, {
  country: 'it',
  dataUrl: '/path/to/it.json',
  placeholder: 'Select...',
  onSelect: (node) => console.log(node)
});
```

