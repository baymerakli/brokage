# Build stage
FROM maven:3.8.4-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY openapi /home/app/openapi
COPY wait-for-it.sh /home/app
RUN mvn -f /home/app/pom.xml clean test -P test package

# Package stage
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
