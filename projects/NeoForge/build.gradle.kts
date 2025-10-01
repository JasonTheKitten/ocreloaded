import com.matyrobbrt.registrationutils.gradle.RegExtension;
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;

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
    shadeApi(project(":Minecraft"))

    "additionalRuntimeClasspath"(files(project.configurations.getByName("shade")))
    "additionalRuntimeClasspath"(files(project.configurations.getByName("shadeApi")))

    implementation("mysticdrew:common-networking-neoforge:1.0.20-1.21.1")
}

sourceSets.main {
    resources {
        srcDir(project(":Minecraft").sourceSets.main.get().resources.srcDirs)
    }
}

afterEvaluate {
    extensions.getByType<RegExtension>().configureJarTask(tasks.shadowJar.get())
}