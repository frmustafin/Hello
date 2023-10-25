package biz.validation

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import worker

fun ICorChainDsl<MkplContext>.validateNameHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { profileValidating.name.isNotEmpty() && !profileValidating.name.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain leters"
            )
        )
    }
}