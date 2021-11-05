# Cities Generator
Generates a descriptor file for the cities choosing:

- **-c:** the country of the generated cities named by the first two characters for example **uk** and **it**. If not specified the default locale of the machine is used.
- **-s:** the case for the name of the cities. Can be true or false or none will be true as default.
- **-d:** true if you allow duplicated names of cities. Else none or false.
- **-p:** choose the first provider to create the file descriptor. You can choose for **uk**: BRITANNICA or GEONAMES. For **it**: COMUNIITALIANI, TUTTITALIA or EXTRAGEONAMES. For all other coutries the provider is GEONAMES. Else start a default.
- **-u:** a optional username to use for the providers: GEONAMES and EXTRAGEONAMES. If not specified **vota** is the default.

To generate the cities, you can choose between 3 modes:

- By a command line shell digit:
```
mvn org.apache.maven.plugins:maven-dependency-plugin:3.0.2:copy -Dartifact=it.vige.cities:cities-generator:1.1.8:jar -DoutputDirectory=. && java -jar cities-generator-1.1.8.jar -c uk
```
It will return a json file inside the ${user.home}/cities-generator dir

- Download the source and execute:
```
cd library;./gradlew build;java -jar build/libs/cities-generator-1.1.8.jar -c it
```

- Through api java follow the instructions:
	
1. On maven add the following script in the pom.xml file:
```
	<dependency>
		<groupId>it.vige.cities</groupId>
		<artifactId>cities-generator</artifactId>
		<version>1.1.8</version>
	</dependency>
```
	
   or on gradle in the build.gradle file:
		
   ```
	compile('it.vige.cities:cities-generator:1.1.8')
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
java -jar build/libs/cities-generator-service-1.1.8.jar --country=it --server.port=8380 --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```
Keycloak params are mandatory to connect to a custom keycloak server. It allows the authorization. To use the service connect through browser to http://localhost:8380/swagger-ui/index.html
In a production environment you could use https so:
```
java -Djavax.net.ssl.trustStore=./application.keystore -Djavax.net.ssl.trustStorePassword=password -jar build/libs/cities-generator-service-1.1.8.jar --server.ssl.key-store=./application.keystore --server.ssl.key-store-password=password --server.ssl.trust-store=./application.keystore --server.ssl.trust-store-password=password --server.port=8743 --country=it --keycloak.realm=${realm} --keycloak.auth-server-url=${url} --keycloak.resource=${resource}
```

### Docker

There is an already docker image to start the REST service. Execute this command to start the service:
```
docker pull vige/cities-generator
```
To run the image use the command:
```
docker run -d --name cities-generator -p8743:8443 -eCOUNTRY=it -eREALM=${realm} -eAUTHURL=${url} -eRESOURCE=${resource} vige/cities-generator
```
Where it is the chosen country. You can choose uk,it or other else country using the first two characters of the code.
Over the country, optionally as for the library you can add the following param:

- REALM
- AUTHURL
- RESOURCE
- COUNTRY
- PROVIDER
- CASESENSITIVE
- DUPLICATEDNAMES
- USERNAME

This image starts without SSO server, so it is not complete. For a sample complete environment you can start the command from the root folder of the project:
```
COUNTRY=${COUNTRY} docker-compose up
```
Where ${COUNTRY} is the choosen language, it or en. It will allow to download a keycloak instance where the server can be connected. 
After you can connect to keycloak through the url http://auth-ct.vige.it:8080/auth

Add the following DNS in your /etc/hosts file:
```
$IP_ADDRESS cities-generator-service.vige.it
$IP_ADDRESS auth-ct.vige.it
```
where in $IP_ADDRESS you must choose the ip address where is located the server.
To use the service connect through browser to http://cities-generator-service.vige.it:8380/swagger-ui/index.html

#### certificates

in a production environment we are using a default certificate but you could move a different ssl certificate and keys. Use this command to generate it:
```
keytool -genkey -alias cities-generator-service -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore ./application.keystore -validity 3650 -dname "CN=cities-generator-service.vige.it, OU=Vige, O=Vige, L=Rome, S=Italy, C=IT" -storepass password -keypass password
```
You need to create a certificate and import it through the command:
```
keytool -v -export -file mytrustCA.cer -keystore ./application.keystore -alias cities-generator-service
keytool -import -alias trustedCA -file mytrustCA.cer -keystore ./application.keystore -storepass password -keypass password
```
