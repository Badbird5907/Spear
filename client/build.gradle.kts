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
}

tasks.shadowJar {
    archiveClassifier.set("");
}