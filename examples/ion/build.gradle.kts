plugins {
    kotlin("jvm") version "1.6.21"
    id("me.champeau.jmh") version "0.7.1"
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

    // Ion
    implementation("com.amazon.ion:ion-element:1.0.0")

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junitParams)
    testRuntimeOnly(Dependencies.junitEngine)
}

tasks.test {
    useJUnitPlatform()
}

// This is a work-around until Kanonic publishes a Gradle task
val generate = tasks.register<Exec>("generate") {
    dependsOn(":kanonic-tool:install")
    workingDir(projectDir)
    commandLine(
        "../../kanonic-tool/build/install/kanonic-tool/bin/kanonic",
        "-o",
        "src/main/kotlin",
        "src/main/kanonic"
    )
}

//jmh {
//    resultFormat = "json"
//    resultsFile = project.file("$buildDir/reports/jmh/results.json")
//}

tasks.compileKotlin {
    dependsOn(generate)
}
