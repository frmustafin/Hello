package ru.otus.otuskotlin.marketplace.common.repo

interface IProfileRepository {
    suspend fun createProfile(rq: DbProfileRequest): DbProfileResponse
    suspend fun readProfile(rq: DbProfileIdRequest): DbProfileResponse
    suspend fun updateProfile(rq: DbProfileRequest): DbProfileResponse
    suspend fun deleteProfile(rq: DbProfileIdRequest): DbProfileResponse
    suspend fun searchProfile(rq: DbProfileFilterRequest): DbProfilesResponse
    companion object {
        val NONE = object : IProfileRepository {
            override suspend fun createProfile(rq: DbProfileRequest): DbProfileResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readProfile(rq: DbProfileIdRequest): DbProfileResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateProfile(rq: DbProfileRequest): DbProfileResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteProfile(rq: DbProfileIdRequest): DbProfileResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchProfile(rq: DbProfileFilterRequest): DbProfilesResponse {
                TODO("Not yet implemented")
            }
        }
    }
}