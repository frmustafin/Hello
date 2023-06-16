rootProject.name = "Hello"

include("m1l1-hello")
include("m2hm3")
include("m2hm3-common")
include("m2hm3-mappers")

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
include("untitled")
