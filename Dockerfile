FROM openjdk:17-jdk-slim
WORKDIR /app

# Gradle Wrapper에 실행 권한 부여
COPY gradlew /app/gradlew
COPY gradle /app/gradle
RUN chmod +x /app/gradlew

# JAR 파일 복사
COPY build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar /app/SesacSemi2.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "SesacSemi2.jar"]
