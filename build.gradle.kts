plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.1.0"
}

group = "dev.badbird.spear"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.javalin:javalin:6.1.3")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "spear"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = "Spear"
                description = "A annotation wrapper for Javalin"
                url = "https://github.com/Badbird5907/Spear/"
                developers {
                    developer {
                        id = "Badbird5907"
                        name = "Evan Yu"
                        email = "contact@badbird.dev"
                    }
                }
                scm {
                    connection = "https://github.com/Badbird5907/Spear.git"
                    developerConnection = "https://github.com/Badbird5907/Spear.git"
                    url = "https://github.com/Badbird5907/Spear/"
                }
            }
        }
    }
}
tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}