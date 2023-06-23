package fr.mustafin.demo.api.v1.controller

import fr.mustafin.demo.service.MkplProfileBlockingProcessor
import me.frmustafin.swipe.api.v1.models.*
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportCreate
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportDelete
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportRead
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportSearch
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportUpdate

@RestController
@RequestMapping("v1/profile")
class ProfileController(private val processor: MkplProfileBlockingProcessor) {

    @PostMapping("create")
    fun createAd(@RequestBody request: ProfileCreateRequest): ProfileCreateResponse {
        val context = MkplContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    fun readAd(@RequestBody request: ProfileReadRequest): ProfileReadResponse {
        val context = MkplContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportRead()
    }

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody request: ProfileUpdateRequest): ProfileUpdateResponse {
        val context = MkplContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody request: ProfileDeleteRequest): ProfileDeleteResponse {
        val context = MkplContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody request: ProfileSearchRequest): ProfileSearchResponse {
        val context = MkplContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportSearch()
    }
}
