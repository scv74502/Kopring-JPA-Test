FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# 모든 파일을 한 번에 복사
COPY . .

# 빌드 실행
RUN gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]