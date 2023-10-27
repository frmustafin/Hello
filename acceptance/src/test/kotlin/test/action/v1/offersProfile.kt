package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileOffersRequest
import me.frmustafin.swipe.api.v1.models.ProfileOffersResponse
import me.frmustafin.swipe.api.v1.models.ProfileReadObject
import me.frmustafin.swipe.api.v1.models.ProfileResponseObject

suspend fun Client.offersAd(id: String?, mode: ProfileDebug = debug): List<ProfileResponseObject> = offersAd(id, mode) {
    it should haveSuccessResult
    it.profiles ?: listOf()
}

suspend fun <T> Client.offersAd(id: String?, mode: ProfileDebug = debug, block: (ProfileOffersResponse) -> T): T =
    withClue("searchOffersV1: $id") {
        val response = sendAndReceive(
            "ad/offers",
            ProfileOffersRequest(
                requestType = "offers",
                debug = mode,
                profile = ProfileReadObject(id = id),
            )
        ) as ProfileOffersResponse

        response.asClue(block)
    }