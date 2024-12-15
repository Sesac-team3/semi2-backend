FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew
ENTRYPOINT ["java", "-jar", "SesacSemi2.jar"]
