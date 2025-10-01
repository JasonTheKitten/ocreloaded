import li.cil.ocreloaded.gradle.ShadingAttribute;
import org.gradle.api.attributes.Usage;

plugins {
    id("java-library")
    id("com.gradleup.shadow")
}

val shadedUsage = objects.named(ShadingAttribute::class.java, ShadingAttribute.SHADED)
val standardUsage = objects.named(ShadingAttribute::class.java, ShadingAttribute.STANDARD)

val shade by configurations.creating {
	isCanBeResolved = true
	isCanBeConsumed = false
	
    attributes {
		attribute(ShadingAttribute.SHADING_ATTRIBUTE, shadedUsage)
		attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
	}
}

val shadeRuntimeElements by configurations.creating {
	isCanBeConsumed = true
	isCanBeResolved = false
	extendsFrom(shade)
	
    attributes {
		attribute(ShadingAttribute.SHADING_ATTRIBUTE, shadedUsage)
		attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
	}
}

configurations.named("implementation") { extendsFrom(shade) }

configurations.named("runtimeElements") {
	attributes {
		attribute(ShadingAttribute.SHADING_ATTRIBUTE, standardUsage)
	}
}

val shadeApi by configurations.creating {
	isCanBeResolved = true
	isCanBeConsumed = false
	
    attributes {
		attribute(ShadingAttribute.SHADING_ATTRIBUTE, shadedUsage)
		attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_API))
	}
}

configurations.named("api") { extendsFrom(shadeApi) }

val shadeApiElements by configurations.creating {
	isCanBeConsumed = true
	isCanBeResolved = false
	extendsFrom(shadeApi)
	
    attributes {
		attribute(ShadingAttribute.SHADING_ATTRIBUTE, shadedUsage)
		attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_API))
	}
}

configurations.named("apiElements") {
	attributes {
		attribute(ShadingAttribute.SHADING_ATTRIBUTE, standardUsage)
	}
}