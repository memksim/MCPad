plugins {
    kotlin("jvm")
}

group = "com.memksim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ai.koog:koog-agents:0.5.1")

    implementation("org.postgresql:postgresql:42.7.4")

    val exposed = "1.0.0-rc-3"
    val h2 = "2.2.224"
    implementation("org.jetbrains.exposed:exposed-core:${exposed}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposed}")

    testImplementation("com.h2database:h2:${h2}")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}