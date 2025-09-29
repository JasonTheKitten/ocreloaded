evaluationDependsOn(":Minecraft")

plugins {
    id("oc-loader")
    alias(libs.plugins.loom)
}

loom {

}

dependencies {
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchmentMc.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${libs.versions.fabricLoader.get()}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${libs.versions.fabricApi.get()}")

    implementation(project(":Minecraft"))
}

sourceSets.named("main") {
    resources {
        srcDir(project(":Minecraft").sourceSets.named("main").get().resources.srcDirs)
    }
}