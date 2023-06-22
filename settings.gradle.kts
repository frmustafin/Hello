rootProject.name = "Hello"

include("m1l1-hello")
include("jackson")
include("common")
include("mappers")

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
include("untitled")
