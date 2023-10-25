package biz.validation

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import worker

// TODO-validation-4: смотрим пример COR DSL валидации
fun ICorChainDsl<MkplContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { profileValidating.name.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}