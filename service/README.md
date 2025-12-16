# Cities Generator Service

REST service for accessing cities data. This service provides a REST API that returns JSON format with the cities of the used country according to the configured parameters.

## Features

- REST API for cities data
- Swagger UI documentation
- Keycloak integration for authentication
- Docker support (development and production)
- HTTPS support for production environments

## Building

To build the service, download the source under `/service` folder and execute:

```bash
./gradlew clean build
```

## Running

Start the service through the command:

```bash
java -jar build/libs/cities-generator-service-1.2.8.jar --country=IT --language=it --server.port=8380 --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```

Keycloak params are mandatory to connect to a custom keycloak server. It allows the authorization. To use the service connect through browser to <http://cities-generator-service.vige.it:8380/swagger-ui/index.html>

## Production Environment

In a production environment you could use https so:

```bash
java -Djavax.net.ssl.trustStore=./docker/prod/volume/cert/application-ct.keystore -Djavax.net.ssl.trustStorePassword=password -jar build/libs/cities-generator-service-1.2.8.jar --server.ssl.key-store=./docker/prod/volume/cert/application-ct.keystore --server.ssl.key-store-password=password --server.ssl.trust-store=./docker/prod/volume/cert/application-ct.keystore --server.ssl.trust-store-password=password --server.port=8743 --country=IT --language=it --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```

## Docker

### Development Image

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

### Production Image

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

## SSL Certificates

In a production environment we are using a default certificate but you could move a different ssl certificate and keys. Use this command to generate it:

```bash
keytool -genkey -alias cities-generator-service -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore ./docker/prod/volume/cert/application-ct.keystore -validity 3650 -dname "CN=cities-generator-service.vige.it, OU=Vige, O=Vige, L=Rome, S=Italy, C=IT" -storepass password -keypass password
```

You need to create a certificate and import it through the command:

```bash
keytool -v -export -file mytrustCA.cer -keystore ./docker/prod/volume/cert/application-ct.keystore -alias cities-generator-service
keytool -import -alias trustedCA -file mytrustCA.cer -keystore ./docker/prod/volume/cert/application-ct.keystore -storepass password -keypass password
```

