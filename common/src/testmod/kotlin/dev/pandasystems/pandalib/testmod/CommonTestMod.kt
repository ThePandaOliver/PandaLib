package dev.pandasystems.pandalib.testmod

import dev.pandasystems.pandalib.core.handles.identifier
import dev.pandasystems.pandalib.registry.DeferredRegistry
import net.minecraft.world.item.Item

object CommonTestMod {
	val items = DeferredRegistry.create<Item>()
	val testItem = items.register(identifier("pandalib_test", "test_item")) { Item(Item.Properties()) }

	fun init() {

	}
}