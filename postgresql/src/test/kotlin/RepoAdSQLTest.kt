package ru.otus.otuskotlin.marketplace.backend.repo.sql

import RepoProfileCreateTest
import RepoProfileDeleteTest
import RepoProfileReadTest
import RepoProfileSearchTest
import RepoProfileUpdateTest
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository


class RepoAdSQLCreateTest : RepoProfileCreateTest() {
    override val repo: IProfileRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSQLDeleteTest : RepoProfileDeleteTest() {
    override val repo: IProfileRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLReadTest : RepoProfileReadTest() {
    override val repo: IProfileRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLSearchTest : RepoProfileSearchTest() {
    override val repo: IProfileRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoAdSQLUpdateTest : RepoProfileUpdateTest() {
    override val repo: IProfileRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}
