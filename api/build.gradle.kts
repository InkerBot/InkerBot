group = "com.eloli"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public")
}

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
    api("com.google.inject:guice:5.0.1")
    api("com.google.guava:guava:30.1.1-jre")
    api("org.apache.commons:commons-text:1.9")
    api("org.slf4j:slf4j-api:1.7.32")
    api("com.google.code.gson:gson:2.8.8")
    api("org.yaml:snakeyaml:1.29")
    api("com.squareup.okhttp3:okhttp:4.9.1")
    api("org.hibernate.orm:hibernate-core:6.0.0.Alpha7")
}

kotlin{
    experimental {
        coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE
    }
}