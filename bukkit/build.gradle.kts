plugins {
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

repositories {
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    implementation(project(":core"))

    compileOnly("org.spigotmc:spigot-api:${rootProject.extra["spigotVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks {

    build {
        dependsOn("shadowJar")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                mapOf(
                    "version" to version
                )
            )
        }
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {

        archiveFileName.set("shadow.jar")
        //relocate(...)

    }

}
