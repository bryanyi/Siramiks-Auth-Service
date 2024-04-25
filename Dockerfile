
FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} authservice.jar

# start the jar file
ENTRYPOINT ["java", "-jar", "/authservice.jar"]

# expose port
EXPOSE 7070
