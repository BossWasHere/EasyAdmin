plugins {
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

repositories {
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation(project(":core"))

    compileOnly("net.md-5:bungeecord-api:${rootProject.extra["bungeecordVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
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

        archiveFileName.set("EasyAdmin-Bungee-${project.version}.jar")
        //relocate(...)

    }

}
