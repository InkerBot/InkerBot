plugins {
    kotlin("jvm") version "1.5.10"
}

group = "com.eloli"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "kotlin")
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
}
dependencies {
    implementation(kotlin("stdlib"))

    api(project(":api"))

    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation("org.slf4j:slf4j-log4j12:1.7.32")
}