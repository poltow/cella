# cella

Task Description:
---------------------
Create a simple application that allows to manage products.
It should consist of:

- REST API
- service layer
- DAO layer
- relational database

Product attributes:

    Name
    Description
    Weight
    Price
    Country


API endpoints:
- create new product
- update existing product
- delete a product
- get list of product (pagination, sorting by name, search by name)

Technologies to use: spring boot, spring framework, spring data, MySQL db.

Code Repo: Please use GitHub repo so we can check the code when it is ready

Important:
- It is important to show well-organised and clean code
- No Front end code is required. Java code only
- Examples of Unit tests would be a plus (no need for full coverage)
- DB should contain only one table (Product). Thinks of required indexes
- The code should be well-readable



SOLUTION
---------------------

Please note that you may have to edit the .\src\main\resources\application.yml file in order
to properly set up Mysql DB access.

Tech Stack
- Java 11
- Spring Boot
- Maven
- MySql 8
- Swagger
- Jackson
- Jacoco

In order to run the app it is possible to use mvn commands (Mysql or H2 DB options)
   
For mysql:

    mvn spring-boot:run -Dspring-boot.run.profiles=local

For H2:

    mvn spring-boot:run -Dspring-boot.run.profiles=test

It is possible to check the health of the service by running the command below:

    curl -X POST http://localhost:8080/message/echo -H "accept: application/json" -H "cache-control: no-cache" -H "content-type: application/json" -d "{\"message\":\"mensaje de prueba\"}"

For getting a code coverage report:

1) Run:
    
    mvn clean package

2) Open the report file below:

   .\target\site\jacoco\index.html

To get API information and test all the calls, open this url in a browser:

    http://localhost:8080/swagger-ui.html

To use postman to test the project, please import the Cella.postman_collection.json file to postman
