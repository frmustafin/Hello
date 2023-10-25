package model

import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility

data class ProfileEntity (
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: MkplProfile): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != MkplVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = MkplProfile(
        id = id?.let { MkplUserId(it) }?: MkplUserId.NONE,
        name = name?: "",
        description = description?: "",
        visibility = visibility?.let { MkplVisibility.valueOf(it) }?: MkplVisibility.NONE,
        lock = lock?.let { MkplProfileLock(it) } ?: MkplProfileLock.NONE,
    )
}