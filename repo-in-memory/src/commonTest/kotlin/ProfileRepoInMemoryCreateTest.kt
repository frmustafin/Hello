class ProfileRepoInMemoryCreateTest: RepoProfileCreateTest() {
    override val repo = ProfileRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}