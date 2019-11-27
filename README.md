# cities-generator
Generates a descriptor file for the cities choosing:

- c: the country of the generated cities. Mandatory. Actually EN and IT are supported.
- s: the case for the name of the cities. Can be true or false or none will be true as default.
- d: true if you allow duplicated names of cities. Else none or false
- p: choose the first provider to create the file descriptor. You can choose for EN: BRITANNICA. For IT: COMUNIITALIANI or TUTTITALIA. Else start a default

To generate the cities, you can choose between 3 modes:

1 - By a command line shell digit: mvn archetype:generate -Dfilter=it.vige.cities-generator and follow the instructions. It will return a json file

2 - Download the source and execute: ./gradlew build;cd build;java -jar build/libs/cities-generator-0.0.1-SNAPSHOT.jar

3 - Through api java follow the instructions:
	
	1 - on maven add the following script in the pom.xml file:
	```
			<dependency>
				<groupId>it.vige.cities</groupId>
				<artifactId>cities-generator</artifactId>
				<version>1.0.0</version>
			</dependency>
	```
		or on gradle in the build.gradle file:
	```
		    compile('it.vige.cities:cities-generator:1.0.0')
	```
	
	2 - execute the following java instructions:
	```
		import it.vige.cities.Generator;
		import it.vige.cities.Countries;
		import it.vige.cities.result.Nodes;
		...
		Generator generator = new Generator(Countries.IT.name(), false, false);
		Nodes result = generator.generate();
		System.out.println(result.getZones());
	```
