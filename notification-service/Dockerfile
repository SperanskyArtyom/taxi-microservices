FROM gradle:9.4.1-jdk25 AS build
WORKDIR /app

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY common/build.gradle.kts common/
COPY notification-service/build.gradle.kts notification-service/

RUN sed -i '/trip-service/d' settings.gradle.kts
RUN sed -i '/user-service/d' settings.gradle.kts

RUN gradle dependencies --no-daemon

COPY common common
COPY notification-service notification-service

RUN gradle :notification-service:bootJar -x test --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/notification-service/build/libs/*.jar notification-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "notification-service.jar"]