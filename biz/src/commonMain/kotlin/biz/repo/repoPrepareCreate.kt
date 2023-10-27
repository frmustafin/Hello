package biz.repo

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import worker

fun ICorChainDsl<MkplContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MkplState.RUNNING }
    handle {
        profileRepoRead = profileValidated.deepCopy()
        profileRepoRead.id = principal.id
        profileRepoPrepare = profileRepoRead

    }
}