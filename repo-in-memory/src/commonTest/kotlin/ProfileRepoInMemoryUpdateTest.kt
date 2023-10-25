import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

class ProfileRepoInMemoryUpdateTest: RepoProfileUpdateTest() {
    override val repo: IProfileRepository = ProfileRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}