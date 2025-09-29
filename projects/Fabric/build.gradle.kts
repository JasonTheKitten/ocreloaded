import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

evaluationDependsOn(":Minecraft")

plugins {
    id("oc-loader")
    alias(libs.plugins.loom)
}

dependencies {
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchmentMc.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${libs.versions.fabricLoader.get()}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${libs.versions.fabricApi.get()}")

    implementation(project(":Core"))
    implementation(project(":Minecraft"))
}

sourceSets.main {
    resources {
        srcDir(project(":Minecraft").sourceSets.main.get().resources.srcDirs)
    }
}

tasks.withType<ShadowJar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(project(":Core").sourceSets.main.get().output)
    from(project(":Minecraft").sourceSets.main.get().output)

    dependencies {
        include(dependency(libs.typesafeConfig))
        include(dependency(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar")))
    }

    relocate("com.typesafe.config", "li.cil.ocreloaded.lib.config")
    archiveClassifier.set("all")
}

tasks.withType<RemapJarTask> {
    inputFile.set(tasks.shadowJar.get().archiveFile)
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.remapJar)
}