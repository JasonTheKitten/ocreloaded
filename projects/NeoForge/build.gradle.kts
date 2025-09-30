import com.matyrobbrt.registrationutils.gradle.RegExtension;
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("oc-neo-based")
    id("oc-loader")
    id("com.matyrobbrt.mc.registrationutils") version "1.21.0-0.2.2"
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

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Minecraft"))

    "additionalRuntimeClasspath"(libs.typesafeConfig)
    "additionalRuntimeClasspath"(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
    jarJar(libs.typesafeConfig)
    jarJar(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))

    implementation("mysticdrew:common-networking-neoforge:1.0.20-1.21.1")
    
    shade(libs.typesafeConfig)
    shade(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}

sourceSets.main {
    resources {
        srcDir(project(":Minecraft").sourceSets.main.get().resources.srcDirs)
    }
}

tasks.withType<ShadowJar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations = listOf(project.configurations.getByName("shade"))

    from(sourceSets.main.get().output)
    from(project(":Core").sourceSets.main.get().output)
    from(project(":Minecraft").sourceSets.main.get().output)

    relocate("com.typesafe.config", "li.cil.ocreloaded.lib.config")
    archiveClassifier.set("all")
}

afterEvaluate {
    extensions.getByType<RegExtension>().configureJarTask(tasks.shadowJar.get())
}