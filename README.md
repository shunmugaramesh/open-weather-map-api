# open-weather-map-api

### Spec
* Implemented this API using Java 11 and Spring boot version 2.5.12.
* Created one Controller layer, service layer and a repository layer
* Exceptions handled for 400 Bad Request, 500 Internal Server Error, 404 Not Found
* Validated query params city and apikey as mandatorty params. Country is optional
  * city must be in alphabet.
  * apiKey must be of length 32 and must be alphanumeric.
  * Any failed cases, the API will return 400 Bad Request with valid message
* if API Key Usage limit exceeded more than 5 times in an hour, then API will return 500 error with the appropriate error message.
  * Used H2 database to store and update the usage of apiKey
* This API will directly call Open Weather Map service to retrieve weather JSON and return the description to the calling client. 
   * *Trade-off* : As per requirement the API stores the result from downstream service to H2 DB and then query from DB. Since I have covered the DB layer for API Key functionality, 
I didn't do save and retrieve the result from DB as they are repetitive.
* Covered test cases for most of the scenarios.

### List of API Keys created for the account.

* 60f4084d4a116eb07ef41ff89f64e1de 
* bd5de0b680bbcd26d0d413cc7da30b82
* fa9ee6bd5066c723fe9bf49dbe554eba
* 9416f40b60316a5c31c05f2613ad55f8
* dd5abe4be8575412c0cc3f888dee4bf5


### How To Run locally

* Goto root directory
* Execute mvn clean install
* Start the application with mvn spring-boot:run
* Hit the app with any REST client using http://localhost:8080/weatherapi/v1/description?city={city}&country={country}&apiKey={apiKey}} 
    Eg: http://localhost:8080/weatherapi/v1/description?city=Melbourne&country=Australia&apiKey=60f4084d4a116eb07ef41ff89f64e1de
* Access h2 database using URL http://localhost:8080/h2 Username : sa. No password.
    * Table and Columns will be auto created on app start up.
