package java;

import dev.pandasystems.pandalib.serializer.PropertySerializerRegistry;
import dev.pandasystems.pandalib.serializer.SerializationOverrides;
import dev.pandasystems.pandalib.serializer.typeserializers.types.UuidSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MyConfig implements SerializationOverrides {
    public String name = "Main Server";
    public UUID serverId = UUID.randomUUID();
    public int port = 25565;
    
    @Override
    public void defineSerializers(@NotNull PropertySerializerRegistry serializers) {
        serializers.register("serverId", new UuidSerializer());   
    }
}
