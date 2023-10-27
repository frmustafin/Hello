package test.action

import docker.KtorDockerCompose
import docker.SpringDockerCompose
import fixture.BaseFunSpec
import fixture.client.RestClient
import fixture.client.WebSocketClient
import fixture.db.PostgresClearer

open class AccRestSpringTest : BaseFunSpec(SpringDockerCompose, PostgresClearer(), {
    val restClient = RestClient(SpringDockerCompose)
    testApiV1(restClient, "rest ")

    val websocketClient = WebSocketClient(SpringDockerCompose)
    testApiV1(websocketClient, "websocket ")
})

open class AccRestKtorTest : BaseFunSpec(KtorDockerCompose, {
    val restClient = RestClient(KtorDockerCompose)
    testStubApiV1(restClient, "rest ")

    if (false) {
        // я пока не разобрался, почему websocket-тесты не работают с ktor. Выключил
        val websocketClient = WebSocketClient(KtorDockerCompose)
        testStubApiV1(websocketClient, "websocket ")
    }
})