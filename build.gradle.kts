plugins {
    id("java")
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ru.hofftech"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.telegram:telegrambots:6.9.7.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}