plugins {
    kotlin("jvm") version "1.6.21"
    id("org.gradle.application")
}

application {
    applicationName = "kanonic-generate"
    mainClass.set("io.johnedquinn.kanonic.syntax.Main")
}

distributions {
    main {
        distributionBaseName.set("kanonic-generate")
    }
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kanonic-gen"))
    implementation(project(":kanonic-runtime"))
    implementation("info.picocli:picocli:4.7.0")
    implementation("com.squareup:kotlinpoet:1.12.0")
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
