package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile

data class DbProfilesResponse(
    override val data: List<MkplProfile>?,
    override val isSuccess: Boolean,
    override val errors: List<MkplError> = emptyList(),
): IDbResponse<List<MkplProfile>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbProfilesResponse(emptyList(), true)
        fun success(result: List<MkplProfile>) = DbProfilesResponse(result, true)
        fun error(errors: List<MkplError>) = DbProfilesResponse(null, false, errors)
        fun error(error: MkplError) = DbProfilesResponse(null, false, listOf(error))
    }
}