import config.RabbitConfig
import config.RabbitExchangeConfiguration
import controller.RabbitController
import processor.RabbitDirectProcessor
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor

fun main() {
    val config = RabbitConfig()
    val adProcessor = MkplProfileProcessor()

    val producerConfigV1 = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange-v1",
        queueIn = "v1-queue",
        queueOut= "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = producerConfigV1,
            processor = adProcessor
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}