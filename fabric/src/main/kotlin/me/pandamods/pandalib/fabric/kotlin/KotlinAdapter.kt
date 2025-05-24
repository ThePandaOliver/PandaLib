package me.pandamods.pandalib.fabric.kotlin

import net.fabricmc.loader.api.LanguageAdapter
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.impl.util.DefaultLanguageAdapter

open class KotlinAdapter : LanguageAdapter {
	override fun <T : Any> create(mod: ModContainer, value: String, type: Class<T>): T {
		println(value)
		println(type)
		return LanguageAdapter.getDefault().create(mod, value, type)
	}
}