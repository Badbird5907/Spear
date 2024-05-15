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
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("jakarta.el:jakarta.el-api:6.0.0")
}
