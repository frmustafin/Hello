package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileResponseObject
import me.frmustafin.swipe.api.v1.models.ProfileUpdateObject
import me.frmustafin.swipe.api.v1.models.ProfileUpdateRequest
import me.frmustafin.swipe.api.v1.models.ProfileUpdateResponse
import test.action.beValidId
import test.action.beValidLock

suspend fun Client.updateProfile(id: String?, lock: String?, profile: ProfileUpdateObject, mode: ProfileDebug = debug): ProfileResponseObject =
    updateProfile(id, lock, profile, mode) {
        it should haveSuccessResult
        it.profile shouldNotBe null
        it.profile?.apply {
            if (profile.name != null)
                name shouldBe profile.name
            if (profile.description != null)
                description shouldBe profile.description
            if (profile.visibility != null)
                visibility shouldBe profile.visibility
        }
        it.profile!!
    }

suspend fun <T> Client.updateProfile(id: String?, lock: String?, profile: ProfileUpdateObject, mode: ProfileDebug = debug, block: (ProfileUpdateResponse) -> T): T =
    withClue("updatedV1: $id, lock: $lock, set: $profile") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "ad/update", ProfileUpdateRequest(
                requestType = "update",
                debug = mode,
                profile = profile.copy(id = id, lock = lock)
            )
        ) as ProfileUpdateResponse

        response.asClue(block)
    }
