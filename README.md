# Cities Generator
Generates a descriptor file for the cities choosing:

- **-c:** the country of the generated cities named by the first two characters for example **gb** and **it**. If not specified the default locale of the machine is used.
- **-s:** the case for the name of the cities. Can be true or false or none will be true as default.
- **-d:** true if you allow duplicated names of cities. Else none or false.
- **-p:** choose the first provider to create the file descriptor. You can choose for **gb**: BRITANNICA or GEONAMES. For **it**: COMUNIITALIANI, TUTTITALIA or EXTRAGEONAMES. For all other coutries the provider is GEONAMES. Else start a default.
- **-u:** a optional username to use for the providers: GEONAMES and EXTRAGEONAMES. If not specified **vota** is the default.

To generate the cities, you can choose between 3 modes:

- By a command line shell digit:
```
mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.0:copy -Dartifact=it.vige.cities:cities-generator:1.2.5:jar -DoutputDirectory=. && java -jar cities-generator-1.2.5.jar -c gb
```
It will return a json file inside the ${user.home}/cities-generator dir

- Download the source and execute:
```
cd library;./gradlew build;java -jar build/libs/cities-generator-1.2.5.jar -c it
```

- Through api java follow the instructions:
	
1. On maven add the following script in the pom.xml file:
```
	<dependency>
		<groupId>it.vige.cities</groupId>
		<artifactId>cities-generator</artifactId>
		<version>1.2.5</version>
	</dependency>
```
	
   or on gradle in the build.gradle file:
		
   ```
	compile('it.vige.cities:cities-generator:1.2.5')
   ```
	
2. Execute the following java instructions:
```
	import it.vige.cities.Generator;
	import it.vige.cities.Countries;
	import it.vige.cities.result.Nodes;
	...
	Configuration configuration = new Configuration();
	configuration.setCountry(Countries.it.name());
	Generator generator = new Generator(configuration, true);
	Nodes result = generator.generate();
	System.out.println(result.getZones());
	```
	you can also generate a file through the instruction:
	```
	...
	import it.vige.cities.Result;
	...
	Result result = generator.generateFile();
```
You will find the file it.json in the ${user.home}/cities-generator directory

## Geonames registration

If you use GEONAMES or EXTRAGEONAMES, you use a default username. In the long run this default username may be inactive, so you will need a new username to specify in the configuration field seen above. To get the new username you must register through the site: https://www.geonames.org/login

## REST Service

A REST service can be installed in your machine. This returns a json format with the cities of the used country according the roles seen over. To install it download the source under /service folder and execute:
```
./gradlew clean build
```
And then start it through the command:
```
java -jar build/libs/cities-generator-service-1.2.5.jar --country=it --server.port=8380 --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```
Keycloak params are mandatory to connect to a custom keycloak server. It allows the authorization. To use the service connect through browser to http://cities-generator-service.vige.it:8380/swagger-ui/index.html
In a production environment you could use https so:
```
java -Djavax.net.ssl.trustStore=./docker/prod/volume/cert/application-ct.keystore -Djavax.net.ssl.trustStorePassword=password -jar build/libs/cities-generator-service-1.2.5.jar --server.ssl.key-store=./docker/prod/volume/cert/application-ct.keystore --server.ssl.key-store-password=password --server.ssl.trust-store=./docker/prod/volume/cert/application-ct.keystore --server.ssl.trust-store-password=password --server.port=8743 --country=it --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```

### Docker development image

There is an already docker image to start the REST service. Execute this command to start the service:
```
docker pull vige/cities-generator
```
To run the image use the command:
```
docker run -d --name cities-generator -p8380:8080 -eCOUNTRY=it -eREALM=${realm} -eAUTHURL=${url} -eRESOURCE=${resource} vige/cities-generator
```
Where it is the chosen country. You can choose gb,it or other else country using the first two characters of the code.
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
```
COUNTRY=${COUNTRY} REPLACER_CLIENT_ADDRESS=${REPLACER_CLIENT_ADDRESS} AUTHURL=${AUTHURL} docker compose up
```
Where ${COUNTRY} is the choosen language, it or en. While ${REPLACER_CLIENT_ADDRESS} is the address of the cities-generator client, for example cities-generator-service.vige.it:8380 .
${AUTHURL} is the keycloak address url like https://auth-ct.vige.it:8080
It will allow to download a keycloak instance where the server can be connected. 
After you can connect to keycloak through the url http://auth-ct.vige.it:8080

Here a sample:
COUNTRY=it REPLACER_CLIENT_ADDRESS=cities-generator-service.vige.it:8380 AUTHURL=http://auth-ct.vige.it:8080 docker compose up

Add the following DNS in your /etc/hosts file:
```
$IP_ADDRESS cities-generator-service.vige.it
$IP_ADDRESS auth-ct.vige.it
```
where in $IP_ADDRESS you must choose the ip address where is located the server.
To use the service connect through browser to http://cities-generator-service.vige.it:8380/swagger-ui/index.html

If you need to make write operations you can log through:
```
user: root  
pass: gtn
```

### Docker production image

There is an already docker image to start the REST service. Execute this command to start the service:
```
docker pull vige/cities-generator
```
To run the image use the command:
```
docker run -d --name cities-generator -p8743:8443 -eCOUNTRY=it -eREALM=${realm} -eAUTHURL=${url} -eRESOURCE=${resource} vige/cities-generator
```
Where it is the chosen country. You can choose gb,it or other else country using the first two characters of the code.
Over the country, optionally as for the library you can add the following param:

