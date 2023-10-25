import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility

abstract class BaseInitProfiles(val op: String): IInitObjects<MkplProfile> {

    open val lockOld: MkplProfileLock = MkplProfileLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MkplProfileLock = MkplProfileLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        lock: MkplProfileLock = lockOld,
    ) = MkplProfile(
        id = MkplUserId("ad-repo-$op-$suf"),
        name = "$suf stub",
        description = "$suf stub description",
        visibility = MkplVisibility.VISIBLE_TO_OWNER,
        lock = lock,
    )
}