FROM openjdk:12
WORKDIR /app/server/
COPY . .
RUN javac Server.java
EXPOSE 4004
CMD ["java", "Server"]