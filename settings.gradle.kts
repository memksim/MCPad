plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    kotlin("plugin.serialization") version "2.2.20" apply false
}
rootProject.name = "MCPad"
include("bot")