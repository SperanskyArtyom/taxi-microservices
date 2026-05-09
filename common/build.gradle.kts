plugins {
    id("org.springframework.boot") apply false
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}