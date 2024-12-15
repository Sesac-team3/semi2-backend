FROM openjdk:17-jdk-slim
ARG JAR_FILE=./build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/SesacSemi2.jar"]
