import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// UMD build (requires React external)
const umdConfig = defineConfig({
  plugins: [react()],
  build: {
    emptyOutDir: false, // Don't clean dist between builds
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
      },
    },
  },
});

// Standalone build (includes React - larger but zero dependencies)
const standaloneConfig = defineConfig({
  plugins: [react()],
  build: {
    emptyOutDir: false, // Don't clean dist between builds
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
      },
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

export default ({ mode }) => {
  if (mode === 'standalone') {
    return standaloneConfig;
  }
  if (mode === 'webcomponent') {
    return webComponentConfig;
  }
  return umdConfig;
};
