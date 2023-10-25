import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import config.RabbitConfig
import config.RabbitConfig.Companion.RABBIT_PASSWORD
import config.RabbitConfig.Companion.RABBIT_USER
import config.RabbitExchangeConfiguration
import controller.RabbitController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.frmustafin.swipe.api.v1.models.Gender
import me.frmustafin.swipe.api.v1.models.ProfileCreateObject
import me.frmustafin.swipe.api.v1.models.ProfileCreateRequest
import me.frmustafin.swipe.api.v1.models.ProfileCreateResponse
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileRequestDebugMode
import me.frmustafin.swipe.api.v1.models.ProfileRequestDebugStubs
import org.junit.Test
import org.testcontainers.containers.RabbitMQContainer
import processor.RabbitDirectProcessor
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import kotlin.test.assertEquals
import kotlin.test.BeforeTest

//  TODO-rmq-8: тесты в использованием testcontainers
class RabbitMqTest {

    companion object {
        const val EXCHANGE_TYPE = "direct"
        const val TRANSPORT_EXCHANGE_V1 = "transport-exchange-v1"
    }

    val container by lazy {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser(RABBIT_USER, RABBIT_PASSWORD)
            start()
        }
    }
    val config by lazy {
        RabbitConfig(
            port = container.getMappedPort(5672),
            host = container.host
        )
    }
    val processorConfigV1 = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = TRANSPORT_EXCHANGE_V1,
        queueIn = "v1-queue",
        queueOut = "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = EXCHANGE_TYPE
    )
    val processorV1 by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = processorConfigV1
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processorV1)
        )
    }

    @BeforeTest
    fun tearUp() {
        println("init controller")
        GlobalScope.launch {
            controller.start()
        }
        Thread.sleep(6000)
        // await when controller starts producers
        println("controller initiated")
    }

    @Test
    fun ProfileCreateTestV1() {
        println("start test v1")
        val processorConfig = processorV1.processorConfig
        val keyIn = processorConfig.keyIn

        val connection1 = ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection()
        connection1.createChannel().use { channel ->
            var responseJson = ""
            channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
            val queueOut = channel.queueDeclare().queue
            channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
            val deliverCallback = DeliverCallback { consumerTag, delivery ->
                responseJson = String(delivery.body, Charsets.UTF_8)
                println(" [x] Received by $consumerTag: '$responseJson'")
            }
            channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

            channel.basicPublish(processorConfig.exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

            Thread.sleep(3000)
            // waiting for message processing
            println("RESPONSE: $responseJson")
            val response = apiV1Mapper.readValue(responseJson, ProfileCreateResponse::class.java)
            val expected = MkplProfileStub.get()

            assertEquals(expected.name, response.profile?.name)
            assertEquals(expected.age, response.profile?.age)
        }
    }

    private val boltCreateV1 = with(MkplProfileStub.get()) {
        ProfileCreateRequest(
            profile = ProfileCreateObject(
                name = "Ivan",
                age = 23,
                gender = Gender.MAN
            ),
            requestType = "create",
            debug = ProfileDebug(
                mode = ProfileRequestDebugMode.STUB,
                stub = ProfileRequestDebugStubs.SUCCESS
            )
        )
    }

}