FROM gradle:9.4.1-jdk25 AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY user-service user-service

RUN gradle :user-service:bootJar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/user-service/build/libs/*.jar user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "user-service.jar"]