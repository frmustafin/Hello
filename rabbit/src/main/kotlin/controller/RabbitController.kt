package controller

import RabbitProcessorBase
import config.rabbitLogger
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// TODO-rmq-5: запуск процессора
class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) {

    fun start() = runBlocking {
        rabbitLogger.info("start init processors")
        processors.forEach {
            try {
                launch { it.process() }
            } catch (e: RuntimeException) {
                // логируем, что-то делаем
                e.printStackTrace()
            }

        }
    }

}