plugins {
    id("oc-neo-based")
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
}

artifacts {
    add("apiJava", sourceSets["main"].java.sourceDirectories.singleFile)
}

dependencies {

}