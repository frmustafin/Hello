package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import worker

fun ICorChainDsl<MkplContext>.stubOffersSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    handle {
        state = MkplState.FINISHING
        profileResponse = MkplProfileStub.prepareResult {
            profileRequest.id.takeIf { it != MkplUserId.NONE }?.also { this.id = it }
        }
        profilesResponse.addAll(MkplProfileStub.prepareManSearchList(profileResponse.age, MkplGender.MAN))
    }
}
