package ru.otus.otuskotlin.marketplace.common.models

data class MkplProfile(
    var id: MkplUserId = MkplUserId.NONE,
    var name: String = "",
    var age: Int = 0,
    var mkplGender: MkplGender = MkplGender.NONE,
    var mkplHobbies: MkplHobbies = MkplHobbies.NONE,
    var description: String = "",
    var visibility: MkplVisibility = MkplVisibility.NONE,
    val permissionsClient: MutableSet<MkplProfilePermissionClient> = mutableSetOf()
) {
    fun deepCopy(): MkplProfile = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )
}