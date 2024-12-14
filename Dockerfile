FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar /app/SesacSemi2.jar
ENTRYPOINT ["java", "-jar", "SesacSemi2.jar"]
