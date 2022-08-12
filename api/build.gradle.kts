description = "API for EasyAdmin"

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
}

tasks {

    javadoc {
        val jdOptions = (options as StandardJavadocDocletOptions)
        jdOptions.tags("apiNote:a:API Note:", "implNote:a:Implementation Note:")
        jdOptions.docTitle("EasyAdmin API " + project.version)
    }

}
