plugins {
    id("oc-base")
}

repositories {
    mavenCentral()
}

sourceSets {
    val jninject by creating {}
}

val moddedJNLua by tasks.registering(Jar::class) {
    archiveFileName.set("OpenComputers-JNLua-injected.jar")
    destinationDirectory.set(layout.buildDirectory.dir("injected-libs"))

    from(zipTree(file("../../libs/OpenComputers-JNLua.jar"))) {
        exclude("META-INF/MANIFEST.MF")
    }
    from(sourceSets["jninject"].output)

    manifest {
        attributes("Automatic-Module-Name" to "li.cil.oc.jnlua")
    }
}

val moddedLuaJ by tasks.registering(Jar::class) {
    archiveFileName.set("OpenComputers-LuaJ-modded.jar")
    destinationDirectory.set(layout.buildDirectory.dir("injected-libs"))

    from(zipTree(file("../../libs/OpenComputers-LuaJ.jar"))) {
        exclude("META-INF/MANIFEST.MF")
    }

    manifest {
        attributes("Automatic-Module-Name" to "li.cil.oc.luaj")
    }
}

dependencies {
    compileOnly(libs.slf4j)

    shadeApi(libs.typesafeConfig)
    shadeApi(files(moddedJNLua))
    shadeApi(files(moddedLuaJ))
}