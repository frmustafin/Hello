import me.frmustafin.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = ProfileCreateRequest(
        requestId = "123",
        debug = ProfileDebug(
            mode = ProfileRequestDebugMode.STUB,
            stub = ProfileRequestDebugStubs.BAD_NAME
        ),
        profile = ProfileCreateObject(
            name = "Farid",
            age = 25,
            gender = Gender.MAN,
            hobbies = Hobbies.BEER,
            description = "profile description",
            visibility = ProfileVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"name\":\\s*\"Farid\""))
        assertContains(json, Regex("\"age\":\\s*25"))
        assertContains(json, Regex("\"gender\":\\s*\"man\""))
        assertContains(json, Regex("\"hobbies\":\\s*\"beer\""))
        assertContains(json, Regex("\"description\":\\s*\"profile description\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as ProfileCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "123"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, ProfileCreateRequest::class.java)

        assertEquals("123", obj.requestId)
    }
}