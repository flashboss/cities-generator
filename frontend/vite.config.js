import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { readFileSync } from 'fs';
import { join } from 'path';
import os from 'os';

// Plugin to serve JSON files from $HOME/cities-generator in development
const citiesGeneratorPlugin = () => {
  return {
    name: 'cities-generator-local',
    configureServer(server) {
      server.middlewares.use('/cities-generator', (req, res, next) => {
        // Only serve from local filesystem in development
        // In production, files should be in public folder or served via dataUrl
        const url = req.url || '';
        const match = url.match(/\/([a-z]{2})\.json$/);
        
        if (match) {
          const country = match[1];
          const homeDir = os.homedir();
          const jsonPath = join(homeDir, 'cities-generator', `${country}.json`);
          
          try {
            const jsonContent = readFileSync(jsonPath, 'utf-8');
            // Validate it's valid JSON
            JSON.parse(jsonContent);
            res.setHeader('Content-Type', 'application/json');
            res.setHeader('Access-Control-Allow-Origin', '*');
            res.end(jsonContent);
            console.log(`[Cities Generator] Served ${country}.json from ${jsonPath}`);
          } catch (err) {
            // File not found or invalid JSON, try next middleware (public folder)
            console.log(`[Cities Generator] File not found at ${jsonPath}, trying public folder...`);
            next();
          }
        } else {
          next();
        }
      });
    },
  };
};

// UMD build (requires React external)
const umdConfig = defineConfig({
  plugins: [react(), citiesGeneratorPlugin()],
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
