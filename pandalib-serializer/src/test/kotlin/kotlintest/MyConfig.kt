package kotlintest

import dev.pandasystems.pandalib.serializer.PropertySerializerRegistry
import dev.pandasystems.pandalib.serializer.SerializationOverrides
import dev.pandasystems.pandalib.serializer.typeserializers.types.UuidSerializer
import java.util.*

class MyConfig : SerializationOverrides {
	var name: String = "Hello, world!"
	var serverId: UUID = UUID.fromString("d015bfd1-60ea-4819-912e-99b607ec6b7f")
	var port: Int = 25565
	
	override fun defineSerializers(serializers: PropertySerializerRegistry) {
		serializers.register("serverId", UuidSerializer())
	}
}