- REALM
- AUTHURL
- RESOURCE
- COUNTRY
- PROVIDER
- CASESENSITIVE
- DUPLICATEDNAMES
- USERNAME

This image starts without SSO server, so it is not complete. For a sample complete environment you can start the command from the docker/prod folder of the project:
```
COUNTRY=${COUNTRY} REPLACER_CLIENT_ADDRESS=${REPLACER_CLIENT_ADDRESS} HOST_NAME=${HOST_NAME} AUTHURL=${AUTHURL} PASSWORD_STORE=${PASSWORD_STORE} docker compose up
```
Where ${COUNTRY} is the choosen language, it or en. While ${HOST_NAME} is the external keycloak url, for example auth-ct.vige.it .
${REPLACER_CLIENT_ADDRESS} is the address of the cities-generator client, for example cities-generator-service.vige.it:8743
${AUTHURL} is the keycloak address url like https://auth-ct.vige.it:8443
${PASSWORD_STORE} is the password for the SSL/TLS keystore files. Default: `password`.
It will allow to download a keycloak instance where the server can be connected. 
After you can connect to keycloak through the url https://auth-ct.vige.it:8443

Here a sample:
COUNTRY=it REPLACER_CLIENT_ADDRESS=cities-generator-service.vige.it:8743 HOST_NAME=auth-ct.vige.it AUTHURL=https://auth-ct.vige.it:8443 docker compose up

Add the following DNS in your /etc/hosts file:
```
$IP_ADDRESS cities-generator-service.vige.it
$IP_ADDRESS auth-ct.vige.it
```
where in $IP_ADDRESS you must choose the ip address where is located the server.
To use the service connect through browser to https://cities-generator-service.vige.it:8743/swagger-ui/index.html

If you need to make write operations you can log through:
```
user: root  
pass: gtn
```

#### certificates

in a production environment we are using a default certificate but you could move a different ssl certificate and keys. Use this command to generate it:
```
keytool -genkey -alias cities-generator-service -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore ./docker/prod/volume/cert/application-ct.keystore -validity 3650 -dname "CN=cities-generator-service.vige.it, OU=Vige, O=Vige, L=Rome, S=Italy, C=IT" -storepass password -keypass password
```
You need to create a certificate and import it through the command:
```
keytool -v -export -file mytrustCA.cer -keystore ./docker/prod/volume/cert/application-ct.keystore -alias cities-generator-service
keytool -import -alias trustedCA -file mytrustCA.cer -keystore ./docker/prod/volume/cert/application-ct.keystore -storepass password -keypass password
```

Actually two samples of generated cities can be found online:

https://raw.githubusercontent.com/flashboss/cities-generator/master/cities/it.json
https://raw.githubusercontent.com/flashboss/cities-generator/master/cities/gb.json

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

```
cd frontend
npm install
```

### Development

Start the development server:

```
npm run dev
```

### Build

Build all variants (UMD, Standalone, Web Component):

```
npm run build
```

Or build specific variants:

```
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
  country="it" 
  placeholder="Select location...">
</cities-dropdown>

<!-- Using custom base URL (automatically appends /it.json) -->
<cities-dropdown 
  country="it" 
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
    country: 'it',
    placeholder: 'Select location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
  
  // Using custom base URL (automatically appends /it.json)
  CitiesGenerator.render('#my-dropdown', {
    country: 'it',
    dataUrl: 'https://example.com/cities',
    placeholder: 'Select location...',
    onSelect: (node) => {
      console.log('Selected:', node);
    }
  });
  
  // Using search functionality
  CitiesGenerator.render('#my-dropdown', {
    country: 'it',
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
        country="it"
        placeholder="Select location..."
        onSelect={(node) => console.log(node)}
      />
      
      {/* Using custom base URL (automatically appends /it.json) */}
      <CitiesDropdown
        country="it"
        dataUrl="https://example.com/cities"
        placeholder="Select location..."
        onSelect={(node) => console.log(node)}
      />
      
      {/* Using search functionality */}
      <CitiesDropdown
        country="it"
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
- `dataUrl` (string, optional): Base URL for remote data source. If not specified, uses default GitHub URL: `https://raw.githubusercontent.com/flashboss/cities-generator/master/_db/europe/{country}.json`. If specified, treated as base URL and automatically appends `/{country}.json` (any `.json` extension in the URL is automatically removed)
- `country` (string, optional): Country code, e.g., "it", "gb" (default: "it")
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
<cities-dropdown country="it"></cities-dropdown>

<!-- Using custom base URL (automatically appends /it.json) -->
<cities-dropdown 
  country="it" 
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
{{ attach_library('your_theme/cities_dropdown') }}

{# Using default parameters #}
<cities-dropdown />

{# Using default URL #}
<cities-dropdown country="it"></cities-dropdown>

{# Using custom base URL (automatically appends /it.json) #}
<cities-dropdown country="it" data-url="/sites/default/files/cities"></cities-dropdown>
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
<cities-dropdown country="it"></cities-dropdown>

<%-- Using default URL --%>
<cities-dropdown country="it"></cities-dropdown>

<%-- Using custom base URL (automatically appends /it.json) --%>
<cities-dropdown country="it" data-url="<%= themeDisplay.getCDNBaseURL() %>/o/cities-generator/data"></cities-dropdown>
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
<cities-dropdown country="it"></cities-dropdown>

<!-- Using custom base URL (automatically appends /it.json) -->
<cities-dropdown country="it" data-url="<?php echo JURI::root(); ?>data"></cities-dropdown>
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