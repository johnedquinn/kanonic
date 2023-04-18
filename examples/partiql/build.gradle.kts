plugins {
    kotlin("jvm") version "1.6.21"
    id("me.champeau.gradle.jmh") version "0.5.3"
    id("org.gradle.antlr")
}

version = "unspecified"

repositories {
    mavenCentral()
}

object Versions {
    const val junit = "5.9.0"
    const val antlr = "4.10.1"
}

object Dependencies {
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit5"
    const val junitParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junit}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
}

dependencies {
    // Plugins
    antlr("org.antlr:antlr4:${Versions.antlr}")

    // Runtime Dependencies
    implementation(kotlin("stdlib"))
    implementation(project(":kanonic-runtime"))
    implementation(project(":kanonic-syntax"))

    // External Runtime Dependencies
    implementation("com.amazon.ion:ion-element:1.0.0")
    implementation("org.partiql:partiql-lang-kotlin:0.9.2")
    implementation("org.antlr:antlr4-runtime:${Versions.antlr}")

    // Compile Dependencies
    compileOnly(project(":kanonic-gen"))
    compileOnly(project(":kanonic-syntax-gen"))
    compileOnly(project(":kanonic-tool"))

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

tasks.generateGrammarSource {
    val antlrPackage = "io.johnedquinn.partiql.antlr.generated"
    val antlrSources = "src/main/java/${antlrPackage.replace('.', '/')}"
    maxHeapSize = "64m"
    arguments = listOf("-visitor", "-long-messages", "-package", antlrPackage)
    outputDirectory = File(antlrSources)
}

//jmh {
//    resultFormat = "json"
//    resultsFile = project.file("$buildDir/reports/jmh/results.json")
//}

tasks.compileKotlin {
    dependsOn(tasks.generateGrammarSource)
}

tasks.compileKotlin {
    dependsOn(generate)
}
