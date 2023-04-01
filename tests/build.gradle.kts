plugins {
    kotlin("jvm") version "1.6.21"
}

version = "unspecified"

repositories {
    mavenCentral()
}

object Versions {
    const val junit = "5.9.0"
}

object Dependencies {
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit5"
    const val junitParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junit}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kanonic-gen"))
    implementation(project(":kanonic-parser"))
    implementation(project(":kanonic-runtime"))
    implementation(project(":kanonic-syntax"))
    implementation(project(":kanonic-tool"))

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junitParams)
    testRuntimeOnly(Dependencies.junitEngine)
}

tasks.test {
    useJUnitPlatform()
}
