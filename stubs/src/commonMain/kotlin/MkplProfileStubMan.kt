package ru.otus.otuskotlin.marketplace.stubs

import ru.otus.otuskotlin.marketplace.common.models.*

object MkplProfileStubMan {
    val PROFILE_MAN: MkplProfile
        get() = MkplProfile(
            id = MkplUserId("666"),
            name = "Ivan",
            age = 23,
            mkplGender = MkplGender.MAN,
            mkplHobbies = MkplHobbies.BEER,
            description = "Designer from Saint-Petersburg",
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                MkplProfilePermissionClient.READ,
                MkplProfilePermissionClient.UPDATE,
                MkplProfilePermissionClient.DELETE,
                MkplProfilePermissionClient.MAKE_VISIBLE_PUBLIC,
                MkplProfilePermissionClient.MAKE_VISIBLE_GROUP,
                MkplProfilePermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val PROFILE_WOMAN = PROFILE_MAN.copy(mkplGender = MkplGender.WOMAN)
}
