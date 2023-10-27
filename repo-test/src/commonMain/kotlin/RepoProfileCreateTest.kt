import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoProfileCreateTest {
    abstract val repo: IProfileRepository

    protected open val lockNew: MkplProfileLock = MkplProfileLock("20000000-0000-0000-0000-000000000002")

    private val createObj = MkplProfile(
        name = "create object",
        description = "create object description",
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createProfile(DbProfileRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: MkplUserId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.name, result.data?.name)
        assertEquals(expected.description, result.data?.description)
        assertNotEquals(MkplUserId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitProfiles("create") {
        override val initObjects: List<MkplProfile> = emptyList()
    }
}