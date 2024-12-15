# 1. OpenJDK 17 이미지를 기반으로 시작
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. gradlew 및 gradle 디렉토리 복사 (파일 권한을 복사 후 설정)
COPY gradlew /app/gradlew
COPY gradle /app/gradle

# 4. gradlew 파일에 실행 권한 부여
RUN chmod +x /app/gradlew

# 5. JAR 파일 복사
COPY build/libs/SesacSemi2-0.0.1-SNAPSHOT.jar /app/SesacSemi2.jar

# 6. 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/SesacSemi2.jar"]
