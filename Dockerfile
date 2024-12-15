FROM openjdk:17-jdk-slim
WORKDIR /app

COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar /app/SesacSemi2.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "SesacSemi2.jar"]
