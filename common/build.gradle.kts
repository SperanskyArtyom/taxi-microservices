plugins {
    id("org.springframework.boot") apply false
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    compileOnly("org.springframework.boot:spring-boot-starter-security")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}