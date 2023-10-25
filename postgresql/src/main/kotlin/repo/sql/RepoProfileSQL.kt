import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileResponse
import ru.otus.otuskotlin.marketplace.common.repo.DbProfilesResponse
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

class RepoProfileSQL(
    properties: SqlProperties,
    initObjects: Collection<MkplProfile> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IProfileRepository {

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
        }

        Database.connect(
            properties.url, driver, properties.user, properties.password
        )

        transaction {
            if (properties.dropDatabase) SchemaUtils.drop(ProfileTable)
            SchemaUtils.create(ProfileTable)
            initObjects.forEach { createProfile(it) }
        }
    }

    private fun createProfile(ad: MkplProfile): MkplProfile {
        val res = ProfileTable.insert {
            to(it, ad, randomUuid)
        }

        return ProfileTable.from(res)
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbProfileResponse): DbProfileResponse =
        transactionWrapper(block) { DbProfileResponse.error(it.asMkplError()) }

    override suspend fun createProfile(rq: DbProfileRequest): DbProfileResponse = transactionWrapper {
        DbProfileResponse.success(createProfile(rq.profile))
    }

    private fun read(id: MkplUserId): DbProfileResponse {
        val res = ProfileTable.select {
            ProfileTable.id eq id.asString()
        }.singleOrNull() ?: return DbProfileResponse.errorNotFound
        return DbProfileResponse.success(ProfileTable.from(res))
    }

    override suspend fun readProfile(rq: DbProfileIdRequest): DbProfileResponse = transactionWrapper { read(rq.id) }

    private fun update(
        id: MkplUserId,
        lock: MkplProfileLock,
        block: (MkplProfile) -> DbProfileResponse
    ): DbProfileResponse =
        transactionWrapper {
            if (id == MkplUserId.NONE) return@transactionWrapper DbProfileResponse.errorEmptyId

            val current = ProfileTable.select { ProfileTable.id eq id.asString() }
                .firstOrNull()
                ?.let { ProfileTable.from(it) }

            when {
                current == null -> DbProfileResponse.errorNotFound
                current.lock != lock -> DbProfileResponse.errorConcurrent(lock, current)
                else -> block(current)
            }
        }

    override suspend fun updateProfile(rq: DbProfileRequest): DbProfileResponse =
        update(rq.profile.id, rq.profile.lock) {
            ProfileTable.update({
                (ProfileTable.id eq rq.profile.id.asString()) and (ProfileTable.lock eq rq.profile.lock.asString())
            }) {
                to(it, rq.profile, randomUuid)
            }
            read(rq.profile.id)
        }

    override suspend fun deleteProfile(rq: DbProfileIdRequest): DbProfileResponse = update(rq.id, rq.lock) {
        ProfileTable.deleteWhere {
            (ProfileTable.id eq rq.id.asString()) and (ProfileTable.lock eq rq.lock.asString())
        }
        DbProfileResponse.success(it)
    }

    override suspend fun searchProfile(rq: DbProfileFilterRequest) =
        transactionWrapper({
            val res = ProfileTable.select {
                buildList {
                    add(Op.TRUE)
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (ProfileTable.name like "%${rq.titleFilter}%")
                                    or (ProfileTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbProfilesResponse(data = res.map { ProfileTable.from(it) }, isSuccess = true)
        }, {
            DbProfilesResponse.error(it.asMkplError())
        })
}