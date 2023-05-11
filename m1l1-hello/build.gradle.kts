import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") 
}

dependencies {
    testImplementation(kotlin("test-junit"))
}
