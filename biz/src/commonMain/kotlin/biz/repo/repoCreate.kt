package biz.repo

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileRequest
import worker

fun ICorChainDsl<MkplContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbProfileRequest(profileRepoPrepare)
        val result = profileRepo.createProfile(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            profileRepoDone = resultAd
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}