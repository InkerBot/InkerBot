plugins {
  kotlin("jvm") version "1.5.10"
  id("com.github.johnrengelman.shadow").version("7.0.0")
  `maven-publish`
}

group = "bot.inker"
version = "1.0-SNAPSHOT-${System.currentTimeMillis()}"

subprojects {
  apply(plugin = "kotlin")
}

allprojects {
  apply(plugin = "maven-publish")
  repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://www.jitpack.io")
  }
  publishing {
    repositories {
      if (project.properties.containsKey("mavenUploadEnable")) {
        maven(project.properties["mavenRepoUrl"]!!) {
          credentials {
            username = project.properties["mavenRepoUsername"] as String
            password = project.properties["mavenRepoPassword"] as String
          }
        }
      }
      mavenLocal()
    }
    publications {
      create<MavenPublication>("maven") {
        from(components["java"])
      }
    }
  }
}

dependencies {
  api(project(":api"))

  api("org.apache.maven.resolver:maven-resolver-impl:1.7.1")
  api("org.apache.maven.resolver:maven-resolver-connector-basic:1.7.1")
  api("org.apache.maven.resolver:maven-resolver-transport-http:1.7.1")
  api("org.apache.maven:maven-resolver-provider:3.8.1")



  implementation("org.apache.logging.log4j:log4j-api:2.14.1")
  implementation("org.apache.logging.log4j:log4j-core:2.14.1")
  implementation("org.slf4j:slf4j-log4j12:1.7.32")
  implementation("com.h2database:h2:1.4.200")


  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0-M1")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0-M1")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.jar {
  manifest {
    attributes["Main-Class"] = "bot.inker.core.MainKt"
  }
}

tasks.shadowJar {
  archiveBaseName.set("InkerBot")
  archiveVersion.set("")
  archiveClassifier.set("app")
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}