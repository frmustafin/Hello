package ru.otus.otuskotlin.marketplace.mappers.v1

import me.frmustafin.api.v1.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.mappers.v1.exceptions.UnknownMkplCommand

fun MkplContext.toTransportProfile(): IResponse = when (val cmd = command) {
    MkplCommand.CREATE -> toTransportCreate()
    MkplCommand.READ -> toTransportRead()
    MkplCommand.UPDATE -> toTransportUpdate()
    MkplCommand.DELETE -> toTransportDelete()
    MkplCommand.SEARCH -> toTransportSearch()
    MkplCommand.OFFERS -> toTransportOffers()
    MkplCommand.NONE -> throw UnknownMkplCommand(cmd)
}

fun MkplContext.toTransportCreate() = ProfileCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    profile = profileResponse.toTransportProfile()
)

fun MkplContext.toTransportRead() = ProfileReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    profile = profileResponse.toTransportProfile()
)

fun MkplContext.toTransportUpdate() = ProfileUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    profile = profileResponse.toTransportProfile()
)

fun MkplContext.toTransportDelete() = ProfileDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    profile = profileResponse.toTransportProfile()
)

fun MkplContext.toTransportSearch() = ProfileSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    profiles = profilesResponse.toTransportProfile()
)

fun MkplContext.toTransportOffers() = ProfileOffersResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    profiles = profilesResponse.toTransportProfile()
)

fun List<MkplProfile>.toTransportProfile(): List<ProfileResponseObject>? = this
    .map { it.toTransportProfile() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun MkplContext.toTransportInit() = ProfileInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun MkplProfile.toTransportProfile(): ProfileResponseObject = ProfileResponseObject(
    ownerId = id.takeIf { it != MkplUserId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    age = age.takeIf { it != 0 },
    gender = mkplGender.toTransportProfile(),
    hobbies = mkplHobbies.toTransportProfile(),
    description = description.takeIf { it.isNotBlank() },
    visibility = visibility.toTransportProfile(),
    permissions = permissionsClient.toTransportProfile(),
)

private fun Set<MkplProfilePermissionClient>.toTransportProfile(): Set<ProfilePermissions>? = this
    .map { it.toTransportProfile() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkplProfilePermissionClient.toTransportProfile() = when (this) {
    MkplProfilePermissionClient.READ -> ProfilePermissions.READ
    MkplProfilePermissionClient.UPDATE -> ProfilePermissions.UPDATE
    MkplProfilePermissionClient.MAKE_VISIBLE_OWNER -> ProfilePermissions.MAKE_VISIBLE_OWN
    MkplProfilePermissionClient.MAKE_VISIBLE_GROUP -> ProfilePermissions.MAKE_VISIBLE_GROUP
    MkplProfilePermissionClient.MAKE_VISIBLE_PUBLIC -> ProfilePermissions.MAKE_VISIBLE_PUBLIC
    MkplProfilePermissionClient.DELETE -> ProfilePermissions.DELETE
}

private fun MkplVisibility.toTransportProfile(): ProfileVisibility? = when (this) {
    MkplVisibility.VISIBLE_PUBLIC -> ProfileVisibility.PUBLIC
    MkplVisibility.VISIBLE_TO_GROUP -> ProfileVisibility.REGISTERED_ONLY
    MkplVisibility.VISIBLE_TO_OWNER -> ProfileVisibility.OWNER_ONLY
    MkplVisibility.NONE -> null
}

private fun MkplGender?.toTransportProfile(): Gender? = when (this) {
    MkplGender.MAN -> Gender.MAN
    MkplGender.WOMAN -> Gender.WOMAN
    else -> null
}

private fun MkplHobbies?.toTransportProfile(): Hobbies? = when (this) {
    MkplHobbies.MOVIE -> Hobbies.MOVIE
    MkplHobbies.BEER -> Hobbies.BEER
    MkplHobbies.MUSIC -> Hobbies.MUSIC
    MkplHobbies.HAIKING -> Hobbies.HAIKING
    else -> null
}


private fun List<MkplError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportProfile() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplError.toTransportProfile() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
