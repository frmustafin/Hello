import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalRelations

fun MkplProfile.resolveRelationsTo(principal: MkplPrincipalModel): Set<MkplPrincipalRelations> = setOfNotNull(
    MkplPrincipalRelations.NONE,
    MkplPrincipalRelations.NEW.takeIf { id == MkplUserId.NONE },
    MkplPrincipalRelations.MODERATABLE.takeIf { visibility != MkplVisibility.VISIBLE_TO_OWNER },
    MkplPrincipalRelations.PUBLIC.takeIf { visibility == MkplVisibility.VISIBLE_PUBLIC },
)