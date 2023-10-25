package biz.validation

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import worker

fun ICorChainDsl<MkplContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { profileValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}