package dev.pandasystems.pandalib.neoforge.testmod

import dev.pandasystems.pandalib.testmod.CommonClientTestMod
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod

@Mod(value = "pandalib_test", dist = [Dist.CLIENT])
class NeoForgeClientTestMod {
	init {
		CommonClientTestMod.initClient()
	}
}
