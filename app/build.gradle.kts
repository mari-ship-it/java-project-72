plugins {
    application
    jacoco
    checkstyle
    id("io.freefair.lombok") version "8.13.1"
    id("org.sonarqube") version "7.1.0.6387"
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

    implementation(platform("com.konghq:unirest-java-bom:4.5.1"))
    implementation("com.konghq:unirest-java-core")
    implementation("com.konghq:unirest-modules-gson")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("io.javalin:javalin-testtools:6.7.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    } }

sonar {
    properties {
        property ("sonar.projectKey", "mari-ship-it_java-project-72")
        property ("sonar.organization", "mari-ship-it")
        property ("sonar.host.url", "https://sonarcloud.io")
        property ("sonar.coverage.jacoco.xmlReportPaths",
        "${buildDir}/reports/jacoco/test/jacocoTestReport.xml")
    }
}
