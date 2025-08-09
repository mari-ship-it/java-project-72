plugins {
    application
    jacoco
    checkstyle
    id ("org.sonarqube") version "6.2.0.5505"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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
