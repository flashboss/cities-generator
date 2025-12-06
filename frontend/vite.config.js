import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// UMD build (requires React external, includes CSS)
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
        assetFileNames: '[name].[ext]',
        format: 'umd',
        // Inject CSS into JS bundle
        manualChunks: undefined,
      },
      plugins: [
        // Plugin to inject CSS into JS
        {
          name: 'inject-css',
          generateBundle(options, bundle) {
            // Find CSS file and inject it into JS bundle
            const cssFile = Object.keys(bundle).find(file => file.endsWith('.css'));
            if (cssFile && bundle[cssFile]) {
              const cssContent = bundle[cssFile].source;
              const jsFile = Object.keys(bundle).find(file => file.endsWith('.umd.js'));
              if (jsFile && bundle[jsFile]) {
                // Inject CSS as style tag
                const cssInjection = `
(function() {
  if (typeof document !== 'undefined') {
    const style = document.createElement('style');
    style.textContent = ${JSON.stringify(cssContent)};
    document.head.appendChild(style);
  }
})();
`;
                bundle[jsFile].code = cssInjection + bundle[jsFile].code;
                // Delete CSS file
                delete bundle[cssFile];
              }
            }
          },
        },
      ],
    },
  },
});

// Standalone build (includes React - larger but zero dependencies)
const standaloneConfig = defineConfig({
  plugins: [react()],
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
        // Plugin to inject CSS into JS
        {
          name: 'inject-css',
          generateBundle(options, bundle) {
            // Find CSS file and inject it into JS bundle
            const cssFile = Object.keys(bundle).find(file => file.endsWith('.css'));
            if (cssFile && bundle[cssFile]) {
              const cssContent = bundle[cssFile].source;
              const jsFile = Object.keys(bundle).find(file => file.endsWith('.iife.js'));
              if (jsFile && bundle[jsFile]) {
                // Inject CSS as style tag
                const cssInjection = `
(function() {
  if (typeof document !== 'undefined') {
    const style = document.createElement('style');
    style.textContent = ${JSON.stringify(cssContent)};
    document.head.appendChild(style);
  }
})();
`;
                bundle[jsFile].code = cssInjection + bundle[jsFile].code;
                // Delete CSS file
                delete bundle[cssFile];
              }
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
