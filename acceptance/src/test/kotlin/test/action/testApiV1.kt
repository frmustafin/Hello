package test.action

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import me.frmustafin.swipe.api.v1.models.ProfileUpdateObject
import test.action.v1.createProfile
import test.action.v1.deleteProfile
import test.action.v1.haveError
import test.action.v1.prod
import test.action.v1.readProfile
import test.action.v1.someCreateProfile
import test.action.v1.updateProfile

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1 prod") {
        test("Create Profile ok") {
            client.createProfile(mode = prod)
        }

        test("Read Profile ok") {
            val created = client.createProfile(mode = prod)
            client.readProfile(created.ownerId, mode = prod).asClue {
                it shouldBe created
            }
        }

        test("Update Profile ok") {
            val created = client.createProfile(mode = prod)
            client.updateProfile(
                created.ownerId,
                created.lock,
                ProfileUpdateObject(name = "Ivan", description = someCreateProfile.description),
                mode = prod
            )
            client.readProfile(created.ownerId, mode = prod) {
                it.profile?.name shouldBe "Ivan"
                it.profile?.description shouldBe someCreateProfile.description
            }
        }

        test("Delete Profile ok") {
            val created = client.createProfile(mode = prod)
            client.deleteProfile(created.ownerId, created.lock, mode = prod)
            client.readProfile(created.ownerId, mode = prod) {
                it should haveError("not-found")
            }
        }
    }
}