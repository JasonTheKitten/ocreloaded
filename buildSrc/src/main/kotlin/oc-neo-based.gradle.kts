plugins {
    id("oc-common")
    id("net.neoforged.moddev")
}

neoForge {
    val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

    parchment {
        minecraftVersion = libs.findVersion("parchmentMc").get().toString()
        mappingsVersion = libs.findVersion("parchment").get().toString()
    }

    runs {
        create("client") {
            client()
        }

        create("server") {
            server()
        }

        create("data") {
            data()
        }
    }
}