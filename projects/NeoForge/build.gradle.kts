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
    implementation(project(":Minecraft"))
}