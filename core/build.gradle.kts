plugins {
    `java-library`
}

dependencies {
    api(project(":api"))
    api(project(":datasource"))
    compileOnlyApi("org.jetbrains:annotations:23.0.0")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.xerial:sqlite-jdbc:3.39.3.0")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.postgresql:postgresql:42.5.0")

    implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")
    implementation("org.yaml:snakeyaml:1.32")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks {

    test {
        useJUnitPlatform()
    }

}
