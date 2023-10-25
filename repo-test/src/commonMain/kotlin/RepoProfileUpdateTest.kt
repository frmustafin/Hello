import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoProfileUpdateTest {
    abstract val repo: IProfileRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = MkplUserId("ad-repo-update-not-found")
    protected val lockBad = MkplProfileLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MkplProfileLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MkplProfile(
            id = updateSucc.id,
            name = "update object",
            description = "update object description",
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MkplProfile(
        id = updateIdNotFound,
        name = "update object not found",
        description = "update object not found description",
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        MkplProfile(
            id = updateConc.id,
            name = "update object not found",
            description = "update object not found description",
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateProfile(DbProfileRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.name, result.data?.name)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateProfile(DbProfileRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateProfile(DbProfileRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitProfiles("update") {
        override val initObjects: List<MkplProfile> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}