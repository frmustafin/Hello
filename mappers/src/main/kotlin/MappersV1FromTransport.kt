package ru.otus.otuskotlin.marketplace.mappers.v1

import me.frmustafin.api.v1.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.mappers.v1.exceptions.UnknownRequestClass

fun MkplContext.fromTransport(request: IRequest) = when (request) {
    is ProfileCreateRequest -> fromTransport(request)
    is ProfileReadRequest -> fromTransport(request)
    is ProfileUpdateRequest -> fromTransport(request)
    is ProfileDeleteRequest -> fromTransport(request)
    is ProfileSearchRequest -> fromTransport(request)
    is ProfileOffersRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toProfileId() = this?.let { MkplUserId(it) } ?: MkplUserId.NONE
private fun String?.toAdWithId() = MkplProfile(id = this.toProfileId())
private fun IRequest?.requestId() = this?.requestId?.let { MkplRequestId(it) } ?: MkplRequestId.NONE

private fun ProfileDebug?.transportToWorkMode(): MkplWorkMode = when (this?.mode) {
    ProfileRequestDebugMode.PROD -> MkplWorkMode.PROD
    ProfileRequestDebugMode.TEST -> MkplWorkMode.TEST
    ProfileRequestDebugMode.STUB -> MkplWorkMode.STUB
    null -> MkplWorkMode.PROD
}

private fun ProfileDebug?.transportToStubCase(): MkplStubs = when (this?.stub) {
    ProfileRequestDebugStubs.SUCCESS -> MkplStubs.SUCCESS
    ProfileRequestDebugStubs.NOT_FOUND -> MkplStubs.NOT_FOUND
    ProfileRequestDebugStubs.BAD_ID -> MkplStubs.BAD_ID
    ProfileRequestDebugStubs.BAD_NAME -> MkplStubs.BAD_NAME
    ProfileRequestDebugStubs.BAD_AGE -> MkplStubs.BAD_AGE
    ProfileRequestDebugStubs.BAD_GENDER -> MkplStubs.BAD_GENDER
    ProfileRequestDebugStubs.BAD_HOBBIES -> MkplStubs.BAD_HOBBIES
    ProfileRequestDebugStubs.BAD_DESCRIPTION -> MkplStubs.BAD_DESCRIPTION
    ProfileRequestDebugStubs.BAD_VISIBILITY -> MkplStubs.BAD_VISIBILITY
    ProfileRequestDebugStubs.CANNOT_DELETE -> MkplStubs.CANNOT_DELETE
    ProfileRequestDebugStubs.BAD_SEARCH_STRING -> MkplStubs.BAD_SEARCH_STRING
    null -> MkplStubs.NONE
}

fun MkplContext.fromTransport(request: ProfileCreateRequest) {
    command = MkplCommand.CREATE
    requestId = request.requestId()
    profileRequest = request.profile?.toInternal() ?: MkplProfile()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: ProfileReadRequest) {
    command = MkplCommand.READ
    requestId = request.requestId()
    profileRequest = request.profile?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: ProfileUpdateRequest) {
    command = MkplCommand.UPDATE
    requestId = request.requestId()
    profileRequest = request.profile?.toInternal() ?: MkplProfile()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: ProfileDeleteRequest) {
    command = MkplCommand.DELETE
    requestId = request.requestId()
    profileRequest = request.profile?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: ProfileSearchRequest) {
    command = MkplCommand.SEARCH
    requestId = request.requestId()
    profileFilterRequest = request.profileFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: ProfileOffersRequest) {
    command = MkplCommand.OFFERS
    requestId = request.requestId()
    profileRequest = request.profile?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ProfileSearchFilter?.toInternal(): MkplProfileFilter = MkplProfileFilter(
    searchString = this?.searchString ?: ""
)

private fun ProfileCreateObject.toInternal(): MkplProfile= MkplProfile(
    name = this.name ?: "",
    age = this.age ?: 0,
    mkplGender = this.gender.fromTransport(),
    mkplHobbies = this.hobbies.fromTransport(),
    description = this.description ?: "",
    visibility = this.visibility.fromTransport(),
)

private fun ProfileUpdateObject.toInternal(): MkplProfile = MkplProfile(
    id = this.id.toProfileId(),
    name = this.name ?: "",
    age = this.age ?: 0,
    mkplGender = this.gender.fromTransport(),
    mkplHobbies = this.hobbies.fromTransport(),
    description = this.description ?: "",
    visibility = this.visibility.fromTransport(),
)

private fun ProfileVisibility?.fromTransport(): MkplVisibility = when (this) {
    ProfileVisibility.PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
    ProfileVisibility.OWNER_ONLY -> MkplVisibility.VISIBLE_TO_OWNER
    ProfileVisibility.REGISTERED_ONLY -> MkplVisibility.VISIBLE_TO_GROUP
    else -> MkplVisibility.NONE
}

private fun Gender?.fromTransport(): MkplGender = when (this) {
    Gender.MAN -> MkplGender.MAN
    Gender.WOMAN -> MkplGender.WOMAN
    else -> MkplGender.NONE
}

private fun Hobbies?.fromTransport(): MkplHobbies = when (this) {
    Hobbies.MOVIE -> MkplHobbies.MOVIE
    Hobbies.BEER -> MkplHobbies.BEER
    Hobbies.MUSIC -> MkplHobbies.MUSIC
    Hobbies.HAIKING -> MkplHobbies.HAIKING
    null -> MkplHobbies.NONE
}


