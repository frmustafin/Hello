package test.action

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import me.frmustafin.swipe.api.v1.models.Gender
import me.frmustafin.swipe.api.v1.models.ProfileSearchFilter
import me.frmustafin.swipe.api.v1.models.ProfileUpdateObject
import test.action.v1.createProfile
import test.action.v1.deleteProfile
import test.action.v1.readProfile
import test.action.v1.searchProfile
import test.action.v1.someCreateProfile
import test.action.v1.updateProfile

fun FunSpec.testStubApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1 stub") {
        test("Create Ad ok") {
            client.createProfile()
        }

        test("Read Ad ok") {
            val created = client.createProfile()
            client.readProfile(created.ownerId).asClue {
                it shouldBe created
            }
        }

        test("Update Profile ok") {
            val created = client.createProfile()
            client.updateProfile(created.ownerId, created.lock, ProfileUpdateObject(name = "Ivan"))
            client.readProfile(created.ownerId) {
                // TODO раскомментировать, когда будет реальный реп
                //it.ad?.title shouldBe "Selling Nut"
                //it.ad?.description shouldBe someCreateAd.description
            }
        }

        test("Delete Profile ok") {
            val created = client.createProfile()
            client.deleteProfile(created.ownerId, created.lock)
            client.readProfile(created.ownerId) {
                // it should haveError("not-found") TODO раскомментировать, когда будет реальный реп
            }
        }
    }
}