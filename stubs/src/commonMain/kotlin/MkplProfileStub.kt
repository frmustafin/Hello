package ru.otus.otuskotlin.marketplace.stubs

import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStubMan.PROFILE_MAN
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStubMan.PROFILE_WOMAN

object MkplProfileStub {
    fun get(): MkplProfile = PROFILE_MAN.copy()

    fun prepareResult(block: MkplProfile.() -> Unit): MkplProfile = get().apply(block)

    fun prepareManSearchList(filter: Int, gender: MkplGender) = listOf(
        mkplProfileMan("d-666-01", filter, gender),
        mkplProfileMan("d-666-02", filter, gender),
        mkplProfileMan("d-666-03", filter, gender),
        mkplProfileMan("d-666-04", filter, gender),
        mkplProfileMan("d-666-05", filter, gender),
        mkplProfileMan("d-666-06", filter, gender),
    )

    fun prepareManOffersList(filter: Int, gender: MkplGender) = listOf(
        mkplProfileWoman("s-666-01", filter, gender),
        mkplProfileWoman("s-666-02", filter, gender),
        mkplProfileWoman("s-666-03", filter, gender),
        mkplProfileWoman("s-666-04", filter, gender),
        mkplProfileWoman("s-666-05", filter, gender),
        mkplProfileWoman("s-666-06", filter, gender),
    )

    private fun mkplProfileMan(id: String, filter: Int, gender: MkplGender) =
        mkplProfile(PROFILE_MAN, id = id, filter = filter, gender = gender)

    private fun mkplProfileWoman(id: String, filter: Int, gender: MkplGender) =
        mkplProfile(PROFILE_WOMAN, id = id, filter = filter, gender = gender)

    private fun mkplProfile(base: MkplProfile, id: String, filter: Int, gender: MkplGender) = base.copy(
        id = MkplUserId(id),
        age = filter,
        description = "desc $filter $id",
        mkplGender = gender,
    )

}
