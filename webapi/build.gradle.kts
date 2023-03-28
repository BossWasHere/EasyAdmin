dependencies {
    compileOnly(project(":api"))

    implementation("com.hellokaton:blade-core:2.1.2.RELEASE")
}

tasks {

    test {
        useJUnitPlatform()
    }

}
