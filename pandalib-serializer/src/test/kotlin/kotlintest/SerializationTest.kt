package kotlintest

import dev.pandasystems.pandalib.serializer.serializers.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {
	@Test
	fun encodeTest() {
		val serializer = Json()
		val encoding = serializer.encodeObject(MyConfig())

		val expectedJson: String = """
			{
			    "name": "Hello, world!",
			    "port": 25565,
			    "serverId": "d015bfd1-60ea-4819-912e-99b607ec6b7f"
			}
		""".trimIndent()

		println(encoding)
		println(expectedJson)
		assertEquals(encoding, expectedJson)
	}
}