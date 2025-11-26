// Web Component wrapper for maximum portability
import { CitiesDropdown } from './CitiesDropdown';
import { Nodes } from './types';

class CitiesDropdownElement extends HTMLElement {
  private root: ShadowRoot;
  private reactRoot: any = null;

  static get observedAttributes() {
    return ['country', 'language', 'data-url', 'placeholder', 'username', 'password', 'enable-search', 'search-placeholder', 'model', 'popup'];
  }

  constructor() {
    super();
    this.root = this.attachShadow({ mode: 'open' });
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
    const country = this.getAttribute('country') || 'IT';
    const language = this.getAttribute('language') || 'it';
    const dataUrl = this.getAttribute('data-url');
    const placeholder = this.getAttribute('placeholder') || 'Select location...';
    const username = this.getAttribute('username');
    const password = this.getAttribute('password');
    const enableSearch = this.hasAttribute('enable-search');
    const searchPlaceholder = this.getAttribute('search-placeholder') || 'Search location...';
    const modelAttr = this.getAttribute('model');
    const model = modelAttr ? parseInt(modelAttr, 10) : 0;
    const popup = this.hasAttribute('popup');

    // Import React dynamically for Web Component
    import('react').then((React) => {
      import('react-dom/client').then((ReactDOM) => {
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
          model,
          popup,
          onSelect: handleSelect,
        });

        if (!this.reactRoot) {
          const container = document.createElement('div');
          this.root.appendChild(container);
          this.reactRoot = ReactDOM.createRoot(container);
        }

        this.reactRoot.render(element);
      });
    });
  }
}

// Register the custom element
if (!customElements.get('cities-dropdown')) {
  customElements.define('cities-dropdown', CitiesDropdownElement);
}

export default CitiesDropdownElement;

