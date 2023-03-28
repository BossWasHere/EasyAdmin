dependencies {
    compileOnly(project(":api"))

    compileOnly("org.jetbrains:annotations:24.0.1")
}

tasks {

    test {
        useJUnitPlatform()
    }

}
