package biz.repo

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import worker

fun ICorChainDsl<MkplContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        val request = DbProfileRequest(profileRepoPrepare)
        val result = profileRepo.updateProfile(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            profileRepoDone = resultAd
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
            profileRepoDone
        }
    }
}
