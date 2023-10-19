package biz.groups

import ICorChainDsl
import chain
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.operation(title: String, command: MkplCommand, block: ICorChainDsl<MkplContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == MkplState.RUNNING }
}