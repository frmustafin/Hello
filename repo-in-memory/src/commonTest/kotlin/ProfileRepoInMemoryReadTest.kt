import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

class ProfileRepoInMemoryReadTest: RepoProfileReadTest() {
    override val repo: IProfileRepository = ProfileRepoInMemory(
        initObjects = initObjects
    )
}