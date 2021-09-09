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
    implementation(kotlin("reflect"))

    api(project(":api"))

    api("org.apache.maven.resolver:maven-resolver-impl:1.7.1")
    api("org.apache.maven.resolver:maven-resolver-connector-basic:1.7.1")
    api("org.apache.maven.resolver:maven-resolver-transport-http:1.7.1")
    api("org.apache.maven:maven-resolver-provider:3.8.1")

    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation("org.slf4j:slf4j-log4j12:1.7.32")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0-M1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0-M1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}