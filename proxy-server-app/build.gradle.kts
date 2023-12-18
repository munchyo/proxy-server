plugins {
    id("java")
    id("java-library")
    id("org.springframework.boot") version("3.2.0")
}

dependencies {
    implementation(project(":proxy-server-a"))
    implementation(project(":proxy-server-b"))
    implementation(project(":proxy-server-common"))
}