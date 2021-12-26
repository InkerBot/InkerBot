plugins {
  kotlin("jvm") version "1.5.10"
  `maven-publish`
  id("bot.inker.boot.scaner").version("1.1.2-SNAPSHOT")
}

group = "bot.inker"
version = "1.0-SNAPSHOT"
// version = "1.0-SNAPSHOT-${System.currentTimeMillis()}"

subprojects {
  apply(plugin = "kotlin")
}

allprojects {
  apply(plugin = "maven-publish")
  repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    maven("https://www.jitpack.io")
    mavenCentral()
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



  implementation("org.apache.logging.log4j:log4j-api:2.17.0")
  implementation("org.apache.logging.log4j:log4j-core:2.17.0")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")
  implementation("com.h2database:h2:2.0.202")

  booterClasspath(kotlin("stdlib")) // Idea issue: If kotlin not in appClassloader, It can't debug this
  booterClasspath(kotlin("reflect"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0-M1")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0-M1")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.bootRepo {
  mainClass.set("bot.inker.core.MainKt")
  mavenUrl.set("http://localhost:8080/")
  repoUrl.set("http://localhost:8080/repo")
}

tasks.devRepoServer {
  port.set(8080)
}

tasks.jijJar {
  archiveBaseName.set("InkerBot")
  archiveVersion.set("")
  archiveClassifier.set("app")
}