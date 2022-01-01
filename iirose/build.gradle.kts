plugins {
  id("com.github.johnrengelman.shadow").version("7.0.0")
}

group = rootProject.group
version = rootProject.version

repositories {
  mavenCentral()
}

dependencies {
  compileOnly(project(":api"))
}

tasks.shadowJar {
  archiveBaseName.set("iirose")
  archiveVersion.set("")
  archiveClassifier.set("app")

  dependencies{
    exclude("org.jetbrains.kotlin")
    exclude {
      (excludes.contains(it.moduleGroup) || excludes.contains(it.moduleGroup+":")
              || excludes.contains(":"+it.moduleName) || excludes.contains(it.moduleGroup+":"+it.moduleName))
    }
  }
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}