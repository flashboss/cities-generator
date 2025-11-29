---
layout: guide
title: Cities Generator
permalink: /guide/
---

# Cities Generator

Generates a descriptor file for the cities choosing:

- **-c:** the country of the generated cities named by the first two characters for example **GB** and **IT**. If not specified the default locale of the machine is used.
- **-l:** the language code for the location names (e.g., "it", "en", "fr", "de", "es", "pt"). Supported languages: IT (Italian, default), EN (English), FR (French), DE (German), ES (Spanish), PT (Portuguese). If not specified, defaults to "it".
- **-s:** the case for the name of the cities. Can be true or false or none will be true as default.
- **-d:** true if you allow duplicated names of cities. Else none or false.
- **-p:** choose the first provider to create the file descriptor. You can choose for **GB**: BRITANNICA, GEONAMES or OPENSTREETMAP. For **IT**: COMUNIITALIANI, WIKIPEDIA, EXTRAGEONAMES, EXTRA_OPENSTREETMAP or OPENSTREETMAP. For all other countries the provider is GEONAMES or OPENSTREETMAP. Else start a default.
- **-u:** a optional username to use for the providers: GEONAMES and EXTRAGEONAMES. If not specified **vota** is the default.
- **-g** or **--git-config:** publish generated JSON files to Git repository. Format: CSV with key=value pairs separated by commas.
  
  Keys: `repo` (repository URL, required), `branch` (default: master), `dir` (default: _db), `username`, `token`, `message` (commit message).
  
  ```bash
  Example: --git-config "repo=https://github.com/user/repo.git,branch=main,dir=data,username=user,token=xxx,message=Update"
  ```
  
  All fields except `repo` are optional. Default repository: https://github.com/flashboss/cities-generator.git. Username and token can also be set via `GIT_USERNAME` and `GIT_TOKEN` environment variables.

## Enabling DEBUG logs

To see DEBUG level logs (more detailed information), use the system property `-Dorg.slf4j.simpleLogger.defaultLogLevel=debug`:

```bash
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar cities-generator-1.2.7.jar -p EXTRA_GEONAMES -c IT
```

Available log levels: `trace`, `debug`, `info`, `warn`, `error` (default: `info`)

## Remote Debugging

To enable remote debugging, use the `-agentlib:jdwp` parameter:

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar cities-generator-1.2.7.jar -p EXTRA_GEONAMES -c IT -l en
```

Or with quotes (for zsh compatibility):

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address='*:5005' -jar cities-generator-1.2.7.jar -p EXTRA_GEONAMES -c IT -l en
```

Parameters:

- `transport=dt_socket`: uses socket transport
- `server=y`: JVM acts as debug server (waits for debugger to connect)
- `suspend=n`: starts immediately (use `suspend=y` to wait for debugger before starting)
- `address=5005`: listens on port 5005 on all interfaces (default)
- `address=localhost:5005`: listens only on localhost
- `address='*:5005'`: listens on all interfaces (requires quotes in zsh)

Then connect your IDE debugger to `localhost:5005`.

**Example with DEBUG logs and remote debugging:**

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar cities-generator-1.2.7.jar -p EXTRA_GEONAMES -c IT -l en
```

To generate the cities, you can choose between 3 modes:

- By a command line shell digit:

```bash
mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.0:copy -Dartifact=it.vige.cities:cities-generator:1.2.7:jar -DoutputDirectory=. && java -jar cities-generator-1.2.7.jar -c GB -l en
```

It will return a json file inside the ${user.home}/cities-generator/EU/GB/en.json (structure: {continent}/{country}/{language}.json)

- Download the source and execute:

```bash
cd library;./gradlew build;java -jar build/libs/cities-generator-1.2.7.jar -c IT -l it
```

- Through api java follow the instructions:

1. On maven add the following script in the pom.xml file:

```xml
<dependency>
    <groupId>it.vige.cities</groupId>
    <artifactId>cities-generator</artifactId>
    <version>1.2.7</version>
