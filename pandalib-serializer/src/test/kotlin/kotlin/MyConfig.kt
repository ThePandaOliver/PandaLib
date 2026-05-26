package kotlin

import dev.pandasystems.pandalib.serializer.PropertySerializerRegistry
import dev.pandasystems.pandalib.serializer.SerializationOverrides
import dev.pandasystems.pandalib.serializer.typeserializers.types.UuidSerializer
import java.util.UUID

class MyConfig : SerializationOverrides {
	var name: String = "Main Server"
	var serverId: UUID = UUID.randomUUID()
	var port: Int = 25565
	
	override fun defineSerializers(serializers: PropertySerializerRegistry) {
		serializers.register("serverId", UuidSerializer())
	}
}