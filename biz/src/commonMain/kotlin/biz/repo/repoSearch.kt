package biz.repo

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbProfileFilterRequest
import worker

fun ICorChainDsl<MkplContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbProfileFilterRequest(
            titleFilter = profileFilterValidated.searchString,
            ownerId = profileFilterValidated.ownerId,
        )
        val result = profileRepo.searchProfile(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            profilesRepoDone = resultAds.toMutableList()
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}