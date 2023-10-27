package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileResponseObject
import me.frmustafin.swipe.api.v1.models.ProfileSearchFilter
import me.frmustafin.swipe.api.v1.models.ProfileSearchRequest
import me.frmustafin.swipe.api.v1.models.ProfileSearchResponse

suspend fun Client.searchProfile(search: ProfileSearchFilter, mode: ProfileDebug = debug): List<ProfileResponseObject> = searchProfile(search, mode) {
    it should haveSuccessResult
    it.profiles ?: listOf()
}

suspend fun <T> Client.searchProfile(search: ProfileSearchFilter, mode: ProfileDebug = debug, block: (ProfileSearchResponse) -> T): T =
    withClue("searchProfileV1: $search") {
        val response = sendAndReceive(
            "profile/search",
            ProfileSearchRequest(
                requestType = "search",
                debug = mode,
                profileFilter = search,
            )
        ) as ProfileSearchResponse

        response.asClue(block)
    }