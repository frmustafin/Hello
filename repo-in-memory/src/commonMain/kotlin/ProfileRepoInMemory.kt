import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.ProfileEntity
import ru.otus.otuskotlin.marketplace.common.helpers.errorRepoConcurrency
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileResponse
import ru.otus.otuskotlin.marketplace.common.repo.DbProfilesResponse
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ProfileRepoInMemory(
    initObjects: List<MkplProfile> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IProfileRepository {

    private val cache = Cache.Builder<String, ProfileEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: MkplProfile) {
        val entity = ProfileEntity(ad)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createProfile(rq: DbProfileRequest): DbProfileResponse {
        val key = randomUuid()
        val profile = rq.profile.copy(id = MkplUserId(key), lock = MkplProfileLock(randomUuid()))
        val entity = ProfileEntity(profile)
        cache.put(key, entity)
        return DbProfileResponse(
            data = profile,
            isSuccess = true,
        )
    }

    override suspend fun readProfile(rq: DbProfileIdRequest): DbProfileResponse {
        val key = rq.id.takeIf { it != MkplUserId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbProfileResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateProfile(rq: DbProfileRequest): DbProfileResponse {
        val key = rq.profile.id.takeIf { it != MkplUserId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.profile.lock.takeIf { it != MkplProfileLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newAd = rq.profile.copy(lock = MkplProfileLock(randomUuid()))
        val entity = ProfileEntity(newAd)
        return mutex.withLock {
            val oldAd = cache.get(key)
            when {
                oldAd == null -> resultErrorNotFound
                oldAd.lock != oldLock -> DbProfileResponse(
                    data = oldAd.toInternal(),
                    isSuccess = false,
                    errors = listOf(errorRepoConcurrency(MkplProfileLock(oldLock), oldAd.lock?.let { MkplProfileLock(it) }))
                )

                else -> {
                    cache.put(key, entity)
                    DbProfileResponse(
                        data = newAd,
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun deleteProfile(rq: DbProfileIdRequest): DbProfileResponse {
        val key = rq.id.takeIf { it != MkplUserId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != MkplProfileLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldAd = cache.get(key)
            when {
                oldAd == null -> resultErrorNotFound
                oldAd.lock != oldLock -> DbProfileResponse(
                    data = oldAd.toInternal(),
                    isSuccess = false,
                    errors = listOf(errorRepoConcurrency(MkplProfileLock(oldLock), oldAd.lock?.let { MkplProfileLock(it) }))
                )

                else -> {
                    cache.invalidate(key)
                    DbProfileResponse(
                        data = oldAd.toInternal(),
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun searchProfile(rq: DbProfileFilterRequest): DbProfilesResponse {
        TODO("Not yet implemented")
    }

    companion object {
        val resultErrorEmptyId = DbProfileResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbProfileResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbProfileResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                MkplError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}