</dependency>
```

   or on gradle in the build.gradle file:

   ```gradle
   compile('it.vige.cities:cities-generator:1.2.7')
   ```

1. Execute the following java instructions:

```java
import it.vige.cities.Generator;
import it.vige.cities.Countries;
import it.vige.cities.Languages;
import it.vige.cities.result.Nodes;
...
Configuration configuration = new Configuration();
configuration.setCountry(Countries.IT.name());
configuration.setLanguage(Languages.IT); // Using enum (recommended)
// Or use string: configuration.setLanguage("it"); // Optional, defaults to "it"
Generator generator = new Generator(configuration, true);
Nodes result = generator.generate();
System.out.println(result.getZones());
```

you can also generate a file through the instruction:

```java
...
import it.vige.cities.Result;
...
Result result = generator.generateFile();
```

You will find the file EU/IT/it.json in the ${user.home}/cities-generator directory (structure: {continent}/{country}/{language}.json)

For detailed API documentation, see the [Javadoc](https://javadoc.io/doc/it.vige.cities/cities-generator).

## Publishing to Git

After generating the JSON files, you can automatically publish them to a Git repository using the `--git-config` parameter. This feature clones the repository, copies the generated JSON file, commits, and pushes the changes.

### Basic Usage

```bash
# Publish with default repository (https://github.com/flashboss/cities-generator.git)
java -jar cities-generator-1.2.7.jar -c GB -p OPENSTREETMAP --git-config "repo=https://github.com/user/repo.git"

# Publish with custom repository and branch
java -jar cities-generator-1.2.7.jar -c GB -p OPENSTREETMAP --git-config "repo=https://github.com/user/repo.git,branch=main"

# Publish with all options
java -jar cities-generator-1.2.7.jar -c GB -p OPENSTREETMAP --git-config "repo=https://github.com/user/repo.git,branch=main,dir=data,username=myuser,token=ghp_xxxxxxxxxxxxx,message=Update cities data"
```

### Configuration Format

The `--git-config` parameter accepts a CSV string with key=value pairs:

- **repo** (required): Git repository URL
- **branch** (optional, default: `master`): Git branch name
- **dir** (optional, default: `_db`): Directory in repository where to publish files
- **username** (optional): Git username for authentication
- **token** (optional): Git token/password for authentication (GitHub Personal Access Token)
- **message** (optional): Commit message (default: auto-generated based on country and language)

### Authentication

You can provide credentials in two ways:

1. **Via command line:**

   ```bash
   java -jar cities-generator-1.2.7.jar -c GB -p OPENSTREETMAP --git-config "repo=https://github.com/user/repo.git,username=myuser,token=ghp_xxx"
   ```

2. **Via environment variables:**

   ```bash
   export GIT_USERNAME=myuser
   export GIT_TOKEN=ghp_xxxxxxxxxxxxx
   java -jar cities-generator-1.2.7.jar -c GB -p OPENSTREETMAP --git-config "repo=https://github.com/user/repo.git"
   ```

For GitHub, use a Personal Access Token (PAT) as the token. Create one at: <https://github.com/settings/tokens>

### Examples

```bash
# Minimal: only repository URL
--git-config "repo=https://github.com/user/repo.git"

# With branch and directory
--git-config "repo=https://github.com/user/repo.git,branch=develop,dir=cities"

# With authentication
--git-config "repo=https://github.com/user/repo.git,username=myuser,token=ghp_xxx"

