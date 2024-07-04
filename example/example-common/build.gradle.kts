plugins {
    id("java")
}

group = "dev.badbird.spear"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
}