# Cities Generator
Generates a descriptor file for the cities choosing:

- **-c:** the country of the generated cities. Actually **en** and **it** are supported. If not specified the default locale of the machine is used.
- **-s:** the case for the name of the cities. Can be true or false or none will be true as default.
- **-d:** true if you allow duplicated names of cities. Else none or false
- **-p:** choose the first provider to create the file descriptor. You can choose for **en**: BRITANNICA. For **it**: COMUNIITALIANI or TUTTITALIA. Else start a default

To generate the cities, you can choose between 3 modes:

- By a command line shell digit:
```
mvn org.apache.maven.plugins:maven-dependency-plugin:3.0.2:copy -Dartifact=it.vige.cities:cities-generator:1.1.0:jar -DoutputDirectory=. && java -jar cities-generator-1.1.0.jar -c en
```
It will return a json file inside the ${user.home}/cities-generator dir

- Download the source and execute:
```
cd library;./gradlew build;java -jar build/libs/cities-generator-1.1.0.jar -c it
```

- Through api java follow the instructions:
	
1. On maven add the following script in the pom.xml file:
```
	<dependency>
		<groupId>it.vige.cities</groupId>
		<artifactId>cities-generator</artifactId>
		<version>1.1.0</version>
	</dependency>
```
	
   or on gradle in the build.gradle file:
		
   ```
	compile('it.vige.cities:cities-generator:1.1.0')
   ```
	
2. Execute the following java instructions:
```
	import it.vige.cities.Generator;
	import it.vige.cities.Countries;
	import it.vige.cities.result.Nodes;
	...
	Generator generator = new Generator(Countries.IT.name(), false, false);
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

## Docker

There is an already docker image to start a REST service. This returns a json format with the cities of the used country according the roles seen over. Execute this command to start the service:
```
docker pull vige/cities-generator
```
To run the image use the command:
```
docker run -d --name cities-generator -p8743:8443 -eCOUNTRY=it vige/cities-generator
```
Where it is the chosen country. Actually you can choose en or it.
Add the following DNS in your /etc/hosts file:
```
$IP_ADDRESS cities-generator.vige.it
```
where in $IP_ADDRESS you must choose the ip address where is located the server.
To use the service connect through browser to `https://cities-generator.vige.it:8743/swagger-ui.html`