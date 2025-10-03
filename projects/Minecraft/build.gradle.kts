plugins {
    id("oc-neo-based")
}

neoForge {
    neoFormVersion = libs.versions.neoForm.get()
}

sourceSets {
    create("generator")
}

dependencies {
    shadeApi(project(":Core"))

    "generatorImplementation"("com.google.code.gson:gson:2.13.2")
}

val generateBlockstatesTask = tasks.register<JavaExec>("generateBlockstates") {
	group = "generation"
	description = "Generates Minecraft blockstate and model JSON files."

	classpath = sourceSets.getByName("generator").runtimeClasspath

	mainClass.set("li.cil.ocreloaded.generator.BlockstateJsonGenerator")

	val outputDir = project.layout.buildDirectory.dir("generated/resources/assets/ocreloaded")
	args(outputDir.get().asFile.absolutePath)
	outputs.dir(project.layout.buildDirectory.dir("generated/resources"))
}

tasks.named("processResources") {
	dependsOn(generateBlockstatesTask)
}

sourceSets.main.get().resources {
	srcDir(generateBlockstatesTask)
}