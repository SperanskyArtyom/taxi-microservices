FROM gradle:9.4.1-jdk25 AS build
WORKDIR /app

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY common/build.gradle.kts common/
COPY trip-service/build.gradle.kts trip-service/

RUN sed -i '/user-service/d' settings.gradle.kts
RUN sed -i '/notification-service/d' settings.gradle.kts

RUN gradle dependencies --no-daemon

COPY common common
COPY trip-service trip-service

RUN gradle :trip-service:bootJar -x test --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/trip-service/build/libs/*.jar trip-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "trip-service.jar"]