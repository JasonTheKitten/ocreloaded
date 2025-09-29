pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.minecraftforge.net/") {
            name = "MinecraftForge"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }

        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
    }
}

rootProject.name = "OpenComputersReloaded"

include("Core")
include("MinecraftAPI")
include("Minecraft")
include("NeoForge")
include("Fabric")

for (project in rootProject.children) {
    project.projectDir = file("projects/${project.name}")
}