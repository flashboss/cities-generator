---
layout: guide
title: Cities Generator Library
permalink: /library/
---

# Cities Generator Library

Java library for generating descriptor files with cities data. The library can generate JSON files containing hierarchical location data (regions, provinces, municipalities) for various countries.

## Features

- Generate cities data for multiple countries (IT, GB, and more)
- Support for multiple languages (IT, EN, FR, DE, ES, PT)
- Multiple data providers (Geonames, OpenStreetMap, Wikipedia, etc.)
- Export to JSON format
- Publish directly to Git repositories
- Command-line interface
- Java API for programmatic usage

## Command Line Usage

### Basic Parameters

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

## Usage Modes

To generate the cities, you can choose between 3 modes:

### Mode 1: Command Line (Maven)

By a command line shell digit:

```bash
mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.0:copy -Dartifact=it.vige.cities:cities-generator:1.2.7:jar -DoutputDirectory=. && java -jar cities-generator-1.2.7.jar -c GB -l en
```

It will return a json file inside the ${user.home}/cities-generator/EU/GB/en.json (structure: {continent}/{country}/{language}.json)

### Mode 2: Build from Source

Download the source and execute:

```bash
cd library
./gradlew build
java -jar build/libs/cities-generator-1.2.7.jar -c IT -l it
```

### Mode 3: Java API

Through API java follow the instructions:

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

2. Execute the following java instructions:

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

## Geonames Registration

If you use GEONAMES or EXTRAGEONAMES, you use a default username. In the long run this default username may be inactive, so you will need a new username to specify in the configuration field seen above. To get the new username you must register through the site: <https://www.geonames.org/login>

## OpenStreetMap Provider

The OPENSTREETMAP provider uses the Overpass API to retrieve administrative boundaries (regions, provinces, and municipalities) from OpenStreetMap data. This provider:

- **Works for all countries** supported by OpenStreetMap
- **Includes 3 levels**: Regions (level 0), Provinces/Cities (level 1), and Municipalities (level 2)
- **Supports multiple languages** for location names
- **For Italy**: Use EXTRA_OPENSTREETMAP to get macroregions (level 0) grouping regions by geographical area, or OPENSTREETMAP for standard regions

**Note**: OpenStreetMap queries may take longer than other providers due to the number of API calls required (approximately 1 + number of regions + number of provinces queries per country).

## Data Output

Actually samples of generated cities can be found online:

- <https://raw.githubusercontent.com/flashboss/cities-generator/master/_db>

The structure is {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)

## Maven Central

This library is available on Maven Central:

```xml
<dependency>
    <groupId>it.vige.cities</groupId>
    <artifactId>cities-generator</artifactId>
    <version>1.2.7</version>
</dependency>
```

See the [Maven Central page](https://mvnrepository.com/artifact/it.vige.cities/cities-generator) for more information.

