group = "dev.badbird.spear"
version = "2.0.0"

repositories {
    mavenCentral()
}

// fake shadowJar task to make the build work
subprojects {
    tasks.register("shadowJar") {
        println("Hello, world!")
    }

}
tasks.register("shadowJar") {
    println("Hello, world!")
}

