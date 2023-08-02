import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    java
    application
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

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("net.dv8tion:JDA:5.0.0-beta.12")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

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