architectury {
	common(properties["supported_mod_loaders"].toString().split(","))
}

loom.accessWidenerPath.set(file("src/main/resources/${properties["mod_id"]}.accesswidener"))

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")
}
