import org.gradle.api.Project

fun Project.prop(name: String): String? = project.properties[name]?.toString()