import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}


allprojects {
    group = "me.frmustafin.swipe"
    version = "1.0-SNAPSHOT"

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
