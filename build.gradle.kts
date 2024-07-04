plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "dev.badbird.spear"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
}

java {
    withJavadocJar()
    withSourcesJar()
}
subprojects {
    val sourcesJar = tasks.register("sourcesJar", Jar::class.java) {
        archiveClassifier = "sources"
        from(sourceSets.main.get().allSource)
    }
    val javadocJar = tasks.register("javadocJar", Jar::class.java) {
        archiveClassifier = "javadoc"
        from(sourceSets.main.get().allJava)
    }

    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "io.freefair.lombok")

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
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
    signing {
        sign(publishing.publications["mavenJava"])
    }
    tasks {
        javadoc {
            if (JavaVersion.current().isJava9Compatible) {
                (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
            }
        }
        artifacts {
            archives(javadocJar)
            archives(sourcesJar)
        }
        build {
            dependsOn("shadowJar")
        }
    }
}