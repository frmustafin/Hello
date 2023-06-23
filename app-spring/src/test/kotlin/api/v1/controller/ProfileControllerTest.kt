package fr.mustafin.demo.api.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import fr.mustafin.demo.service.MkplProfileBlockingProcessor
import me.frmustafin.swipe.api.v1.models.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*

@WebMvcTest(ProfileController::class, OffersController::class)
internal class ProfileControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    private lateinit var processor: MkplProfileBlockingProcessor

    @Test
    fun createProfile() = testStubProfile(
        "/v1/profile/create",
        ProfileCreateRequest(),
        MkplContext().toTransportCreate()
    )

    @Test
    fun readProfile() = testStubProfile(
        "/v1/profile/read",
        ProfileReadRequest(),
        MkplContext().toTransportRead()
    )

    @Test
    fun updateProfile() = testStubProfile(
        "/v1/profile/update",
        ProfileUpdateRequest(),
        MkplContext().toTransportUpdate()
    )

    @Test
    fun deleteProfile() = testStubProfile(
        "/v1/profile/delete",
        ProfileDeleteRequest(),
        MkplContext().toTransportDelete()
    )

    @Test
    fun searchProfile() = testStubProfile(
        "/v1/profile/search",
        ProfileSearchRequest(),
        MkplContext().toTransportSearch()
    )

    @Test
    fun searchOffers() = testStubProfile(
        "/v1/profile/offers",
        ProfileOffersRequest(),
        MkplContext().toTransportOffers()
    )

    private fun <Req : Any, Res : Any> testStubProfile(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        val request = mapper.writeValueAsString(requestObj)
        val response = mapper.writeValueAsString(responseObj)

        mvc.perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(response))
    }
}
