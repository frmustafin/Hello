package biz.validation

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import worker

fun ICorChainDsl<MkplContext>.finishProfileValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        profileValidated = profileValidating
    }
}

fun ICorChainDsl<MkplContext>.finishProfileFilterValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        profileFilterValidated = profileFilterValidating
    }
}