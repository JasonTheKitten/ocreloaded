plugins {
    id("oc-neo-based")
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

dependencies {
    compileOnly(project(":Core"))
    coreJava(project(path= ":Core", configuration= "coreJava"))

    implementation(libs.typesafeConfig)
    implementation(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}

artifacts {
    add("commonJava", sourceSets["main"].java.sourceDirectories.singleFile)
    add("commonResources", sourceSets["main"].resources.sourceDirectories.singleFile)
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(configurations["coreJava"])
    source(configurations["coreJava"])
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
