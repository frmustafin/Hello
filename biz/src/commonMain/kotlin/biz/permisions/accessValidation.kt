package biz.permisions

import ICorChainDsl
import chain
import checkPermitted
import resolveRelationsTo
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import worker

fun ICorChainDsl<MkplContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == MkplState.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        profileRepoRead.principalRelations = profileRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = checkPermitted(command, profileRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(MkplError(message = "User is not allowed to perform this operation"))
        }
    }
}