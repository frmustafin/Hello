package biz.repo

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileIdRequest
import worker

fun ICorChainDsl<MkplContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbProfileIdRequest(profileRepoPrepare)
        val result = profileRepo.deleteProfile(request)
        if (!result.isSuccess) {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
        profileRepoDone = profileRepoRead
    }
}