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
  implementation(kotlin("stdlib"))
  compileOnly(project(":api"))
  api("net.mamoe:mirai-core:2.7.0")
}

tasks.withType<ShadowJar> {
  archiveBaseName.set("mirai")
  archiveClassifier.set("app")
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}