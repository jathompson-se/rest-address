# SDET Code Challenge - V2 
implemented by Jennifer A Thompson, jathompson.se@gmail.com,  https://www.linkedin.com/in/jathompson-se/

## Challenge Outline:
As part of a testing framework, we need a method to externalize some of our test data to a REST HTTP service that can generate a random address.

Note that the address does NOT have to be logically/shipping valid up to a country level. However, it is supposed to be in a human-readable country mailing format. As an example, “532 Union St., Apt 72, San Diego, NY, 789302” is acceptable even though this address won’t pass USPS validation.
In addition, limit the set of countries to 4:
- US
- Canada
- Mexico
- Netherlands

The response must be in a human-readable format such as JSON, XML, YAML, TOML, etc. The implementation should follow best development practices and should preferably be a Java + Spring Boot solution, but you may if needed, choose another object-oriented language that you are familiar with.

### Technical details: 
#### Endpoint: ####
GET /randomizer/address

#### Return object definition: ####
Fields | type:
- house | String
- street | String
- postalCode | String                          
- City | String
- county | String
- state | String; optional
- stateCode | String; optional
- country | String; optional
- countryCode | String (ISO 3166-1 alpha-3 code) 

## Assumptions and Design Considerations
1. Addresses do not include PO Boxes, FPOs or APOs.
1. Street name may also include apartment, unit number etc. 
1. "City" is a typo and the city field will be returned as "city" in the JSON object response.
1. A specific country can be requested via the REST call by adding the `country=<countryCode>` parameter. This will generate a random address for the requested country. 
1. If no country passed to the REST call, a country will be pseudo-randomly chosen from the list of implemented countries. 
1. If an invalid country is passed to the REST service, it should return a 404 response. 
1. Design & implementation should account for more than 4 country implementations in the future. 
1. State and state code (if set) should return equivalent results. 
1. The Netherlands do not have counties or equivalent entities, so county will be set to the same value as city. 
1. Data is in UTF-8 encoding
1. Values for non-trivial data (street, city, county, state, stateCode & country) will be selected pseudo-randomly from a list of possible values. 
1. Optional fields in the specification will returned pseudo-ramdonly. The rate a field is returned is determined by variables set in a Java interface.
1. Having variablility in the generated address is important, so abbeviations and codes can use lowercase uppercase or both. For example, NY could be ny or Ny.
1. Values for a field should be in the language appropriate for the country/region but appropriate values can be shared. So English speaking Canadian provences can share the same street, city and county data as the US. Addresses in Quebec will use French data. 
1. Source data should cover a variety of cases with white spaces, use of special characters, etc. 
1. Providing 50-200 options per field should sufficient variety for the purposes of this tool\challenge. 
This will allow for the values to be quickly loaded and accessed in memory while retaining a small memory footprint. 
If more options are required, the application's memory footprint may expand to the point where it will consume a non-desirable portion of the heap. 
In this case a database could be used in lieu of caching the values in memory.
1. Advantages of using file over a database: 
    - fast access to data in memory
    - fewer points of failure
    - less configuration
    - easy to verify file encoding 
    - data accessed fast from memory
1. Disadvantages of using flat files: 
    - time to load the data from the files into memory
    - data's memory footprint
    - source data could be corrupted inadvertently by a user
1. Data should be contained in one class so the application can be easily updated to use additional files/data. This will also be useful if implementation changes to use a database. 
1. The application will run in a multi-threaded enviornment. Therefore java.util.concurrent.ThreadLocalRandom should be used for creating pseudo-random numbers to reduce thread contention.  
1. Running a REST service on an insecure port may not be desired unless the application is running in an environment isolated from the internet. There should be an option to run via HTTPS. For future consideration, if HTTPS is always needed, write code to redirect http requests to https.
1. Factory design pattern will be used with an abstract class as the parent. An abstract class is better than an interface in this case because it can implement the getX() methods required by the Spring Boot framework as well as define methods which will useful to more than one child class. 
1. This abstract class will also implement an interface which has constants defined. I chose to use an interface rather than leveraging application.properties so the the application is more stable since illogical values would cause unexpected behavior. 


## Restrictions & Limitations
1. First REST call will take longer than subsequent calls since this will trigger loading the data from files into the java heap. For load tests, call the REST service prior to starting the test. 
1. State and statecode data are in separate files, but ordered so line X in state corresponds to the same line state in stateCode. This order should be maintained to keep the application returning equivalent values. If the source files need to be updated frequently or maintaining the order is important, the data/code can be restructured to ensure this relationship holds. 
1. Data in StreetsNames_EN contains more variability than its non-English counterparts. Future versions should expand this data to cover more cases. 

