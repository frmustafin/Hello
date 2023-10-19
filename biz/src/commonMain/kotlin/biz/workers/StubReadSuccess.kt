package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import worker

fun ICorChainDsl<MkplContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    handle {
        state = MkplState.FINISHING
        val stub = MkplProfileStub.prepareResult {
            profileRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
        }
        profileResponse = stub
    }
}
