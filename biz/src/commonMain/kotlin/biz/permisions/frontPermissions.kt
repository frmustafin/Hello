package biz.permisions

import ICorChainDsl
import resolveFrontPermissions
import resolveRelationsTo
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import worker

fun ICorChainDsl<MkplContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == MkplState.RUNNING }

    handle {
        profileRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                profileRepoDone.resolveRelationsTo(principal)
            )
        )

        for (ad in profilesRepoDone) {
            ad.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    ad.resolveRelationsTo(principal)
                )
            )
        }
    }
}