FROM openjdk:12
WORKDIR /app/client/
COPY . .
EXPOSE 4003
RUN javac Main.java
CMD ["java", "Main"]