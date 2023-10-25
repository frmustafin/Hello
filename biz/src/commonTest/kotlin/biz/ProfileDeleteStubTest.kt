package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileDeleteStubTest {

    private val processor = MkplProfileProcessor()
    val id = MkplUserId("666")

    @Test
    fun delete() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            profileRequest = MkplProfile(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = MkplProfileStub.get()
        assertEquals(stub.id, ctx.profileResponse.id)
        assertEquals(stub.name, ctx.profileResponse.name)
        assertEquals(stub.description, ctx.profileResponse.description)
        assertEquals(stub.visibility, ctx.profileResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            profileRequest = MkplProfile(),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            profileRequest = MkplProfile(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_NAME,
            profileRequest = MkplProfile(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}