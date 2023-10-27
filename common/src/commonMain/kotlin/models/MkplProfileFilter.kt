package ru.otus.otuskotlin.marketplace.common.models

data class MkplProfileFilter(
    var searchString: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var searchPermissions: MutableSet<MkplSearchPermissions> = mutableSetOf(),
    )
