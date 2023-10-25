package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId

data class DbProfileIdRequest(
    val id: MkplUserId,
    val lock: MkplProfileLock = MkplProfileLock.NONE,
) {
    constructor(profile: MkplProfile): this(profile.id, profile.lock)
}
