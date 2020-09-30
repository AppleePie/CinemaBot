#
# Build stage
#
FROM maven:3.6.3-openjdk-14
COPY src /home/app/src
COPY pom.xml /home/app/pom.xml
RUN mvn /home/app/pom.xml clean package