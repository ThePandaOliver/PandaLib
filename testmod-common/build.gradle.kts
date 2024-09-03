// gradle.properties
val modId: String by project
val supportedModLoaders: String by project

val fabricLoaderVersion: String by project

architectury {
	common(supportedModLoaders.split(","))
}

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")

	implementation(project(":common", "namedElements"))
}
