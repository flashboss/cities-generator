// Standalone bundle entry point for UMD/IIFE builds
import { CitiesDropdown } from './CitiesDropdown';
import type { Nodes } from './types';

// Export for global use
(window as any).CitiesGenerator = {
  CitiesDropdown,
  render: (container: string | HTMLElement, props: any) => {
    const React = (window as any).React;
    const ReactDOM = (window as any).ReactDOM;
    
    if (!React || !ReactDOM) {
      console.error('React and ReactDOM must be loaded before CitiesGenerator');
      return;
    }

    const element = typeof container === 'string' 
      ? document.querySelector(container)
      : container;

    if (!element) {
      console.error('Container element not found');
      return;
    }

    const root = ReactDOM.createRoot(element);
    root.render(React.createElement(CitiesDropdown, props));
  },
};

export { CitiesDropdown };

// Auto-register web component if customElements is available
if (typeof window !== 'undefined' && window.customElements) {
  // Register web component when React is available
  const registerWebComponent = () => {
    if ((window as any).React && (window as any).ReactDOM) {
      import('./web-component-wrapper').catch(() => {
        // Web component registration will happen when React loads
      });
    }
  };
  
  // Try immediately
  registerWebComponent();
  
  // Also try after a short delay (in case React loads asynchronously)
  setTimeout(registerWebComponent, 100);
}

