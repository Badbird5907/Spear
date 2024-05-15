plugins {
    id("java")
}

group = "dev.badbird.spear"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.1.3")
    implementation(rootProject)
}
