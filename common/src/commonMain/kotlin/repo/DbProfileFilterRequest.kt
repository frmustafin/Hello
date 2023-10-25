package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.models.MkplUserId

data class DbProfileFilterRequest(
    val titleFilter: String = "",
    val ownerId: MkplUserId = MkplUserId.NONE,
)