package ru.otus.otuskotlin.marketplace.mappers.v1

import me.frmustafin.api.v1.models.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = ProfileCreateRequest(
            requestId = "1234",
            debug = ProfileDebug(
                mode = ProfileRequestDebugMode.STUB,
                stub = ProfileRequestDebugStubs.SUCCESS,
            ),
            profile = ProfileCreateObject(
                name = "Farid",
                age = 25,
                gender = Gender.MAN,
                hobbies = Hobbies.BEER,
                description = "desc",
                visibility = ProfileVisibility.PUBLIC,
            ),
        )

        val context = MkplContext()
        context.fromTransport(req)

        assertEquals(MkplStubs.SUCCESS, context.stubCase)
        assertEquals(MkplWorkMode.STUB, context.workMode)
        assertEquals("Farid", context.profileRequest.name)
        assertEquals(25, context.profileRequest.age)
        assertEquals(MkplGender.MAN, context.profileRequest.mkplGender)
        assertEquals(MkplHobbies.BEER, context.profileRequest.mkplHobbies)
        assertEquals(MkplVisibility.VISIBLE_PUBLIC, context.profileRequest.visibility)
    }

    @Test
    fun toTransport() {
        val context = MkplContext(
            requestId = MkplRequestId("1234"),
            command = MkplCommand.CREATE,
            profileResponse = MkplProfile(
                name = "title",
                age = 25,
                mkplGender = MkplGender.MAN,
                mkplHobbies = MkplHobbies.BEER,
                description = "desc",
                visibility = MkplVisibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                MkplError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MkplState.RUNNING,
        )

        val req = context.toTransportProfile() as ProfileCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("Farid", req.profile?.name)
        assertEquals(25, req.profile?.age)
        assertEquals(Gender.MAN, req.profile?.gender)
        assertEquals(Hobbies.BEER, req.profile?.hobbies)
        assertEquals("desc", req.profile?.description)
        assertEquals(ProfileVisibility.PUBLIC, req.profile?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
