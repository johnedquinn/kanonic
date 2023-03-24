plugins {
    kotlin("jvm") version "1.6.21"
}

group = "io.johnedquinn"
version = "0.1-SNAPSHOT"

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
    // Main
    implementation(kotlin("stdlib"))
    implementation(project(":kanonic-runtime"))
    implementation("com.squareup:kotlinpoet:1.12.0")

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junitParams)
    testRuntimeOnly(Dependencies.junitEngine)
}

val generatedSrc = "$buildDir/generated-src"

sourceSets {
    main {
        java.srcDir(generatedSrc)
    }
}

kotlin.sourceSets {
    main {
        kotlin.srcDir(generatedSrc)
    }
}

tasks.test {
    useJUnitPlatform()
}
