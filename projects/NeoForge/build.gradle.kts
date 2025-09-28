plugins {
    id("oc-neo-based")
    id("oc-loader")
}

neoForge {
    version = libs.versions.neoForge.get()

    validateAccessTransformers = true

    val ocreloaded by mods.registering {
        sourceSet(sourceSets.main.get())
    }

    runs {
        configureEach {
            loadedMods.add(ocreloaded)
        }
    }
}

dependencies {
    compileOnly(project(":Minecraft"))
    commonJava(project(path = ":Minecraft", configuration = "commonJava"))
    commonResources(project(path = ":Minecraft", configuration = "commonResources"))
}