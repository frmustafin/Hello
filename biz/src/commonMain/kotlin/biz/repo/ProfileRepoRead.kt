package biz.repo

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileIdRequest
import worker

fun ICorChainDsl<MkplContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbProfileIdRequest(profileValidated)
        val result = profileRepo.readProfile(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            profileRepoRead = resultAd
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}