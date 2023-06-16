plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":m2hm3"))
    implementation(project(":m2hm3-common"))

    testImplementation(kotlin("test-junit"))
}