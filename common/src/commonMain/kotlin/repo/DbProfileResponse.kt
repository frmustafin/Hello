package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.helpers.errorRepoConcurrency
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock

data class DbProfileResponse(
    override val data: MkplProfile?,
    override val isSuccess: Boolean,
    override val errors: List<MkplError> = emptyList()
): IDbResponse<MkplProfile> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbProfileResponse(null, true)
        fun success(result: MkplProfile) = DbProfileResponse(result, true)
        fun error(errors: List<MkplError>, data: MkplProfile? = null) = DbProfileResponse(data, false, errors)
        fun error(error: MkplError, data: MkplProfile? = null) = DbProfileResponse(data, false, listOf(error))

        val errorEmptyId = error(ru.otus.otuskotlin.marketplace.common.helpers.errorEmptyId)

        fun errorConcurrent(lock: MkplProfileLock, ad: MkplProfile?) = error(
            errorRepoConcurrency(lock, ad?.lock?.let { MkplProfileLock(it.asString()) }),
            ad
        )

        val errorNotFound = error(ru.otus.otuskotlin.marketplace.common.helpers.errorNotFound)
    }
}
