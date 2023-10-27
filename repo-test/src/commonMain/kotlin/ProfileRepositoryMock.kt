import ru.otus.otuskotlin.marketplace.common.repo.DbProfileFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileResponse
import ru.otus.otuskotlin.marketplace.common.repo.DbProfilesResponse
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

class ProfileRepositoryMock(
    private val invokeCreateAd: (DbProfileRequest) -> DbProfileResponse = { DbProfileResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadAd: (DbProfileIdRequest) -> DbProfileResponse = { DbProfileResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateAd: (DbProfileRequest) -> DbProfileResponse = { DbProfileResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteAd: (DbProfileIdRequest) -> DbProfileResponse = { DbProfileResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchAd: (DbProfileFilterRequest) -> DbProfilesResponse = { DbProfilesResponse.MOCK_SUCCESS_EMPTY },
): IProfileRepository {
    override suspend fun createProfile(rq: DbProfileRequest): DbProfileResponse {
        return invokeCreateAd(rq)
    }

    override suspend fun readProfile(rq: DbProfileIdRequest): DbProfileResponse {
        return invokeReadAd(rq)
    }

    override suspend fun updateProfile(rq: DbProfileRequest): DbProfileResponse {
        return invokeUpdateAd(rq)
    }

    override suspend fun deleteProfile(rq: DbProfileIdRequest): DbProfileResponse {
        return invokeDeleteAd(rq)
    }

    override suspend fun searchProfile(rq: DbProfileFilterRequest): DbProfilesResponse {
        return invokeSearchAd(rq)
    }
}