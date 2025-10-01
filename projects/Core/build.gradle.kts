plugins {
    id("oc-base")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.slf4j)

    shadeApi(libs.typesafeConfig)
    shadeApi(files("../../libs/OpenComputers-JNLua.jar", "../../libs/OpenComputers-LuaJ.jar"))
}