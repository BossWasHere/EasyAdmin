plugins {
    `java-library`
}

dependencies {
    api(project(":api"))
    compileOnlyApi("org.jetbrains:annotations:23.0.0")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("com.h2database:h2:2.1.214")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks {

    test {
        useJUnitPlatform()
    }

}
