FROM openjdk:17-jdk-slim

# Gradle 설치
RUN apt-get update && apt-get install -y gradle

WORKDIR /app

COPY . /app

# Gradle로 빌드
RUN gradle clean build

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar"]
