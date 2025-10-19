plugins {
    kotlin("jvm") version "2.2.20"
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
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}