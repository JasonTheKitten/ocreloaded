import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import net.fabricmc.loom.task.RemapJarTask;
import com.matyrobbrt.registrationutils.gradle.RegExtension;

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
    modImplementation("mysticdrew:common-networking-fabric:1.0.20-1.21.1")

    shadeApi(project(":Minecraft"))
}

sourceSets.main {
    resources {
        srcDir(project(":Minecraft").sourceSets.main.get().resources.srcDirs)
    }
}

tasks.withType<RemapJarTask> {
    inputFile.set(tasks.shadowJar.get().archiveFile)
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.remapJar)
}

afterEvaluate {
    extensions.getByType<RegExtension>().configureJarTask(tasks.shadowJar.get())
}