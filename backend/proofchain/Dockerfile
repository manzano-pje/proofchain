# -------- Build Stage --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src

RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package spring-boot:repackage -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]


## Build stage
#FROM eclipse-temurin:21-jdk AS build
#WORKDIR /app
#
## Copy maven wrapper and pom
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN chmod +x mvnw
#
## Download dependencies (cacheable)
#RUN ./mvnw dependency:go-offline
#
## Copy source and build
#COPY src ./src
#RUN ./mvnw clean package -DskipTests
#
## Run stage
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#
## Copy the built jar from the build stage
#COPY --from=build /app/target/*.jar app.jar
#
## Configuration for environment variables
#ENV SPRING_PROFILES_ACTIVE=prod
#ENV SERVER_PORT=8080
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]
