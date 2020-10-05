#
# Build stage
#
FROM maven:3.6.3-openjdk-15 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage for client
#
FROM openjdk:15
WORKDIR /usr/local/
COPY --from=build /home/app/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/server.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/server.jar"]