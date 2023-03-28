plugins {
    id("net.kyori.blossom") version ("1.3.1")
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":core"))

    compileOnly("com.velocitypowered:velocity-api:${rootProject.extra["velocityVersion"]}")
    annotationProcessor("com.velocitypowered:velocity-api:${rootProject.extra["velocityVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {

    build {
        dependsOn("shadowJar")
    }

    blossom {
        val buildInfoClass = "src/main/java/com/backwardsnode/easyadmin/velocity/BuildInfo.java"
        replaceToken("\${version}", project.version, buildInfoClass)
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {

        archiveFileName.set("EasyAdmin-Bukkit-${project.version}.jar")

        //relocate(...)

    }

}
