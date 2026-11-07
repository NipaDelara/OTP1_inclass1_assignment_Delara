# Temperature Converter Application

**Individual In-Class Assignment**  
**Name:** Delara Nipa  
**Project:** OTP1 In-Class Assignment  
**Repository:** https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara

## 1. Assignment Description

The objective of this assignment is to develop a Java-based Temperature Converter application with a graphical user interface, database integration, multilingual support, automated testing, and containerization.

The application allows users to convert temperatures between Celsius, Fahrenheit, and Kelvin and save conversion history in MariaDB.

### Main Functionalities

- Convert Celsius to Fahrenheit.
- Convert Fahrenheit to Celsius.
- Convert Kelvin to Celsius.
- Check extreme temperatures.
- Provide an interactive JavaFX graphical interface.
- Support English, Finnish, Swedish, Japanese, and Arabic.
- Save and retrieve temperature conversion records using MariaDB.
- Store localized temperature unit information in the database.
- Run the application using Docker and an X server.

## 2. Technologies and Tools Used

| Technology | Purpose |
|---|---|
| Java 21 | Main programming language |
| JavaFX | Graphical user interface |
| Maven | Build and dependency management |
| MariaDB | Database management |
| JDBC | Database connectivity |
| JUnit 5 | Automated unit testing |
| JaCoCo | Code coverage analysis |
| Docker | Containerization |
| Jenkins | Continuous integration |
| Kubernetes / Minikube | Container deployment |
| TigerVNC / Xvfb | Running the Docker GUI on macOS |
| IntelliJ IDEA | Development environment |
| Git and GitHub | Version control |

## 3. Design Approach and Implementation

The application was developed using object-oriented programming principles.

### Temperature Conversion

The `TemperatureConverter` and `TempCalculator` classes provide the temperature conversion functionality.

Methods include:

- `celsiusToFahrenheit()`
- `fahrenheitToCelsius()`
- `kelvinToCelsius()`
- `isExtremeTemperature()`

### Graphical User Interface

The `TemperatureConverterGUI` class uses JavaFX to provide an interactive interface.

Users can enter temperature values, select source and target units, change the interface language, convert temperatures, and view conversion history.

### Database Integration

MariaDB stores temperature units, translations, and conversion history.

The database integration uses the following classes:

- `DBConnection`: Manages database connections.
- `TemperatureUnitDAO`: Handles temperature unit database operations.
- `TempRecordDAO`: Handles conversion history database operations.

### Docker and Jenkins

Docker packages the application into a container.

Jenkins automates the build and testing process using a Jenkins pipeline.

Kubernetes and Minikube are used for container deployment and testing.

## 4. Database Localization Approach

### Chosen Approach: Separate Translation Table

I implemented database localization using a **separate translation table approach** in MariaDB.

The database contains three main tables:

| Table | Description |
|---|---|
| `temperature_unit` | Stores Celsius, Fahrenheit, and Kelvin with their symbols |
| `unit_translation` | Stores translated unit names and descriptions |
| `temp_record` | Stores temperature conversion history |

### Database Schema

**temperature_unit**

- `id`: Primary key
- `name`: Temperature unit name
- `symbol`: Temperature unit symbol

**unit_translation**

- `id`: Primary key
- `unit_id`: Foreign key referencing `temperature_unit`
- `lang`: Language code
- `name`: Translated unit name
- `description`: Localized description

**temp_record**

Stores the input temperature, output temperature, unit references, and conversion timestamp.

### Supported Languages

| Language | Code |
|---|---|
| English | en |
| Finnish | fi |
| Swedish | sv |
| Japanese | ja |
| Arabic | ar |

The database contains 15 translation records covering three temperature units across five languages.

### Reasons for Choosing This Approach

I chose the separate translation table approach because it separates language-independent temperature data from translated information.

This avoids creating additional columns for every supported language and reduces unnecessary duplication of temperature unit data.

It also makes the database easier to maintain and extend. New languages can be added by inserting new translation records without changing the existing table structure.

The `unit_id` foreign key connects each translation to the correct temperature unit, maintaining a clear relationship between the tables.

I tested the database using MariaDB SQL queries and verified that temperature units, translations, and conversion history were stored successfully.

