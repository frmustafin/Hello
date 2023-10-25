package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileUpdateStubTest {

    private val processor = MkplProfileProcessor()
    val id = MkplUserId("777")
    val name = "title 666"
    val description = "desc 666"
    val visibility = MkplVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            profileRequest = MkplProfile(
                id = id,
                name = name ,
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.profileResponse.id)
        assertEquals(name, ctx.profileResponse.name)
        assertEquals(description, ctx.profileResponse.description)
        assertEquals(visibility, ctx.profileResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_NAME,
            profileRequest = MkplProfile(
                id = id,
                name = "",
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_DESCRIPTION,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                description = "",
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
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
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_SEARCH_STRING,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}