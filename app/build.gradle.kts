plugins {
    application
    jacoco
    checkstyle
    id("io.freefair.lombok") version "8.13.1"
    id ("org.sonarqube") version "6.2.0.5505"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.h2database:h2:2.3.232")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("gg.jte:jte:3.2.1")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("gg.jte:jte:3.1.9")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }

sonar {
    properties {
        property ("sonar.projectKey", "mari-ship-it_java-project-72")
        property ("sonar.organization", "mari-ship-it")
        property ("sonar.host.url", "https://sonarcloud.io")
    }
}
