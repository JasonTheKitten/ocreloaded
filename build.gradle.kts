plugins {
    id("com.matyrobbrt.mc.registrationutils") version "1.21.0-0.2.2"
    id("oc-common")
    id("com.gradleup.shadow")
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