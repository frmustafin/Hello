package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileDeleteObject
import me.frmustafin.swipe.api.v1.models.ProfileDeleteRequest
import me.frmustafin.swipe.api.v1.models.ProfileDeleteResponse
import test.action.beValidId
import test.action.beValidLock

suspend fun Client.deleteProfile(id: String?, lock: String?, mode: ProfileDebug = debug) {
    withClue("deleteAdV1: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "ad/delete",
            ProfileDeleteRequest(
                requestType = "delete",
                debug = mode,
                profile = ProfileDeleteObject(id = id, lock = lock)
            )
        ) as ProfileDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.profile shouldNotBe null
            response.profile?.ownerId shouldBe id
        }
    }
}