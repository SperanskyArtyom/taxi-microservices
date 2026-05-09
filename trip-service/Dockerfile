FROM gradle:9.4.1-jdk25 AS build
WORKDIR /app

COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN sed -i '/user-service/d' settings.gradle.kts

COPY common common
COPY trip-service trip-service

RUN gradle :trip-service:bootJar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/trip-service/build/libs/*.jar trip-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "trip-service.jar"]