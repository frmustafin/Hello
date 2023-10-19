package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import worker

fun ICorChainDsl<MkplContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == MkplStubs.BAD_NAME && state == MkplState.RUNNING }
    handle {
        state = MkplState.FAILING
        this.errors.add(
            MkplError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
