plugins {
    id("java")
    id("io.freefair.lombok") version "8.1.0"
}

group = "dev.badbird.spear"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.javalin:javalin:6.1.3")
}
