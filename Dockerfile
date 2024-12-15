FROM openjdk:17-jdk-slim
WORKDIR /app
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar /app/SesacSemi2.jar
RUN chmod +x /app/gradlew
ENTRYPOINT ["java", "-jar", "SesacSemi2.jar"]
