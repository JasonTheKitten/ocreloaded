plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

configurations {
    create("coreJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("coreJava", sourceSets["main"].java.sourceDirectories.singleFile)
}

dependencies {
    implementation(libs.typesafeConfig)
    implementation(libs.slf4j)
    implementation(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}