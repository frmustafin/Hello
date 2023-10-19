package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import worker

fun ICorChainDsl<MkplContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        fail(
            MkplError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
