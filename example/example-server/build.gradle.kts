plugins {
    id("java")
}

group = "dev.badbird.spear"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":server"))
    implementation(project(":common"))
    implementation(project(":example:example-common"))
    implementation("io.javalin:javalin:6.1.3")
}