package auth

import ProfileRepoInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplProfilePermissionClient
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Ignore

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
@Ignore
@OptIn(ExperimentalCoroutinesApi::class)
class AdCrudAuthTest {
    @Test
    fun createSuccessTest() = runTest {
        val userId = MkplUserId("123")
        val repo = ProfileRepoInMemory()
        val processor = MkplProfileProcessor(
            settings = MkplCorSettings(
                repoTest = repo
            )
        )
        val context = MkplContext(
            workMode = MkplWorkMode.TEST,
            profileRequest = MkplProfileStub.prepareResult {
                permissionsClient.clear()
                id = MkplUserId.NONE
            },
            command = MkplCommand.CREATE,
            principal = MkplPrincipalModel(
                id = userId,
                groups = setOf(
                    MkplUserGroups.USER,
                    MkplUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(MkplState.FINISHING, context.state)
        with(context.profileResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, MkplProfilePermissionClient.READ)
            assertContains(permissionsClient, MkplProfilePermissionClient.UPDATE)
            assertContains(permissionsClient, MkplProfilePermissionClient.DELETE)
//            assertFalse { permissionsClient.contains(PermissionModel.CONTACT) }
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val adObj = MkplProfileStub.get()
        val userId = adObj.id
        val adId = adObj.id
        val repo = ProfileRepoInMemory(initObjects = listOf(adObj))
        val processor = MkplProfileProcessor(
            settings = MkplCorSettings(
                repoTest = repo
            )
        )
        val context = MkplContext(
            command = MkplCommand.READ,
            workMode = MkplWorkMode.TEST,
            profileRequest = MkplProfile(id = adId),
            principal = MkplPrincipalModel(
                id = userId,
                groups = setOf(
                    MkplUserGroups.USER,
                    MkplUserGroups.TEST,
                )
            )
        )
        processor.exec(context)
        assertEquals(MkplState.FINISHING, context.state)
        with(context.profileResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, MkplProfilePermissionClient.READ)
            assertContains(permissionsClient, MkplProfilePermissionClient.UPDATE)
            assertContains(permissionsClient, MkplProfilePermissionClient.DELETE)
//            assertFalse { context.responseAd.permissions.contains(PermissionModel.CONTACT) }
        }
    }

}