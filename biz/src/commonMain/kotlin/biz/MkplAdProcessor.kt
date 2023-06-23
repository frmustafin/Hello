package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplGender
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub

class MkplProfileProcessor {
    suspend fun exec(ctx: MkplContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == MkplWorkMode.STUB) {
            "Currently working only in STUB mode."
        }

        when (ctx.command) {
            MkplCommand.SEARCH -> {
                ctx.profilesResponse.addAll(MkplProfileStub.prepareManSearchList(23, MkplGender.MAN))
            }
            MkplCommand.OFFERS -> {
                ctx.profilesResponse.addAll(MkplProfileStub.prepareManOffersList(23, MkplGender.WOMAN))
            }
            else -> {
                ctx.profileResponse = MkplProfileStub.get()
            }
        }
    }
}
