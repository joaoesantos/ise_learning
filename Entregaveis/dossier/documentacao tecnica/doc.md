Welcome to the ise_learning wiki!

# Web-client Application
The web-client application can be run using two modes. One as a totally different application from the services application or can be ran as single application with the services application.
For the former the following commands need to be ran when in the folder containing the package.json file, i.e, Code\web-client:
* npm install
* npm start
The last command will start the web application in the predefined port, i.e, 3000.

To run the web-client and services applications as a single artifact, the following commands need to be ran:
* npm install
* npm run-script build
The last command will copy production ready files into the resource folder of the services application and then once this application is running, it will be available in the predefined port.


# Services

## REST API Documentation
https://joaoesantos.github.io/ise_learning/apiDocumentation/

## Properties
For the application to run as expected several properties which must be configured.

### Execution Environments Properties
for the service to call remote execution environments the application has to know the endpoints of each of those environments. To that effect a property file named "executionEnvironments.properties" must exist on the classpath with the following properties:
* execution.environment.java - String representing the remote Java execution environment endpoint, e.g. http://192.168.99.100:8081
* execution.environment.kotlin - String representing the remote Kotlin execution environment endpoint, e.g. http://192.168.99.100:8082
* execution.environment.javascript - String representing the remote JavaScript execution environment endpoint, e.g. http://192.168.99.100:8083
* execution.environment.c# - String representing the remote C# execution environment endpoint, e.g. http://192.168.99.100:8084
* execution.environment.python - String representing the remote Python execution environment endpoint, e.g. http://192.168.99.100:8085

### Database connection properties
To configure your own connection to a database add a properties file name "application.properties" to the resource folder. In this file the following properties must be present:
* iselearning.services.host - String representing the host of the machine that is running the database server, e.g. localhost
* iselearning.services.port - String representing the port of the machine where database server is running, e.g. 5432
* iselearning.services.database - String representing the name of the database in the database server, e.g. iselearning
* iselearning.services.username - String representing the username for authentication when accessing the database, e.g. postgres
* iselearning.services.password - String representing the password for authentication when accessing the database, e.g. pass123
* iselearning.services.driverClassName - String representing the Drive of the database provider, e.g. org.postgresql.Driver
* iselearning.services.providerUrl - String representing the database provider, e.g. jdbc:postgresql:

# Execution Environments

The execution environments allow execution inderectly throw HTTP. The environments expose an endpoint which can receive code in HTTP requests of a given language execute it and return the result. Below are described in more detail the execution environments for each language.

## Java & Kotlin
The Java&Kotlin execution environments include a SpringBoot Java Maven project which when deployed can receive and execute Java and Kotlin code respectively. It was developed so that it could be deployed on a Docker container. To deploy the developer can follow the steps:
1. Run the command* `mvn clean package` to produce the runnable jar file
2. Run the command* `docker build -t code language-execution-environment .` to build the Docker image
3. Run the command* `docker run -p 8081:8080 code language-execution-environment` to execute Docker image on a container
*commands executed on project root folder

## Node.js
The Node.js execution environments include a NodeJs Express project which when deployed can receive and execute Javascript. It was developed so that it could be deployed on a Docker container. To deploy the developer can follow the steps:
1. Run the command* `docker build -t nodejs-execution-environment .` to build the Docker image
3. Run the command* `docker run -p 8083:3500 nodejs-execution-environment` to execute Docker image on a container
*commands executed on project root folder

# Database
To store the platform data we're relying in [PostgreSQL](https://www.postgresql.org/), which is an open source object-relational database, well known for its strong reliability, feature robustness and performance.

## PostgreSQL set up
To configure the database on a PostgreSQL database a single master script was create, and can be found on the following link:
* [Create DB](..\Code\Postgres\createDB.sql)

This scrip includes not only the creation of the model with all its tables and dependencies, but also all the triggers and store procedures that allow a robust consistency of the database.

The single master script was created through the merge of several individual ones used on the development. The other scrips can be found as store procedures on the links bellows:
* [Store procedure to create the model](..\Code\Postgres\p_createTodel.sql)
* [Store procedure to drop the model](..\Code\Postgres\p_dropModel.sql)
* [Store procedure to delete the model](..\Code\Postgres\p_deleteModel.sql)
* [Store procedure to fill the model](..\Code\Postgres\p_fillModel.sql)
* [Store procedure to rebuild the model](..\Code\Postgres\p_rebuildModel.sql)
* [Store procedure to insert a challenge answer](..\Code\Postgres\p_insertChallengeAnswer.sql)
* [Store procedure to insert a questionnaire answer](..\Code\Postgres\p_insertQuestionnaireAnswer.sql)
* [Store procedure to create model triggers](..\Code\Postgres\DBtriggers.sql)