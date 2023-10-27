import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

class ProfileRepoInMemoryDeleteTest: RepoProfileDeleteTest() {
    override val repo: IProfileRepository = ProfileRepoInMemory(
        initObjects = initObjects
    )
}