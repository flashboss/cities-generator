import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

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
              const processPolyfill = `
(function() {
  if (typeof window !== 'undefined' && typeof window.process === 'undefined') {
    window.process = {
      env: {
        NODE_ENV: 'production'
      }
    };
  }
})();
`;
              
              // Inject CSS as style tag
              let cssInjection = '';
              if (cssFile && bundle[cssFile]) {
                const cssContent = bundle[cssFile].source;
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
              
              // Prepend polyfill and CSS to the bundle
              bundle[jsFile].code = processPolyfill + cssInjection + bundle[jsFile].code;
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
