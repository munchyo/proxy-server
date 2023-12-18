plugins {
    id("java")
    id("java-library")
    id("org.springframework.boot") version("3.2.0") apply false
}

dependencies {
    implementation(project(":proxy-server-common"))
}