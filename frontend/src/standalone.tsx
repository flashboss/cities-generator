// Standalone bundle entry point for UMD/IIFE builds
// For UMD: React is external and will be available as global variable
// For IIFE: React is bundled and imported directly
// @ts-ignore - React may be external (UMD) or bundled (IIFE)
import React from 'react';
// @ts-ignore - ReactDOM may be external (UMD) or bundled (IIFE)
import ReactDOM from 'react-dom/client';
import { CitiesDropdown } from './CitiesDropdown';
import type { Nodes } from './types';

// Import all CSS files to include them in the bundle
import './index.css';
import './CitiesDropdown.css';
import './models/CitiesDropdownModel0.css';
import './models/CitiesDropdownModel1.css';

// Polyfill for process (required for React in browser environments)
if (typeof window !== 'undefined' && typeof (window as any).process === 'undefined') {
  (window as any).process = {
    env: {
      NODE_ENV: 'production'
    }
  };
}

// Get React - works for both UMD (external) and IIFE (bundled)
// When React is external (UMD), Vite replaces the import with the global variable
// When React is bundled (IIFE), the import works normally
const getReact = () => {
  // Try imported React first (works for IIFE)
  // @ts-ignore
  if (typeof React !== 'undefined' && typeof ReactDOM !== 'undefined') {
    return { React, ReactDOM };
  }
  // Fallback to window (works for UMD where React is external)
  if (typeof window !== 'undefined' && (window as any).React && (window as any).ReactDOM) {
    return { React: (window as any).React, ReactDOM: (window as any).ReactDOM };
  }
  return null;
};

const reactLibs = getReact();

// Expose React and ReactDOM on window for web component wrapper
// This works for both UMD (where React is external) and IIFE standalone (where React is bundled)
if (reactLibs && typeof window !== 'undefined') {
  (window as any).React = reactLibs.React;
  (window as any).ReactDOM = reactLibs.ReactDOM;
}

// Export for global use
(window as any).CitiesGenerator = {
  CitiesDropdown,
  render: (container: string | HTMLElement, props: any) => {
    const reactLibs = getReact();
    
    if (!reactLibs) {
      console.error('React and ReactDOM must be loaded before CitiesGenerator.render()');
      return;
    }

    const element = typeof container === 'string' 
      ? document.querySelector(container)
      : container;

    if (!element) {
      console.error('Container element not found');
      return;
    }

    const root = reactLibs.ReactDOM.createRoot(element);
    root.render(reactLibs.React.createElement(CitiesDropdown, props));
  },
};

export { CitiesDropdown };

// Auto-register web component if customElements is available
// Import directly for IIFE standalone bundle (React is already bundled)
if (typeof window !== 'undefined' && window.customElements) {
  import('./web-component-wrapper').catch(() => {
    // Web component registration failed
  });
}

