import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility

object ProfileTable : Table("ad") {
    val id = varchar("id", 128)
    val name = varchar("title", 128)
    val description = varchar("description", 512)
    val visibility = enumeration("visibility", MkplVisibility::class)
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = MkplProfile(
        id = MkplUserId(res[id].toString()),
        name = res[name],
        description = res[description],
        visibility = res[visibility],
        lock = MkplProfileLock(res[lock])
    )

    fun from(res: ResultRow) = MkplProfile(
        id = MkplUserId(res[id].toString()),
        name = res[name],
        description = res[description],
        visibility = res[visibility],
        lock = MkplProfileLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, ad: MkplProfile, randomUuid: () -> String) {
        it[id] = ad.id.takeIf { it != MkplUserId.NONE }?.asString() ?: randomUuid()
        it[name] = ad.name
        it[description] = ad.description
        it[visibility] = ad.visibility
        it[lock] = ad.lock.takeIf { it != MkplProfileLock.NONE }?.asString() ?: randomUuid()
    }

}