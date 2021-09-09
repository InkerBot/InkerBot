group = "com.eloli"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.google.inject:guice:5.0.1")
    api("com.google.guava:guava:30.1.1-jre")
    api("org.slf4j:slf4j-api:1.7.32")
    api("com.google.code.gson:gson:2.8.8")
    api("org.yaml:snakeyaml:1.29")
}