## Project Design
The parent/super class is an abstract class `Address` which implements the `AddressConstants`. There are 4 implemented child classes:
 - `AddressCAN`
 - `AddressUSA`
 - `AddressMex`
 - `AddressNLD` 
 
 which implement the county's specific address requirements. The Spring Boot code will process the classes as `Address` and remain agnostic of the implementation type. 
 
 `AddressConstants` defines static variables which determine the rate of variation for address generation. An interface is used to group the values for code maintenance/config updates. 
 
 The `AddressFactory` is used to create Addresses the type of country address requested by the `AddressController`. If no country is specified, a country implementation will be 
 pseudo-randomly selected by the factory. 
 
 `AddressData` enables all data to be loaded and stored in one class. It loads the data into `public static ArrayLists` limited to 1 memory footprint per JVM and accessed via an index. 
 This class was placed the `util` package since there could be a need for additional helper classes as the number of implementations increase. Additional tools may be able to leverage the data
 stored in `AddressHelper`. The data loaded into this class is located under `rest-address/src/main/resources/data`. 

## Project Delivery
This implementation uses Spring Boot 2.3.1 & openjdk version "11.0.7" 2020-04-14 LTS

The REST service response will be returned as a JSON object. 

The application is contained in one maven module named `rest-address`. 

Javadoc is available at `rest-address/javadoc`. To view the documentation, open the index.html file with a web browser.


## Running JUnit tests
If maven is installed, 6 JUnit tests can be run. Open a command prompt and navigate to the project's `rest-address` folder. Then run:
```
mvn clean test
```

## Application Configuration
### Ports 
By default this application will run via HTTP on port 8080. To update the default port, open the `rest-address/src/main/resources/application.properties` file, remove the `#` in front of `server.port` and set the desired port. 

### HTTPS
As noted previously, running a REST service via HTTP in an environment with internet access poses a security risk. This project contains a self-signed certificate if you wish to run with SSL. 

To enable SSL, open `rest-address/src/main/resources/application.properties` and uncomment the block of properties under the `#Remove comments below to run via SSL` comment. To run on a port other than 8443, update the `server.port` property to the desired port.

_Note: The REST client will need import the self-signed cert into its truststore. The self-signed certificate is availabe at `rest-address/src/main/resources/ssl-server.jks`_

### Debug Logging
To enable debug logging for this project, open `rest-address/src/main/resources/application.properties` and change
```
logging.level.jathompson.randomizer.restaddress=INFO
logging.level.jathompson.randomizer.restaddress.util=INFO
```
to 
```
logging.level.jathompson.randomizer.restaddress=DEBUG
logging.level.jathompson.randomizer.restaddress.util=DEBUG
```

## Running The Application
The application may be run in 2 ways:
### Maven: 
If maven is installed (or you wish to install a version compatible with Spring Boot 2.3.1), open a command prompt and navigate to
 the `rest-address` folder of the project (the directory should contain a pom.xml file). From a command prompt issue this command:

```
./mvnw spring-boot:run
```

The REST service is available when a message similar to the following is displayed:
```
 2020-07-07 05:40:57.332  INFO 12932 --- [           main] j.r.restaddress.RestAddressApplication   : Started RestAddressApplication in 2.752 seconds (JVM running for 3.36)
 ```
### Jar file: 
The `rest-address/target` directory contains an executable jar with the required dependencies. Open a command prompt, navigate to `rest-address/target` 
and ensure Java is in the path. Then execute the following command:
 ```
 java -jar rest-address-0.0.1-SNAPSHOT.jar
 ```
 The REST service is available when a message similar to the following is displayed:
 ```
 2020-07-07 05:40:57.332  INFO 12932 --- [           main] j.r.restaddress.RestAddressApplication   : Started RestAddressApplication in 2.752 seconds (JVM running for 3.36)
 ```

## Application Usage:
### For HTTP (default config):
The REST service is available at:
 http://localhost:8080/randomizer/address
 
_Note: If using a non-default port, then update the port in the url above to match the value set in application.properties._ 

This URL can also accept a country argument matching the ISO 3166-1 alpha-3 code for implemented countries. This will return a random address for the specified country. 

For example:

http://localhost:8080/randomizer/address?country=USA

returns a random US address. 

#### Invoking the REST Service:
Use a web browser or the curl command to invoke the service and receive a random address JSON response. 

#### To stop the application:
enter `ctrl-c`

###  For HTTPS (if configured):
The REST service is available at:
https://localhost:8443/randomizer/address

_Note: If using a non-default port, then update the port in the url above to match the value set in application.properties._ 

This URL can also accept a country argument matching the ISO 3166-1 alpha-3 code for implemented countries. This will return a random address for the specified country. 

For example: 

https://localhost:8443/randomizer/address?country=USA

returns a random US address. 

#### Invoking the REST Service:
Use a web browser or the curl command to invoke the service and receive a random address JSON response. 

#### To stop the application:
enter `ctrl-c`
