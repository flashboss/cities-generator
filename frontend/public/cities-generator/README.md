# Cities Generator JSON Files

This directory can be used to store local JSON files if you want to serve them from your own server.

## Default Behavior

By default, the component loads data from the GitHub repository:
- Default URL: `https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/{continent}/{country}/{language}.json`
- Structure: `{continent}/{country}/{language}.json` (e.g., `EU/IT/it.json`, `EU/GB/en.json`)

## Using Local Files

If you want to use local files instead, you can:

1. Place JSON files here with the naming pattern `{continent}/{country}/{language}.json` (e.g., `EU/IT/it.json`, `EU/GB/en.json`)

2. Use the `data-url` attribute to point to this directory:
   ```html
   <cities-dropdown 
     country="IT" 
     language="it"
     data-url="/cities-generator">
   </cities-dropdown>
   ```
   This will automatically load `/cities-generator/EU/IT/it.json`

Or specify the full path:
   ```html
   <cities-dropdown 
     country="IT" 
     language="it"
     data-url="/cities-generator/IT/it.json">
   </cities-dropdown>
   ```

