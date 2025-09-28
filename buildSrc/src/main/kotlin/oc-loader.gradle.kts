plugins {
    id("oc-common")
}

configurations {
    create("commonJava") {
        isCanBeResolved = true
    }
    create("commonResources") {
        isCanBeResolved = true
    }
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(configurations["commonJava"])
    source(configurations["commonJava"])
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