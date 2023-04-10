plugins {
    kotlin("jvm") version "1.6.21"
    id("org.gradle.application")
}

version = "unspecified"

application {
    applicationName = "kanonic"
    mainClass.set("io.johnedquinn.kanonic.tool.Main")
}

distributions {
    main {
        distributionBaseName.set("kanonic-tool")
    }
}

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
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("info.picocli:picocli:4.7.0")

    testImplementation(Dependencies.junitParams)
    testRuntimeOnly(Dependencies.junitEngine)
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Tar>("distTar") {
    compression = Compression.GZIP
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.register<GradleBuild>("install") {
    tasks = listOf("assembleDist", "distZip", "installDist")
}
