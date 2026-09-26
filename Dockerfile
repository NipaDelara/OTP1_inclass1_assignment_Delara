FROM maven:3.9.6-eclipse-temurin-21 AS build

LABEL authors="Delara Nipa"

WORKDIR /app

COPY pom.xml .

COPY . /app

RUN mvn package

CMD ["java", "-jar", "target/TemperatureConverter-1.0-SNAPSHOT.jar"]