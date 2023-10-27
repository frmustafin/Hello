package biz.groups

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorAdministration
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository
import worker

fun ICorChainDsl<MkplContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        profileRepo = when {
            workMode == MkplWorkMode.TEST -> settings.repoTest
            workMode == MkplWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != MkplWorkMode.STUB && profileRepo == IProfileRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}