package ru.otus.otuskotlin.marketplace.common

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import worker

fun ICorChainDsl<MkplContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != MkplWorkMode.STUB }
    handle {
        profileResponse = profileRepoDone
        profilesResponse = profilesRepoDone
        state = when (val st = state) {
            MkplState.RUNNING -> MkplState.FINISHING
            else -> st
        }
    }
}