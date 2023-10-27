package test.action.v1

import me.frmustafin.swipe.api.v1.models.ProfileCreateObject
import me.frmustafin.swipe.api.v1.models.ProfileDebug
import me.frmustafin.swipe.api.v1.models.ProfileRequestDebugMode
import me.frmustafin.swipe.api.v1.models.ProfileRequestDebugStubs
import me.frmustafin.swipe.api.v1.models.ProfileVisibility

val debug = ProfileDebug(mode = ProfileRequestDebugMode.STUB, stub = ProfileRequestDebugStubs.SUCCESS)
val prod = ProfileDebug(mode = ProfileRequestDebugMode.PROD)

val someCreateProfile = ProfileCreateObject(
    name = "Ivan",
    description = "Одинокий работяга с завода",
    visibility = ProfileVisibility.PUBLIC
)