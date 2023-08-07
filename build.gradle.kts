import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    java
    application
    kotlin("plugin.serialization") version "1.9.0"
}

group = "app.lacabra"
version = "0.0.1"
description = "Un simple bot para reportar problemas con la versión preliminar de LA CABRA"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
}

dependencies {

    implementation("ch.qos.logback:logback-classic:1.4.8")
    implementation("io.github.classgraph:classgraph:4.8.161")
    implementation("com.charleskorn.kaml:kaml:0.55.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("net.dv8tion:JDA:5.0.0-beta.13")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.jetbrains.exposed:exposed-core:0.42.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.42.0")

}

kotlin {
    jvmToolchain(18)
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

application {
    mainClass.set("app.lacabra.Main")
}

tasks {
    build {
        doLast {
            println("Application build completed successfully!")
        }
    }
    withType(KotlinCompile::class.java) {
        kotlinOptions.jvmTarget = "18"
    }
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
}