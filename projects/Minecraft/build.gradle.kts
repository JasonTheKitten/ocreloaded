plugins {
    id("oc-neo-based")
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
}

dependencies {
    shadeApi(project(":Core"))
    api("mysticdrew:common-networking-common:1.0.20-1.21.1")
}

tasks.register<Copy>("copyResources") {
    copy {
        from("${projectDir}/src/generated/resources/")
        into(file("build/resources/main/"))
    }
}
