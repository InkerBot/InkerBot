group = rootProject.group
version = rootProject.version

dependencies {
  api(kotlin("stdlib"))
  api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")
  api("com.google.inject:guice:5.0.1")
  api("com.google.guava:guava:31.0.1-jre")
  api("org.apache.commons:commons-text:1.9")
  api("org.slf4j:slf4j-api:1.7.32")
  api("com.google.code.gson:gson:2.8.9")
  api("org.yaml:snakeyaml:1.29")
  api("com.squareup.okhttp3:okhttp:4.9.3")
  api("org.hibernate.orm:hibernate-core:6.0.0.Alpha7")
  api("com.eloli.command:terminal:1.0-SNAPSHOT")
  api("com.eloli.command:ktdsl:1.0-SNAPSHOT")
  api("org.reflections:reflections:0.10.2")
  api("org.quartz-scheduler:quartz:2.3.2")
}
repositories {
  mavenCentral()
}

kotlin {
  experimental {
    coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE
  }
}