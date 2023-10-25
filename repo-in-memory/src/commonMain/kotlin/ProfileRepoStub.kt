import ru.otus.otuskotlin.marketplace.common.models.MkplGender
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileResponse
import ru.otus.otuskotlin.marketplace.common.repo.DbProfilesResponse
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub

class ProfileRepoStub() : IProfileRepository {
    override suspend fun createProfile(rq: DbProfileRequest): DbProfileResponse {
        return DbProfileResponse(
            data = MkplProfileStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readProfile(rq: DbProfileIdRequest): DbProfileResponse {
        return DbProfileResponse(
            data = MkplProfileStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateProfile(rq: DbProfileRequest): DbProfileResponse {
        return DbProfileResponse(
            data = MkplProfileStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteProfile(rq: DbProfileIdRequest): DbProfileResponse {
        return DbProfileResponse(
            data = MkplProfileStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchProfile(rq: DbProfileFilterRequest): DbProfilesResponse {
        return DbProfilesResponse(
            data = MkplProfileStub.prepareManSearchList(filter = 21, MkplGender.WOMAN),
            isSuccess = true,
        )
    }
}