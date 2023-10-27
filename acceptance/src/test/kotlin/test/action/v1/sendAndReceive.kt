package test.action.v1

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import fixture.client.Client
import me.frmustafin.swipe.api.v1.models.IRequest
import me.frmustafin.swipe.api.v1.models.IResponse
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV1RequestSerialize(request)
    log.info { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.info { "Received\n$responseBody" }

    return apiV1ResponseDeserialize(responseBody)
}