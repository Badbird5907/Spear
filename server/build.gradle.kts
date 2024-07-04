plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

group = "dev.badbird.spear"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("io.javalin:javalin:6.1.3")
    compileOnly("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    compileOnly("com.google.inject:guice:7.0.0")
    implementation("org.reflections:reflections:0.10.2") {
        exclude(group = "org.slf4j")
    }
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.shadowJar {
    archiveClassifier.set("");
}