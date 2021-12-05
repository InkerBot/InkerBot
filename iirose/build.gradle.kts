plugins {
  id("com.github.johnrengelman.shadow")
}

group = "com.eloli"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  compileOnly(project(":api"))
}

kotlin {
  experimental {
    coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE
  }
}

tasks.shadowJar {
  archiveBaseName.set("iirose")
  archiveClassifier.set("app")
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}