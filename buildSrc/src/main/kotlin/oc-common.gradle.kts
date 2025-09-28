plugins {
    id("java-library")
    id("com.gradleup.shadow")
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
}