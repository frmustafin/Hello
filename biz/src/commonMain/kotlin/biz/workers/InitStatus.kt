package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import worker

fun ICorChainDsl<MkplContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == MkplState.NONE }
    handle { state = MkplState.RUNNING }
}
