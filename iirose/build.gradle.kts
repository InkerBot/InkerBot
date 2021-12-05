plugins {
  id("com.github.johnrengelman.shadow")
}

group = rootProject.group
version = rootProject.version

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
  archiveVersion.set("")
  archiveClassifier.set("app")
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}