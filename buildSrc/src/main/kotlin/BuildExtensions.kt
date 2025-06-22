/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

import org.gradle.api.Project

fun Project.prop(name: String): String = requireNotNull(project.properties[name]?.toString()) { "Missing property: $name" }