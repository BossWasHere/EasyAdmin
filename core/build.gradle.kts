plugins {
    `java-library`
}

dependencies {
    api(project(":api"))
    api(project(":datasource"))
    compileOnlyApi("org.jetbrains:annotations:24.0.1")

    implementation("com.zaxxer:HikariCP:5.0.1") {
        exclude(group = "org.slf4j")
    }
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.postgresql:postgresql:42.5.4")

    implementation("com.github.ben-manes.caffeine:caffeine:3.1.5")
    implementation("org.yaml:snakeyaml:2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {

    test {
        useJUnitPlatform()
    }

}