# Complete example
--git-config "repo=https://github.com/user/repo.git,branch=main,dir=_db,username=myuser,token=ghp_xxx,message=Update cities for GB"
```

## Geonames registration

If you use GEONAMES or EXTRAGEONAMES, you use a default username. In the long run this default username may be inactive, so you will need a new username to specify in the configuration field seen above. To get the new username you must register through the site: <https://www.geonames.org/login>

## OpenStreetMap provider

The OPENSTREETMAP provider uses the Overpass API to retrieve administrative boundaries (regions, provinces, and municipalities) from OpenStreetMap data. This provider:

- **Works for all countries** supported by OpenStreetMap
- **Includes 3 levels**: Regions (level 0), Provinces/Cities (level 1), and Municipalities (level 2)
- **Supports multiple languages** for location names
- **For Italy**: Use EXTRA_OPENSTREETMAP to get macroregions (level 0) grouping regions by geographical area, or OPENSTREETMAP for standard regions

**Note**: OpenStreetMap queries may take longer than other providers due to the number of API calls required (approximately 1 + number of regions + number of provinces queries per country).

## REST Service

A REST service can be installed in your machine. This returns a json format with the cities of the used country according the roles seen over. To install it download the source under /service folder and execute:

```bash
./gradlew clean build
```

And then start it through the command:

```bash
java -jar build/libs/cities-generator-service-1.2.7.jar --country=IT --language=it --server.port=8380 --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```

Keycloak params are mandatory to connect to a custom keycloak server. It allows the authorization. To use the service connect through browser to <http://cities-generator-service.vige.it:8380/swagger-ui/index.html>

In a production environment you could use https so:

```bash
java -Djavax.net.ssl.trustStore=./docker/prod/volume/cert/application-ct.keystore -Djavax.net.ssl.trustStorePassword=password -jar build/libs/cities-generator-service-1.2.7.jar --server.ssl.key-store=./docker/prod/volume/cert/application-ct.keystore --server.ssl.key-store-password=password --server.ssl.trust-store=./docker/prod/volume/cert/application-ct.keystore --server.ssl.trust-store-password=password --server.port=8743 --country=IT --language=it --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```

### Docker development image

There is an already docker image to start the REST service. Execute this command to start the service:

```bash
docker pull vige/cities-generator
```

To run the image use the command:

```bash
docker run -d --name cities-generator -p8380:8080 -eCOUNTRY=IT -eREALM=${realm} -eAUTHURL=${url} -eRESOURCE=${resource} vige/cities-generator
```

Where IT is the chosen country. You can choose GB,IT or other else country using the first two characters of the code.

Over the country, optionally as for the library you can add the following param:

- REALM
- AUTHURL
- RESOURCE
- COUNTRY
- PROVIDER
- CASESENSITIVE
- DUPLICATEDNAMES
- USERNAME

This image starts without SSO server, so it is not complete. For a sample complete environment you can start the command from the docker/dev folder of the project:

```bash
COUNTRY=${COUNTRY} REPLACER_CLIENT_ADDRESS=${REPLACER_CLIENT_ADDRESS} AUTHURL=${AUTHURL} docker compose up
```

Where ${COUNTRY} is the choosen country, IT or GB. While ${REPLACER_CLIENT_ADDRESS} is the address of the cities-generator client, for example cities-generator-service.vige.it:8380 .

${AUTHURL} is the keycloak address url like <http://auth-ct.vige.it:8080>

It will allow to download a keycloak instance where the server can be connected.

After you can connect to keycloak through the url <http://auth-ct.vige.it:8080>

Here a sample:

```bash
COUNTRY=IT REPLACER_CLIENT_ADDRESS=cities-generator-service.vige.it:8380 AUTHURL=http://auth-ct.vige.it:8080 docker compose up
```

Add the following DNS in your /etc/hosts file:

```text
$IP_ADDRESS cities-generator-service.vige.it
$IP_ADDRESS auth-ct.vige.it
```

where in $IP_ADDRESS you must choose the ip address where is located the server.

To use the service connect through browser to <http://cities-generator-service.vige.it:8380/swagger-ui/index.html>

If you need to make write operations you can log through:

```text
user: root  
pass: gtn
```

### Docker production image

There is an already docker image to start the REST service. Execute this command to start the service:

```bash
docker pull vige/cities-generator
```

To run the image use the command:

```bash
docker run -d --name cities-generator -p8743:8443 -eCOUNTRY=IT -eLANGUAGE=it -eREALM=${realm} -eAUTHURL=${url} -eRESOURCE=${resource} vige/cities-generator
```

Where IT is the chosen country. You can choose GB,IT or other else country using the first two characters of the code.

The language defaults to "it" if not specified.

Over the country, optionally as for the library you can add the following param:

- REALM
- AUTHURL
- RESOURCE
- COUNTRY
- LANGUAGE (default: "it")
- PROVIDER
- CASESENSITIVE
- DUPLICATEDNAMES
- USERNAME

This image starts without SSO server, so it is not complete. For a sample complete environment you can start the command from the docker/prod folder of the project:

```bash
COUNTRY=${COUNTRY} REPLACER_CLIENT_ADDRESS=${REPLACER_CLIENT_ADDRESS} HOST_NAME=${HOST_NAME} AUTHURL=${AUTHURL} PASSWORD_STORE=${PASSWORD_STORE} docker compose up
```

Where ${COUNTRY} is the choosen country, IT or GB. While ${HOST_NAME} is the external keycloak url, for example auth-ct.vige.it .

${REPLACER_CLIENT_ADDRESS} is the address of the cities-generator client, for example cities-generator-service.vige.it:8743

${AUTHURL} is the keycloak address url like <https://auth-ct.vige.it:8443>

${PASSWORD_STORE} is the password for the SSL/TLS keystore files. Default: `password`.

It will allow to download a keycloak instance where the server can be connected.

After you can connect to keycloak through the url <https://auth-ct.vige.it:8443>

Here a sample:

```bash
COUNTRY=IT REPLACER_CLIENT_ADDRESS=cities-generator-service.vige.it:8743 HOST_NAME=auth-ct.vige.it AUTHURL=https://auth-ct.vige.it:8443 docker compose up
```

Add the following DNS in your /etc/hosts file:

```text
$IP_ADDRESS cities-generator-service.vige.it
$IP_ADDRESS auth-ct.vige.it
```

where in $IP_ADDRESS you must choose the ip address where is located the server.

To use the service connect through browser to <https://cities-generator-service.vige.it:8743/swagger-ui/index.html>

If you need to make write operations you can log through:

```text
user: root  
pass: gtn
```

#### certificates

in a production environment we are using a default certificate but you could move a different ssl certificate and keys. Use this command to generate it:

```bash
keytool -genkey -alias cities-generator-service -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore ./docker/prod/volume/cert/application-ct.keystore -validity 3650 -dname "CN=cities-generator-service.vige.it, OU=Vige, O=Vige, L=Rome, S=Italy, C=IT" -storepass password -keypass password
```

You need to create a certificate and import it through the command:

```bash
keytool -v -export -file mytrustCA.cer -keystore ./docker/prod/volume/cert/application-ct.keystore -alias cities-generator-service
keytool -import -alias trustedCA -file mytrustCA.cer -keystore ./docker/prod/volume/cert/application-ct.keystore -storepass password -keypass password
```

Actually samples of generated cities can be found online:

- <https://raw.githubusercontent.com/flashboss/cities-generator/master/_db

The structure is {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)

## Frontend

A portable, hierarchical dropdown menu component for displaying cities data from cities-generator JSON files. The component is designed to work in any platform (WordPress, Drupal, Liferay, Joomla, etc.) with minimal integration effort.

### Features

- 🌳 **Hierarchical Navigation**: Navigate through nested location data
- 📦 **Highly Portable**: Works as Web Component, React component, or vanilla JS
- 🎨 **Customizable**: Easy to style and integrate
- 📱 **Responsive**: Works on desktop and mobile
- ♿ **Accessible**: ARIA labels and keyboard navigation

### Installation

Navigate to the frontend directory and install dependencies:

```bash
cd frontend
npm install
```

### Development

Start the development server:

```bash
npm run dev
```

### Build

Build all variants (UMD, Standalone, Web Component):

```bash
npm run build
```

Or build specific variants:

```bash
npm run build:umd          # UMD bundle (requires React external)
npm run build:standalone   # Standalone bundle (includes React)
npm run build:webcomponent # Web Component ES module
```

The build outputs are in the `dist/` directory:

- `cities-generator.umd.js` - UMD bundle (~18KB, requires React)
- `cities-generator-standalone.iife.js` - Standalone bundle (~52KB, includes React)
- `style.css` - Component styles

### Usage

#### Method 1: Web Component (Most Portable - Recommended)

Works in **any** platform without framework dependencies. Simply include the scripts and use the custom HTML element:

```html
<!-- Load React (if not already loaded) -->
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>

