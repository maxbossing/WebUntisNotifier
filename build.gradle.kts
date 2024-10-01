plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("dev.reformator.stacktracedecoroutinator") version "2.4.5"
    application
}

repositories {
    mavenCentral()
    maven("https://repo.exobyte.dev/releases")
}

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.9.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.7.3")
    implementation("org.jetbrains.kotlinx", "kotlinx-datetime", "0.6.1")

    implementation("io.ktor", "ktor-client-core", "2.3.12")
    implementation("io.ktor", "ktor-client-cio", "2.3.12")

    implementation("com.toddway.shelf" , "Shelf", "2.0.7")
    implementation("org.bytedream", "untis4j", "1.3.5")
}


tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "MainKt"
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
}
