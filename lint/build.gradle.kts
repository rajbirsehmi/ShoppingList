plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    compileOnly(libs.lint.api)
    testImplementation(libs.lint.tests)
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "com.creative.shoppinglist.lint.IssueRegistry")
    }
}
