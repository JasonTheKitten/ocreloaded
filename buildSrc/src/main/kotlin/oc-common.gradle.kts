plugins {
    id("oc-base")
}

configurations {
    create("coreJava") {
        isCanBeResolved = true
    }
}

repositories {
    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }
    maven("https://maven.blamejared.com") {
        name = "BlameJared's maven"
    }
}