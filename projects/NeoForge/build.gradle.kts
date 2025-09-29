plugins {
    id("oc-neo-based")
    id("oc-loader")
}

neoForge {
    version = libs.versions.neoForge.get()

    validateAccessTransformers = true

    val ocreloaded by mods.registering {
        sourceSet(sourceSets.main.get())
        sourceSet(project(":Minecraft").sourceSets.main.get())
        sourceSet(project(":Core").sourceSets.main.get())
    }

    runs {
        configureEach {
            loadedMods.add(ocreloaded)
        }
    }
}

dependencies {
    implementation(project(":Minecraft"))
    "additionalRuntimeClasspath"(libs.typesafeConfig)
    "additionalRuntimeClasspath"(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}

sourceSets.main {
    resources {
        srcDir(project(":Minecraft").sourceSets.main.get().resources.srcDirs)
    }
}