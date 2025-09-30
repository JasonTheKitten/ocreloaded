plugins {
    id("com.matyrobbrt.mc.registrationutils") version "1.21.0-0.2.2"
}

registrationUtils {
    group("li.cil.ocreloaded.minecraft.registration")
    projects {
        create("Fabric") {
            type("Fabric")
        }
        create("NeoForge") {
            type("NeoForge")
        }
        create("Minecraft") {
            type("Common")
        }
    }
}