import org.gradle.api.Project

fun Project.prop(name: String): String = requireNotNull(project.properties[name]?.toString()) { "Missing property: $name" }