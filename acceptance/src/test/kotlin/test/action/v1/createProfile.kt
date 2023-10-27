package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.frmustafin.swipe.api.v1.models.ProfileCreateObject
import me.frmustafin.swipe.api.v1.models.ProfileCreateRequest
import me.frmustafin.swipe.api.v1.models.ProfileCreateResponse
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileResponseObject

suspend fun Client.createProfile(profile: ProfileCreateObject = someCreateProfile, mode: ProfileDebug = debug): ProfileResponseObject =
    this@createProfile.createProfile(profile, mode) {
        it should haveSuccessResult
        it.profile shouldNotBe null
        it.profile?.apply {
            name shouldBe profile.name
            description shouldBe profile.description
            visibility shouldBe profile.visibility
        }
        it.profile!!
    }

suspend fun <T> Client.createProfile(
    profile: ProfileCreateObject = someCreateProfile,
    mode: ProfileDebug = debug,
    block: (ProfileCreateResponse) -> T
): T =
    withClue("createAdV1: $profile") {
        val response = sendAndReceive(
            "ad/create", ProfileCreateRequest(
                requestType = "create",
                debug = mode,
                profile = profile
            )
        ) as ProfileCreateResponse

        response.asClue(block)
    }