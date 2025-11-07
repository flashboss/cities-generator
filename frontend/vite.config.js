import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { readFileSync, readdirSync, statSync } from 'fs';
import { join } from 'path';
import os from 'os';

// Plugin to serve JSON files from $HOME/cities-generator in development
const citiesGeneratorPlugin = () => {
  return {
    name: 'cities-generator-local',
    configureServer(server) {
      // Serve list of available countries
      server.middlewares.use('/cities-generator/countries.json', (req, res, next) => {
        try {
          const homeDir = os.homedir();
          const citiesDir = join(homeDir, 'cities-generator');
          
          const files = readdirSync(citiesDir).filter((file) => 
            file.endsWith('.json') && statSync(join(citiesDir, file)).isFile()
          );

          const countries = files.map((file) => {
            const code = file.replace('.json', '').toLowerCase();
            // Try to read the file to get country name from first zone
            try {
              const content = readFileSync(join(citiesDir, file), 'utf-8');
              const data = JSON.parse(content);
              // Extract country name from zones if available
              const name = data.zones?.[0]?.name || code.toUpperCase();
              return { code, name, file };
            } catch {
              return { code, name: code.toUpperCase(), file };
            }
          });

          res.setHeader('Content-Type', 'application/json');
          res.setHeader('Access-Control-Allow-Origin', '*');
          res.end(JSON.stringify(countries));
        } catch (err) {
          res.statusCode = 404;
          res.end(JSON.stringify([]));
        }
      });

      // Serve individual country JSON files
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
