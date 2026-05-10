FROM gradle:9.4.1-jdk25 AS build
WORKDIR /app

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY common/build.gradle.kts common/
COPY user-service/build.gradle.kts user-service/

RUN sed -i '/trip-service/d' settings.gradle.kts
RUN gradle dependencies --no-daemon

COPY common common
COPY user-service user-service

RUN gradle :user-service:bootJar -x test --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/user-service/build/libs/*.jar user-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-service.jar"]