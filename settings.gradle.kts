rootProject.name = "Swipe"

include("m1l1-hello")
include("jackson")
include("common")
include("mappers")
include("app-spring")
include("biz")
include("stubs")
include("rabbit")
include("lib-core")
include("repo-in-memory")
include("repo-test")
include("postgresql")
include("acceptance")
include("Auth")

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings
    val ktorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false

        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false

        id("io.ktor.plugin") version ktorVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}