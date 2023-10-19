package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplGender
import ru.otus.otuskotlin.marketplace.common.models.MkplHobbies
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import worker

fun ICorChainDsl<MkplContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    handle {
        state = MkplState.FINISHING
        val stub = MkplProfileStub.prepareResult {
            profileRequest.id.takeIf { it != MkplUserId.NONE }?.also { this.id = it }
            profileRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
            profileRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            profileRequest.mkplGender.takeIf { it != MkplGender.NONE }?.also { this.mkplGender = it }
            profileRequest.mkplHobbies.takeIf { it != MkplHobbies.NONE }?.also { this.mkplHobbies = it }
            profileRequest.visibility.takeIf { it != MkplVisibility.NONE }?.also { this.visibility = it }
        }
        profileResponse = stub
    }
}
