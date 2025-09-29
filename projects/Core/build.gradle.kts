plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.slf4j)

    api(libs.typesafeConfig)
    api(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}