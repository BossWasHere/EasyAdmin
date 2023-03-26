val bungeecordVersion by extra("1.19-R0.1-SNAPSHOT")
val spigotVersion by extra("1.19-R0.1-SNAPSHOT")

subprojects {

    group = "com.backwardsnode.easyadmin"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("java")
    }

    val java = extensions.getByType<JavaPluginExtension>()
    java.sourceCompatibility = JavaVersion.VERSION_16
    java.targetCompatibility = JavaVersion.VERSION_16

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

defaultTasks("build")
