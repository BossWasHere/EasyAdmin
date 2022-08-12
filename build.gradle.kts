val bungeecordVersion by extra("1.19-R0.1-SNAPSHOT")
val spigotVersion by extra("1.19-R0.1-SNAPSHOT")

plugins {
    java
}

allprojects {
    group = "com.backwardsnode"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("java")
    }

    java.sourceCompatibility = JavaVersion.VERSION_16
    java.targetCompatibility = JavaVersion.VERSION_16

    tasks {

        compileJava {
            options.encoding = "UTF-8"
        }

    }

}

defaultTasks("build")
