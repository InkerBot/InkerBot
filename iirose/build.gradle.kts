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
}

kotlin{
    experimental {
        coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("iirose")
    archiveClassifier.set("app")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}