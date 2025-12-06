import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import fs from 'fs';
import path from 'path';

// UMD build (requires React external, CSS is external)
const umdConfig = defineConfig({
  plugins: [react()],
  build: {
    emptyOutDir: false, // Don't clean dist between builds
    cssCodeSplit: false, // Don't split CSS into separate files
    lib: {
      entry: 'src/standalone.tsx',
      name: 'CitiesGenerator',
      fileName: (format) => `cities-generator.${format}.js`,
      formats: ['umd'],
    },
    rollupOptions: {
      external: ['react', 'react-dom'],
      output: {
        globals: {
          react: 'React',
          'react-dom': 'ReactDOM',
        },
        entryFileNames: 'cities-generator.umd.js',
        chunkFileNames: '[name].js',
        assetFileNames: 'style.css', // CSS file name
        format: 'umd',
      },
    },
  },
});

// Standalone build (includes React - larger but zero dependencies)
const standaloneConfig = defineConfig({
  plugins: [react()],
  define: {
    'process.env.NODE_ENV': JSON.stringify('production'),
  },
  build: {
    emptyOutDir: false, // Don't clean dist between builds
    cssCodeSplit: false, // Don't split CSS into separate files
    lib: {
      entry: 'src/standalone.tsx',
      name: 'CitiesGenerator',
      fileName: (format) => `cities-generator-standalone.${format}.js`,
      formats: ['iife'],
    },
    rollupOptions: {
      output: {
        inlineDynamicImports: true,
        entryFileNames: 'cities-generator-standalone.iife.js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name].[ext]',
        // Inject CSS into JS bundle
        manualChunks: undefined,
      },
      plugins: [
        // Plugin to inject CSS and process polyfill into JS
        {
          name: 'inject-css-and-polyfill',
          generateBundle(options, bundle) {
            // Find CSS file and inject it into JS bundle
            const cssFile = Object.keys(bundle).find(file => file.endsWith('.css'));
            const jsFile = Object.keys(bundle).find(file => file.endsWith('.iife.js'));
            
            if (jsFile && bundle[jsFile]) {
              // Inject process polyfill FIRST (before CSS, before everything)
              // Must be executed immediately and synchronously before any other code
              // Define process globally for React compatibility
              // React accesses 'process' directly, so we need to make it available as a global variable
              // This polyfill works even with UTF-8 BOM present
              const processPolyfill = `
(function() {
  'use strict';
  var processObj = {
    env: {
      NODE_ENV: 'production'
    }
  };
  
  // Get the global object (works in all environments)
  var globalObj = (function() {
    if (typeof globalThis !== 'undefined') return globalThis;
    if (typeof window !== 'undefined') return window;
    if (typeof global !== 'undefined') return global;
    if (typeof self !== 'undefined') return self;
    return this;
  })();
  
  // Define process on all possible global objects
  try {
    globalObj.process = processObj;
    if (typeof window !== 'undefined' && window !== globalObj) {
      window.process = processObj;
    }
    if (typeof globalThis !== 'undefined' && globalThis !== globalObj) {
      globalThis.process = processObj;
    }
    
    // Use Function constructor to define 'process' as a true global variable
    // This works even in strict mode because Function creates a new execution context
    // This is necessary because React accesses 'process' directly, not 'window.process'
    (new Function('process', 'this.process = process;')).call(globalObj, processObj);
  } catch(e) {
    // Fallback: try Object.defineProperty
    try {
      Object.defineProperty(globalObj, 'process', {
        value: processObj,
        writable: true,
        enumerable: true,
        configurable: true
      });
    } catch(e2) {
      // Last resort: direct assignment (might not work in strict mode)
      try {
        globalObj.process = processObj;
      } catch(e3) {}
    }
  }
})();
`;
              
              // Inject CSS as style tag - preserve Unicode characters like UMD does
              // Read CSS directly from source files to preserve UTF-8 encoding
              let cssInjection = '';
              if (cssFile && bundle[cssFile]) {
                // Try to read CSS from source files first to preserve Unicode characters
                // This mimics how UMD loads external CSS files with proper UTF-8 encoding
                let cssContent = '';
                try {
                  // Read CSS files from source directory with explicit UTF-8 encoding
                  const cssFiles = [
                    'src/index.css',
                    'src/CitiesDropdown.css',
                    'src/models/CitiesDropdownModel0.css',
                    'src/models/CitiesDropdownModel1.css'
                  ];
                  const cssContents = cssFiles.map(file => {
                    const filePath = path.resolve(process.cwd(), file);
                    if (fs.existsSync(filePath)) {
                      return fs.readFileSync(filePath, 'utf8');
                    }
                    return '';
                  }).filter(content => content.length > 0);
                  cssContent = cssContents.join('\n');
                } catch (err) {
                  // Fallback to bundle CSS if reading source fails
                  let bundleCss = bundle[cssFile].source;
                  if (Buffer.isBuffer(bundleCss)) {
                    bundleCss = bundleCss.toString('utf8');
                  } else if (typeof bundleCss !== 'string') {
                    bundleCss = String(bundleCss);
                  }
                  cssContent = bundleCss;
                }
                // JSON.stringify correctly handles Unicode characters in strings
                cssInjection = `
(function() {
  if (typeof document !== 'undefined') {
    const style = document.createElement('style');
    style.textContent = ${JSON.stringify(cssContent)};
    document.head.appendChild(style);
  }
})();
`;
                // Delete CSS file
                delete bundle[cssFile];
              }
              
              // Prepend UTF-8 BOM and polyfill/CSS to the bundle
              // BOM is needed for proper Unicode character interpretation (like UMD does)
              // The polyfill is designed to work correctly even with BOM present
              const utf8BOM = '\uFEFF';
              bundle[jsFile].code = utf8BOM + processPolyfill + cssInjection + bundle[jsFile].code;
            }
          },
        },
      ],
    },
  },
});

// Web Component build
const webComponentConfig = defineConfig({
  plugins: [react()],
  build: {
    emptyOutDir: false, // Don't clean dist between builds (only clean on first build)
    lib: {
      entry: 'src/index.tsx',
      name: 'CitiesGenerator',
      fileName: (format) => `cities-generator-wc.${format}.js`,
      formats: ['es'],
    },
    rollupOptions: {
      output: {
        entryFileNames: 'cities-generator-wc.js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name].[ext]',
      },
    },
  },
});

// Demo app build (full React app with configurator)
const demoConfig = defineConfig({
  plugins: [react()],
  build: {
    emptyOutDir: false,
    rollupOptions: {
      input: 'src/main.tsx',
      output: {
        format: 'iife',
        name: 'CitiesGeneratorDemo',
        entryFileNames: 'cities-generator-demo.js',
        inlineDynamicImports: true,
      },
    },
  },
});

export default ({ mode }) => {
  if (mode === 'standalone') {
    return standaloneConfig;
  }
  if (mode === 'webcomponent') {
    return webComponentConfig;
  }
  if (mode === 'demo') {
    return demoConfig;
  }
  return umdConfig;
};
