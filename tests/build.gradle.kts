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
    implementation(project(":kanonic-runtime"))
    implementation(project(":kanonic-syntax"))
    implementation(project(":kanonic-syntax-gen"))
    implementation(project(":kanonic-tool"))

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junitParams)
    testRuntimeOnly(Dependencies.junitEngine)
}

tasks.test {
    useJUnitPlatform()
}

val generatedSrc = "$buildDir/generated-src/main/kotlin"

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

val generate = tasks.register<Exec>("generate") {
    dependsOn(":kanonic-tool:install")
    workingDir(projectDir)
    commandLine(
        "../kanonic-tool/build/install/kanonic-tool/bin/kanonic",
        "-o",
        "build/generated-src/main/kotlin",
        "src/main/resources"
    )
}

tasks.compileKotlin {
    dependsOn(generate)
}
