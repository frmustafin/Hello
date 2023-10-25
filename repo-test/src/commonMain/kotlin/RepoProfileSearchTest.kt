import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoProfileSearchTest {
    abstract val repo: IProfileRepository

    protected open val initializedObjects: List<MkplProfile> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchProfile(DbProfileFilterRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitProfiles("search") {

        val searchOwnerId = MkplUserId("owner-124")
        override val initObjects: List<MkplProfile> = listOf(
            createInitTestModel("ad1"),
        )
    }
}