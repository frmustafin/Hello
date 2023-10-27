package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import kotlin.test.Test
import kotlin.test.Ignore

@OptIn(ExperimentalCoroutinesApi::class)
@Ignore
class BizValidationOffersTest {

    private val command = MkplCommand.OFFERS
    private val processor by lazy { MkplProfileProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

}