package fr.mustafin.demo.service

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext


@Service
class MkplProfileBlockingProcessor {
    private val processor = MkplProfileProcessor()

    fun exec(ctx: MkplContext) = runBlocking { processor.exec(ctx) }
}