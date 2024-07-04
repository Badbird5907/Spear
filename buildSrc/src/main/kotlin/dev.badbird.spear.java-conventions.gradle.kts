plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.google.code.gson:gson:2.10.1")
}

group = "dev.badbird"
version = "2.0.0"

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}