// Simplified Web Component wrapper that works with bundled React
// This will be used when React is available globally

declare global {
  interface Window {
    React: any;
    ReactDOM: any;
  }
}

class CitiesDropdownElement extends HTMLElement {
  private reactRoot: any = null;

  static get observedAttributes() {
    return ['country', 'language', 'data-url', 'placeholder', 'username', 'password', 'enable-search', 'search-placeholder'];
  }

  connectedCallback() {
    this.render();
  }

  attributeChangedCallback() {
    if (this.reactRoot) {
      this.render();
    }
  }

  private render() {
    if (!window.React || !window.ReactDOM) {
      console.error('React and ReactDOM must be loaded before using cities-dropdown');
      this.innerHTML = '<div style="padding: 10px; color: red;">Error: React not loaded</div>';
      return;
    }

    const React = window.React;
    const ReactDOM = window.ReactDOM;
    const { CitiesDropdown } = (window as any).CitiesGenerator || {};

    if (!CitiesDropdown) {
      console.error('CitiesGenerator not loaded');
      this.innerHTML = '<div style="padding: 10px; color: red;">Error: CitiesGenerator not loaded</div>';
      return;
    }

    const country = this.getAttribute('country') || 'IT';
    const language = this.getAttribute('language') || 'it';
    const dataUrl = this.getAttribute('data-url');
    const placeholder = this.getAttribute('placeholder') || 'Select location...';
    const username = this.getAttribute('username');
    const password = this.getAttribute('password');
    const enableSearch = this.getAttribute('enable-search') === 'true';
    const searchPlaceholder = this.getAttribute('search-placeholder') || 'Search location...';

    const handleSelect = (node: any) => {
      this.dispatchEvent(
        new CustomEvent('select', {
          detail: node,
          bubbles: true,
        })
      );
    };

    const element = React.createElement(CitiesDropdown, {
      country,
      language,
      dataUrl,
      placeholder,
      username,
      password,
      enableSearch,
      searchPlaceholder,
      onSelect: handleSelect,
    });

    if (!this.reactRoot) {
      this.reactRoot = ReactDOM.createRoot(this);
    }

    this.reactRoot.render(element);
  }
}

// Register the custom element
if (!customElements.get('cities-dropdown')) {
  customElements.define('cities-dropdown', CitiesDropdownElement);
}

export default CitiesDropdownElement;

