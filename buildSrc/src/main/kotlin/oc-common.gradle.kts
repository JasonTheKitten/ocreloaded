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
    maven("https://maven.blamejared.com") {
        name = "BlameJared's maven"
    }
}

val shade by configurations.creating
configurations["implementation"].extendsFrom(shade)