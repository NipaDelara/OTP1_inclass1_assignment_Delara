## Temperature Converter Application

Individual In-Class Assignment
Name: Delara Nipa

### 1. Assignment Description

The objective of this assignment is to develop a Java-based Temperature Converter 
application with a graphical user interface, database integration, automated testing, 
and Docker support.

The application allows users to convert temperatures between Celsius, Fahrenheit, 
and Kelvin.

##### Main functionalities:
* Convert Celsius to Fahrenheit.
* Convert Fahrenheit to Celsius.
* Convert Kelvin to Celsius.
* Check extreme temperatures.
* Provide a graphical user interface using JavaFX.
* Save and retrieve temperature conversion data using MariaDB.

##### Deliverables:

### 2. Technologies & Tools Used
* Java 21 - Main programming language
* JavaFX - Graphical user interface
* Maven - Build and dependency management
* MariaDB - Database management
* JDBC - Database connectivity
* JUnit 5 - Unit testing
* JaCoCo - Code coverage analysis
* Docker - Containerization
* Jenkins - Continuous integration
* IntelliJ IDEA - Development environment
* Git & GitHub - Version control

### 3. Design Approach & Implementation Method

The application was developed using object-oriented programming principles.

#### Temperature Conversion:
The TemperatureConverter class contains the temperature conversion methods.

* celsiusToFahrenheit()

* fahrenheitToCelsius()

* kelvinToCelsius()

* isExtremeTemperature()

#### Graphical User Interface:

The TemperatureConverterGUI class uses JavaFX to provide an interactive interface where users can enter temperature values, select conversions, and view results.

#### Database Integration:

MariaDB is used for storing temperature conversion information.

DBConnection manages database connectivity.

TemperatureUnit represents temperature unit data.

TemperatureUnitDAO handles database operations using JDBC.

#### Docker and Jenkins:

Docker is used to package the application into a container. 
Jenkins automates building and testing through a pipeline.

### 4. Testing & Quality Assurance Steps

The application uses JUnit 5 for automated testing and JaCoCo for code coverage.

#### Automated Testing:

The TemperatureConverterTest class tests the temperature conversion methods.

#### Test Cases:


1. [x] Celsius 0°C to Fahrenheit - 32°F
2. [x] 
3. [x] Fahrenheit 212°F to Celsius - 100°C
4. [x] 
5. [x] Kelvin 273.15 K to Celsius - 0°C
6. [x] 
7. [x] Extreme temperature check - Correct Boolean result
8. [x] 
9. [x] Database connection - Successful connection

#### Testing Commands:

##### Run automated tests:

_mvn test_

##### Build and test the application:

_mvn clean install_

##### Generate a code coverage report:

_mvn clean verify_

#### Code Coverage:

##### JaCoCo generates the code coverage report at:

_target/site/jacoco/index.html_

#### Manual Testing:

* Launch the JavaFX GUI.
* Enter temperature values.
* Verify conversion results.
* Check database connectivity.
* Verify data saving and retrieval.
* Run the application in Docker using an X Server.

#### Testing Results:

The Maven build and automated tests completed successfully during development. 
The JavaFX application was also tested with Docker, X Server, and MariaDB.

### 5. How to Run
#### Prerequisites

Install the following:

* Java JDK 21
* Apache Maven
* MariaDB
* Docker Desktop
* An X Server for running the Docker GUI

#### Step 1: Clone the Repository

Clone the individual assignment repository:

git clone https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara.git

Open the project directory.

#### Step 2: Configure MariaDB

Start MariaDB and create the database:

CREATE DATABASE temperature_converter;

Configure the database connection in the DBConnection class.

#### Step 3: Build the Project

_mvn clean install_

#### Step 4: Run Unit Tests

_mvn test_

#### Step 5: Run the JavaFX Application

Open the project in IntelliJ IDEA and run the TemperatureConverterGUI main class.

#### Step 6: Generate Code Coverage

_mvn clean verify_

_Open target/site/jacoco/index.html to view the coverage report._

#### Step 7: Docker

##### Build the Docker image:

_docker build -t temperature-converter ._ 

Running the graphical application in Docker requires X Server configuration and database connectivity.

##### Docker Hub Image:

https://hub.docker.com/repository/docker/nipa93/temperature-converter/general

### Conclusion
This assignment demonstrates Java programming, object-oriented design, graphical user interface development, 
database integration, automated testing, code coverage, and Docker containerization.
