#
# Package stage for client
#
FROM openjdk:15
WORKDIR /usr/local/
COPY ./target/Bot.Main-jar-with-dependencies.jar /usr/local/lib/client.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/client.jar"]