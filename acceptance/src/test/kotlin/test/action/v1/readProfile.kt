package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileReadObject
import me.frmustafin.swipe.api.v1.models.ProfileReadRequest
import me.frmustafin.swipe.api.v1.models.ProfileReadResponse
import me.frmustafin.swipe.api.v1.models.ProfileResponseObject
import test.action.beValidId

suspend fun Client.readProfile(id: String?, mode: ProfileDebug = debug): ProfileResponseObject =
    this@readProfile.readProfile(id, mode) {
        it should haveSuccessResult
        it.profile shouldNotBe null
        it.profile!!
    }

suspend fun <T> Client.readProfile(id: String?, mode: ProfileDebug = debug, block: (ProfileReadResponse) -> T): T =
    withClue("readAdV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "ad/read",
            ProfileReadRequest(
                requestType = "read",
                debug = mode,
                profile = ProfileReadObject(id = id)
            )
        ) as ProfileReadResponse

        response.asClue(block)
    }