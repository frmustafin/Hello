import ru.otus.otuskotlin.marketplace.common.models.MkplProfilePermissionClient
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalRelations
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<MkplUserPermissions>,
    relations: Iterable<MkplPrincipalRelations>,
) = mutableSetOf<MkplProfilePermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    MkplUserPermissions.READ_OWN to mapOf(
        MkplPrincipalRelations.OWN to MkplProfilePermissionClient.READ
    ),
    MkplUserPermissions.READ_GROUP to mapOf(
        MkplPrincipalRelations.GROUP to MkplProfilePermissionClient.READ
    ),
    MkplUserPermissions.READ_PUBLIC to mapOf(
        MkplPrincipalRelations.PUBLIC to MkplProfilePermissionClient.READ
    ),
    MkplUserPermissions.READ_CANDIDATE to mapOf(
        MkplPrincipalRelations.MODERATABLE to MkplProfilePermissionClient.READ
    ),

    // UPDATE
    MkplUserPermissions.UPDATE_OWN to mapOf(
        MkplPrincipalRelations.OWN to MkplProfilePermissionClient.UPDATE
    ),
    MkplUserPermissions.UPDATE_PUBLIC to mapOf(
        MkplPrincipalRelations.MODERATABLE to MkplProfilePermissionClient.UPDATE
    ),
    MkplUserPermissions.UPDATE_CANDIDATE to mapOf(
        MkplPrincipalRelations.MODERATABLE to MkplProfilePermissionClient.UPDATE
    ),

    // DELETE
    MkplUserPermissions.DELETE_OWN to mapOf(
        MkplPrincipalRelations.OWN to MkplProfilePermissionClient.DELETE
    ),
    MkplUserPermissions.DELETE_PUBLIC to mapOf(
        MkplPrincipalRelations.MODERATABLE to MkplProfilePermissionClient.DELETE
    ),
    MkplUserPermissions.DELETE_CANDIDATE to mapOf(
        MkplPrincipalRelations.MODERATABLE to MkplProfilePermissionClient.DELETE
    ),
)