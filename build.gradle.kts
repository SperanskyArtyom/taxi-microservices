plugins {
    java
    id("org.springframework.boot") version "4.0.6" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "io.github.speranskyartyom.taxi-microservices"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    dependencies {
            implementation("org.springframework.boot:spring-boot-starter-security")
            implementation("io.jsonwebtoken:jjwt-api:0.12.5")

        if (name != "common") {
            runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
            runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
            implementation(project(":common"))
            implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")

            testImplementation("org.springframework.security:spring-security-test")
        }
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}