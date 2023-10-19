package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplGender
import ru.otus.otuskotlin.marketplace.common.models.MkplHobbies
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStubMan
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileCreateStubTest {

    private val processor = MkplProfileProcessor()
    val id = MkplUserId("666")
    val name = "Ivan"
    val age = 23
    val gender = MkplGender.MAN
    val hobbies = MkplHobbies.BEER
    val description = "Designer from Saint-Petersburg"
    val visibility = MkplVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                age = age,
                description = description,
                mkplGender = gender,
                mkplHobbies = hobbies,
                visibility = visibility
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfileStubMan.PROFILE_MAN.id, ctx.profileResponse.id)
        assertEquals(name, ctx.profileResponse.name)
        assertEquals(age, ctx.profileResponse.age)
        assertEquals(description, ctx.profileResponse.description)
        assertEquals(hobbies, ctx.profileResponse.mkplHobbies)
        assertEquals(gender, ctx.profileResponse.mkplGender)
        assertEquals(visibility, ctx.profileResponse.visibility)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_NAME,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                age = age,
                description = description,
                mkplGender = gender,
                mkplHobbies = hobbies,
                visibility = visibility
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
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_DESCRIPTION,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                age = age,
                description = description,
                mkplGender = gender,
                mkplHobbies = hobbies,
                visibility = visibility
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
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                age = age,
                description = description,
                mkplGender = gender,
                mkplHobbies = hobbies,
                visibility = visibility
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            profileRequest = MkplProfile(
                id = id,
                name = name,
                age = age,
                description = description,
                mkplGender = gender,
                mkplHobbies = hobbies,
                visibility = visibility
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplProfile(), ctx.profileResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}