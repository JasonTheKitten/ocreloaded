plugins {
    id("oc-neo-based")
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
}

dependencies {
    api(project(":Core"))
}

tasks.register<Copy>("copyResources") {
    copy {
        from("${projectDir}/src/generated/resources/")
        into(file("build/resources/main/"))
    }
}