## 5. Testing and Quality Assurance

The application uses JUnit 5 for automated testing and JaCoCo for code coverage.

### Test Cases

- [x] Celsius 0°C to Fahrenheit = 32°F
- [x] Fahrenheit 212°F to Celsius = 100°C
- [x] Kelvin 273.15 K to Celsius = 0°C
- [x] Extreme temperature checking
- [x] MariaDB connection
- [x] Saving conversion history
- [x] Retrieving conversion history
- [x] JavaFX GUI running through Docker and TigerVNC

### Testing Commands

Run automated tests:

```bash
mvn test
```

Build and test:

```bash
mvn clean install
```

Generate JaCoCo coverage:

```bash
mvn clean verify
```

Coverage report location:

```text
target/site/jacoco/index.html
```

### Manual Testing

The JavaFX application was tested by entering temperature values, selecting units, converting temperatures, and saving conversion history.

Example:

**30°C → 86°F**

The application displayed the correct result, and MariaDB logs confirmed successful saving.

## 6. How to Run

### Prerequisites

- Java JDK 21
- Apache Maven
- MariaDB
- Docker Desktop
- TigerVNC or another compatible VNC viewer for the Docker GUI

### Step 1: Clone the Repository

```bash
git clone https://github.com/NipaDelara/OTP1_inclass1_assignment_Delara.git
cd OTP1_inclass1_assignment_Delara
```

### Step 2: Configure MariaDB

Create the database:

```sql
CREATE DATABASE temperature_converter;
```

Initialize the database using the project's `init.sql` file.

Configure the database connection using the appropriate environment variables or local database settings.

### Step 3: Build the Project

```bash
mvn clean install
```

### Step 4: Run Unit Tests

```bash
mvn test
```

### Step 5: Run the JavaFX Application

Open the project in IntelliJ IDEA and run `TemperatureConverterGUI`.

Alternatively, run:

```bash
mvn javafx:run
```

### Step 6: Generate Code Coverage

```bash
mvn clean verify
```

Open:

`target/site/jacoco/index.html`

### Step 7: Docker

Build the image:

```bash
docker build -t temperature-converter .
```

The graphical application requires a virtual X display and VNC configuration when running inside Docker.

On macOS, the application was successfully tested using Xvfb, x11vnc, and TigerVNC.

### Docker Hub Image

https://hub.docker.com/r/nipa93/temperature-converter

Image:

`nipa93/temperature-converter:latest`

### Step 8: Kubernetes

Start Minikube:

```bash
minikube start
```

Deploy using the Kubernetes YAML configuration:

```bash
kubectl apply -f deployment.yaml
```

Verify the deployment:

```bash
kubectl get pods
```

The expected pod status is `Running`.

The Kubernetes commands should be executed using the deployment file included in the repository.

## 7. Jenkins CI/CD

Jenkins is used to automate the project build and testing workflow.

The pipeline includes:

1. Checkout source code from GitHub.
2. Run Maven tests.
3. Build the Java application.
4. Generate the JaCoCo coverage report.
5. Build the Docker image.
6. Push the Docker image to Docker Hub.

The Jenkins pipeline configuration is stored in the `Jenkinsfile`.

## 8. Submission Deliverables

The assignment submission includes:

1. **GitHub Repository:** Source code, `init.sql`, tests, `Jenkinsfile`, `Dockerfile`, Kubernetes YAML configuration, and README.
2. **Docker Hub Image:** Published Temperature Converter Docker image.
3. **GUI Screenshots:** JavaFX GUI in English, Japanese, and Arabic, running using an X server.
4. **Testing and Deployment Screenshots:** Successful Jenkins pipeline, JaCoCo coverage report, and Kubernetes pod status.
5. **Database Localization Explanation:** Description of the chosen translation table approach, schema, and reasons for choosing it.

## 9. Conclusion

This assignment demonstrates practical experience in Java programming, object-oriented design, JavaFX GUI development, MariaDB database integration, JDBC, multilingual localization, automated testing, code coverage, Docker containerization, Jenkins CI/CD, and Kubernetes.

The Temperature Converter was successfully tested through its JavaFX interface running in Docker, with conversion records saved in MariaDB.