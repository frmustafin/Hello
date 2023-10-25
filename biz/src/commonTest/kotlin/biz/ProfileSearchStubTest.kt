package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileFilter
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileSearchStubTest {

    private val processor = MkplProfileProcessor()
    val filter = MkplProfileFilter(searchString = "Ivan")

    @Test
    fun read() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            profileFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.profilesResponse.size > 1)
        val first = ctx.profilesResponse.firstOrNull() ?: fail("Empty response list")
        with (MkplProfileStub.get()) {
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            profileFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            profileFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_NAME,
            profileFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}