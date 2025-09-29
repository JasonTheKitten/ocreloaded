plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.typesafeConfig)
    implementation(libs.slf4j)
    api(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}