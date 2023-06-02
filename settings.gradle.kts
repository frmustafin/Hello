rootProject.name = "Hello"

//include("m1l1-hello")
include("m2l8-marketing")

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
