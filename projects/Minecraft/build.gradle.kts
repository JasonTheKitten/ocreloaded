plugins {
    id("oc-neo-based")
    `java-library`
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
}

dependencies {
    api(project(":Core"))
    api(project(":MinecraftAPI"))
}

tasks.register<Copy>("copyResources") {
    copy {
        from("${rootProject.projectDir}/libs/lua52")
        into(file("build/resources/main/data/ocreloaded/libs/lua52"))
    }

    copy {
        from("${rootProject.projectDir}/libs/lua53")
        into(file("build/resources/main/data/ocreloaded/libs/lua53"))
    }

    copy {
        from("${projectDir}/src/generated/resources/")
        into(file("build/resources/main/"))
    }
}
