# Cities Generator JSON Files

This directory can be used to store local JSON files if you want to serve them from your own server.

## Default Behavior

By default, the component loads data from the GitHub repository:
- Default URL: `https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/europe/{country}.json`

## Using Local Files

If you want to use local files instead, you can:

1. Place JSON files here with the naming pattern `{country}.json` (e.g., `it.json`, `gb.json`)

2. Use the `data-url` attribute to point to this directory:
   ```html
   <cities-dropdown 
     country="it" 
     data-url="/cities-generator">
   </cities-dropdown>
   ```
   This will automatically load `/cities-generator/it.json`

Or specify the full path:
   ```html
   <cities-dropdown 
     country="it" 
     data-url="/cities-generator/it.json">
   </cities-dropdown>
   ```

