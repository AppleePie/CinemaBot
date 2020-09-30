#
# Package stage for server
#
FROM openjdk:15
WORKDIR /usr/local/
COPY ./target/MongoAPI.Server-jar-with-dependencies.jar /usr/local/lib/server.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/server.jar"]