package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import fr.mustafin.demo.service.MkplProfileBlockingProcessor
import me.frmustafin.swipe.api.v1.models.ProfileOffersRequest
import me.frmustafin.swipe.api.v1.models.ProfileOffersResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportOffers

@RestController
@RequestMapping("v1/ad")
class OfferController(private val processor: MkplProfileBlockingProcessor) {

    @PostMapping("offers")
    fun searchOffers(@RequestBody request: ProfileOffersRequest): ProfileOffersResponse {
        val context = MkplContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportOffers()
    }
}
