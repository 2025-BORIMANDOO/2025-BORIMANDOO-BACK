# 1. Java 21을 위한 경량 JDK 이미지 사용
FROM eclipse-temurin:21-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 이미지에 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. 프로파일 환경변수 기본값 설정 (선택)
ENV SPRING_PROFILES_ACTIVE=api

# 5. JAR 실행 명령
ENTRYPOINT ["java", "-jar", "/app/app.jar"]