/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:Suppress("UnstableApiUsage")

plugins {
	id("common")
}

val buildFor: String by extra

val fabricLoaderVersion: String? by extra

architectury {
	common(buildFor.split(",").map { it.trim() })
}

configurations {
	implementation.get().extendsFrom(nonModImplementation.get())
}

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
}