plugins {
    kotlin("jvm") version "2.2.20"
    id("kotlinx-serialization")
}

group = "com.memksim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.3.0")
    implementation("ai.koog:koog-agents:0.5.1")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    val ktor = "3.3.0"
    implementation("io.ktor:ktor-client-core:${ktor}")
    implementation("io.ktor:ktor-client-java:${ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor}")
    implementation("io.ktor:ktor-client-logging:${ktor}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}