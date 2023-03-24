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

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kanonic-gen"))
    implementation(project(":kanonic-runtime"))
    implementation("info.picocli:picocli:4.7.0")
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