<!-- Load Cities Generator -->
<script src="./dist/cities-generator.umd.js"></script>
<link rel="stylesheet" href="./dist/style.css">

<!-- Use the component -->
<!-- Using default parameters -->
<cities-dropdown />

<!-- Using default URL -->
<cities-dropdown 
  country="IT" 
  language="it"
  placeholder="Select location...">
</cities-dropdown>

<!-- Using custom base URL (automatically appends /IT/it.json) -->
<cities-dropdown 
  country="IT" 
  language="it"
  data-url="https://example.com/cities"
  placeholder="Select location...">
</cities-dropdown>

<script>
  document.querySelector('cities-dropdown').addEventListener('select', (e) => {
    console.log('Selected:', e.detail);
    // e.detail contains: { id, name, level, zones }
  });
</script>
```

#### Method 2: Standalone Bundle (Zero Dependencies)

Use when you can't include React separately. The standalone bundle includes React:

```html
<!-- Load standalone bundle (includes React) -->
<script src="./dist/cities-generator-standalone.iife.js"></script>
<link rel="stylesheet" href="./dist/style.css">

<!-- Use via JavaScript API -->
<div id="my-dropdown"></div>
<script>
  // Using default parameters
  CitiesGenerator.render('#my-dropdown');

  // Using default URL
  CitiesGenerator.render('#my-dropdown', {
    country: 'IT',
    language: 'it',
    placeholder: 'Select location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
  
  // Using custom base URL (automatically appends /IT/it.json)
  CitiesGenerator.render('#my-dropdown', {
    country: 'IT',
    language: 'it',
    dataUrl: 'https://example.com/cities',
    placeholder: 'Select location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
  
  // Using search functionality
  CitiesGenerator.render('#my-dropdown', {
    country: 'IT',
    language: 'it',
    placeholder: 'Select location...',
    enableSearch: true,
    searchPlaceholder: 'Search location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
</script>
```

#### Method 3: React Component

For React applications:

```tsx
import { CitiesDropdown } from './CitiesDropdown';

function MyComponent() {
  return (
    <>
      {/* Using default parameters */}
      <CitiesDropdown />

      {/* Using default URL */}
      <CitiesDropdown
        country="IT"
        language="it"
        placeholder="Select location..."
        onSelect={(node) => console.log(node)}
      />
      
      {/* Using custom base URL (automatically appends /IT/it.json) */}
      <CitiesDropdown
        country="IT"
        language="it"
        dataUrl="https://example.com/cities"
        placeholder="Select location..."
        onSelect={(node) => console.log(node)}
      />
      
      {/* Using search functionality */}
      <CitiesDropdown
        country="IT"
        language="it"
        placeholder="Select location..."
        enableSearch={true}
        searchPlaceholder="Search location..."
        onSelect={(node) => console.log(node)}
      />
    </>
  );
}
```

### Component Props

- `data` (Nodes, optional): Direct data object (optional)
- `onSelect` (function, optional): Callback when a leaf node is selected
- `className` (string, optional): Additional CSS classes
- `dataUrl` (string, optional): Base URL for remote data source. If not specified, uses default GitHub URL: <https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/EU/{country}/{language}.json>. If specified, treated as base URL and automatically appends `/{country}/{language}.json` (any `.json` extension in the URL is automatically removed)
- `country` (string, optional): Country code, e.g., "IT", "GB" (default: "IT")
- `language` (string, optional): Language code, e.g., "it", "en", "fr", "de", "es", "pt". Supported languages: IT (Italian, default), EN (English), FR (French), DE (German), ES (Spanish), PT (Portuguese). In Java API, you can use `Languages` enum: `Languages.IT`, `Languages.EN`, etc.
- `placeholder` (string, optional): Placeholder text (default: "Select location...")
- `username` (string, optional): Username for HTTP Basic Authentication
- `password` (string, optional): Password for HTTP Basic Authentication
- `enableSearch` (boolean, optional): Enable text search functionality (default: false)
- `searchPlaceholder` (string, optional): Placeholder text for the search input field (default: "Search location...")

### Events

When using the Web Component, listen for the `select` event:

```javascript
document.querySelector('cities-dropdown').addEventListener('select', (e) => {
  const node = e.detail; // { id, name, level, zones }
  console.log('Selected:', node.name, 'ID:', node.id);
});
```

### Platform Integration Examples

#### WordPress

**1. Upload files to theme:**

- Upload `cities-generator.umd.js` to `wp-content/themes/your-theme/js/`
- Upload JSON files to `wp-content/themes/your-theme/data/`

**2. Enqueue scripts in `functions.php`:**

```php
function enqueue_cities_dropdown() {
    wp_enqueue_script('react', 'https://unpkg.com/react@18/umd/react.production.min.js', [], '18.2.0', true);
    wp_enqueue_script('react-dom', 'https://unpkg.com/react-dom@18/umd/react-dom.production.min.js', ['react'], '18.2.0', true);
    wp_enqueue_script('cities-generator', get_template_directory_uri() . '/js/cities-generator.umd.js', ['react', 'react-dom'], '1.0.0', true);
    wp_enqueue_style('cities-generator', get_template_directory_uri() . '/js/style.css', [], '1.0.0');
}
add_action('wp_enqueue_scripts', 'enqueue_cities_dropdown');
```

**3. Use in template:**

```php
<!-- Using default parameters -->
<cities-dropdown />

<!-- Using default URL -->
<cities-dropdown country="IT" language="it"></cities-dropdown>

<!-- Using custom base URL (automatically appends /IT/it.json) -->
<cities-dropdown 
  country="IT" 
  language="it"
  data-url="<?php echo get_template_directory_uri(); ?>/data">
</cities-dropdown>
```

#### Drupal

**1. Create module or add to theme:**

In `your_theme.libraries.yml`:

```yaml
cities_dropdown:
  js:
    https://unpkg.com/react@18/umd/react.production.min.js: { type: external, minified: true }
    https://unpkg.com/react-dom@18/umd/react-dom.production.min.js: { type: external, minified: true, dependencies: [react] }
    js/cities-generator.umd.js: {}
  css:
    theme:
      js/style.css: {}
  dependencies:
    - core/drupal
```

**2. Attach to template:**

```twig
&#123;&#123; attach_library('your_theme/cities_dropdown') &#125;&#125;

{# Using default parameters #}
<cities-dropdown />

{# Using default URL #}
<cities-dropdown country="IT" language="it"></cities-dropdown>

{# Using custom base URL (automatically appends /IT/it.json) #}
<cities-dropdown country="IT" language="it" data-url="/sites/default/files/cities"></cities-dropdown>
```

#### Liferay

**1. Add to module or theme:**

In `liferay-plugin-package.properties`:

```properties
js.fast.load=true
```

**2. Include in JSP:**

```jsp
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
<script src="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/js/cities-generator.umd.js"></script>
<link rel="stylesheet" href="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/js/style.css">

<%-- Using default parameters --%>
<cities-dropdown />

<%-- Using default URL --%>
<cities-dropdown country="IT" language="it"></cities-dropdown>

<%-- Using custom base URL (automatically appends /IT/it.json) --%>
<cities-dropdown country="IT" language="it" data-url="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/data"></cities-dropdown>
```

#### Joomla

**1. Add to template:**

In `index.php`:

```php
$document = JFactory::getDocument();
$document->addScript('https://unpkg.com/react@18/umd/react.production.min.js');
$document->addScript('https://unpkg.com/react-dom@18/umd/react-dom.production.min.js');
$document->addScript(JURI::root() . 'templates/your-template/js/cities-generator.umd.js');
$document->addStyleSheet(JURI::root() . 'templates/your-template/js/style.css');
```

**2. Use in template:**

```php
<!-- Using default parameters -->
<cities-dropdown />

<!-- Using default URL -->
<cities-dropdown country="IT" language="it"></cities-dropdown>

<!-- Using custom base URL (automatically appends /IT/it.json) -->
<cities-dropdown country="IT" language="it" data-url="<?php echo JURI::root(); ?>data"></cities-dropdown>
```

### Data Format

The component expects JSON in this format (matching the cities-generator output):

```json
{
  "zones": [
    {
      "id": "1",
      "name": "I: ITALIA NORD-OCCIDENTALE",
      "level": 0,
      "zones": [
        {
          "id": "1-12345",
          "name": "Piemonte",
          "level": 1,
          "zones": [
            {
              "id": "1-12345-67890",
              "name": "Torino",
              "level": 2,
              "zones": []
            }
          ]
        }
      ]
    }
  ]
}
```

### Styling

The component uses CSS classes prefixed with `cities-dropdown-`. You can override styles:

```css
.cities-dropdown-trigger {
  border-color: #your-color;
  border-radius: 8px;
}

.cities-dropdown-item:hover {
  background-color: #your-hover-color;
}
```
