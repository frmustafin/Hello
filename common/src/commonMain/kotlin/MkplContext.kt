package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var profileRequest: MkplProfile = MkplProfile(),
    var profileFilterRequest: MkplProfileFilter = MkplProfileFilter(),
    var profileResponse: MkplProfile = MkplProfile(),
    var profilesResponse: MutableList<MkplProfile> = mutableListOf(),

    var profileValidating: MkplProfile = MkplProfile(),
    var profileFilterValidating: MkplProfileFilter = MkplProfileFilter(),

    var profileValidated: MkplProfile = MkplProfile(),
    var profileFilterValidated: MkplProfileFilter = MkplProfileFilter(),
)
