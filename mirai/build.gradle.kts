import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("com.github.johnrengelman.shadow").version("7.0.0")
}

group = "com.eloli"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  compileOnly(project(":api"))
  api("net.mamoe:mirai-core:2.9.1")
}

tasks.withType<ShadowJar> {
  archiveBaseName.set("mirai")
  archiveClassifier.set("app")

  dependencies{
    exclude("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    exclude("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    exclude("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    exclude("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    exclude("org.jetbrains.kotlin")
    exclude("org.slf4j")
    exclude("com.squareup.okio")
    exclude("com.squareup.okhttp3")
    exclude("org.apache.logging.log4j")
    exclude("org.jetbrains:annotations")
    exclude {
      (excludes.contains(it.moduleGroup) || excludes.contains(it.moduleGroup+":")
              || excludes.contains(":"+it.moduleName) || excludes.contains(it.moduleGroup+":"+it.moduleName))
    }
  }
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}