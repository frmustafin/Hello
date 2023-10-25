package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

data class MkplCorSettings(
    val repoStub: IProfileRepository = IProfileRepository.NONE,
    val repoTest: IProfileRepository = IProfileRepository.NONE,
    val repoProd: IProfileRepository = IProfileRepository.NONE,
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}