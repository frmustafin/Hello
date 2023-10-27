package test.action

import docker.RabbitDockerCompose
import fixture.BaseFunSpec
import fixture.client.RabbitClient

class AccRabbitTest : BaseFunSpec(RabbitDockerCompose, {
    val client = RabbitClient(RabbitDockerCompose)

    testStubApiV1(client)
})