package dev.pandasystems.pandalib.testmod

import dev.pandasystems.pandalib.core.handles.identifier
import dev.pandasystems.pandalib.registry.DeferredRegistry
import dev.pandasystems.pandalib.testmod.networking.NetworkTestManager
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

object CommonTestMod {
	val items = DeferredRegistry.create(Registries.ITEM)
	val testItem = items.register(identifier("pandalib_test", "test_item")) { Item(Item.Properties().setId(it)) }

	fun init() {
		items.finalizeRegistration()
		NetworkTestManager.registerHandlers()
	}
}