import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("oc-common")
}

tasks.withType<ProcessResources> {
    val properties = mapOf(
        "neoforgeVersionRange" to rootProject.properties["neo_loader_version_range"],
        "neoforgeLoaderVersionRange" to rootProject.properties["neo_version_range"],
        "minecraftVersionRange" to rootProject.properties["mc_version_range"],
        "minecraftVersion" to rootProject.properties["mc_version"],
        "version" to rootProject.properties["mod_version"]
    )

    inputs.properties(properties)
    filesMatching(listOf("META-INF/neoforge.mods.toml", "pack.mcmeta", "fabric.mod.json")) {
        expand(properties)
    }
}

tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(rootProject.file("libs/lua53")) {
        into("data/ocreloaded/libs/lua53")
    }
    from(rootProject.file("libs/lua52")) {
        into("data/ocreloaded/libs/lua52")
    }
}

tasks.withType<ShadowJar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations = listOf(
        project.configurations.getByName("shade"),
        project.configurations.getByName("shadeApi")
    )

    from(sourceSets.main.get().output)
    from(project(":Core").sourceSets.main.get().output)
    from(project(":Minecraft").sourceSets.main.get().output)

    relocate("com.typesafe.config", "li.cil.ocreloaded.lib.config")
    archiveClassifier.set("all")
}