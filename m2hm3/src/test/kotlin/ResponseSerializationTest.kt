import me.frmustafin.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = ProfileCreateResponse(
        requestId = "123",
        profile = ProfileResponseObject(
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
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"name\":\\s*\"Farid\""))
        assertContains(json, Regex("\"age\":\\s*25"))
        assertContains(json, Regex("\"gender\":\\s*\"man\""))
        assertContains(json, Regex("\"hobbies\":\\s*\"beer\""))
        assertContains(json, Regex("\"description\":\\s*\"profile description\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as ProfileCreateResponse

        assertEquals(response, obj)
    }
}