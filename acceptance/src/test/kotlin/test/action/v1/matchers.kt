package test.action.v1

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.and
import me.frmustafin.swipe.api.v1.models.IResponse
import me.frmustafin.swipe.api.v1.models.ProfileCreateResponse
import me.frmustafin.swipe.api.v1.models.ProfileDeleteResponse
import me.frmustafin.swipe.api.v1.models.ProfileOffersResponse
import me.frmustafin.swipe.api.v1.models.ProfileReadResponse
import me.frmustafin.swipe.api.v1.models.ProfileResponseObject
import me.frmustafin.swipe.api.v1.models.ProfileUpdateResponse
import me.frmustafin.swipe.api.v1.models.ResponseResult

fun haveResult(result: ResponseResult) = Matcher<IResponse> {
    MatcherResult(
        it.result == result,
        { "actual result ${it.result} but we expected $result" },
        { "result should not be $result" }
    )
}

val haveNoErrors = Matcher<IResponse> {
    MatcherResult(
        it.errors.isNullOrEmpty(),
        { "actual errors ${it.errors} but we expected no errors" },
        { "errors should not be empty" }
    )
}

fun haveError(code: String) = haveResult(ResponseResult.ERROR)
    .and(Matcher<IResponse> {
        MatcherResult(
            it.errors?.firstOrNull { e -> e.code == code } != null,
            { "actual errors ${it.errors} but we expected error with code $code" },
            { "errors should not contain $code" }
        )
    })

val haveSuccessResult = haveResult(ResponseResult.SUCCESS) and haveNoErrors

val IResponse.profile: ProfileResponseObject?
    get() = when (this) {
        is ProfileCreateResponse -> profile
        is ProfileReadResponse -> profile
        is ProfileUpdateResponse -> profile
        is ProfileDeleteResponse -> profile
        is ProfileOffersResponse -> profile
        else -> throw IllegalArgumentException("Invalid response type: ${this::class}")
    }
