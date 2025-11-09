import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// UMD build (requires React external)
const umdConfig = defineConfig({
  plugins: [react()],
  build: {
    lib: {
      entry: 'src/standalone.tsx',
      name: 'CitiesGenerator',
      fileName: 'cities-generator',
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
        format: 'umd',
      },
    },
  },
});

// Standalone build (includes React - larger but zero dependencies)
const standaloneConfig = defineConfig({
  plugins: [react()],
  build: {
    lib: {
      entry: 'src/standalone.tsx',
      name: 'CitiesGenerator',
      fileName: 'cities-generator-standalone',
      formats: ['iife'],
    },
    rollupOptions: {
      output: {
        inlineDynamicImports: true,
      },
    },
  },
});

// Web Component build
const webComponentConfig = defineConfig({
  plugins: [react()],
  build: {
    lib: {
      entry: 'src/index.tsx',
      name: 'CitiesGenerator',
      fileName: 'cities-generator-wc',
      formats: ['es'],
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
