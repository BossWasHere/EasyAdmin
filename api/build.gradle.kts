description = "API for EasyAdmin"

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
}

tasks {

    javadoc {
        val jdOptions = (options as StandardJavadocDocletOptions)
        jdOptions.tags("apiNote:a:API Note:", "implNote:a:Implementation Note:")
        jdOptions.docTitle("EasyAdmin API " + project.version)
    }

}
