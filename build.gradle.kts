plugins {
    id("java")
    id("java-library")
    id("org.springframework.boot") version("3.2.0") apply false
}

allprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    group = "io.chiho.proxyserver"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    dependencies {
        // armeria core
        implementation("com.linecorp.armeria:armeria:1.26.4")
        // Integrating with Spring Boot
        implementation(platform("com.linecorp.armeria:armeria-bom:1.26.4"))
        implementation("com.linecorp.armeria:armeria-spring-boot3-starter")
        implementation("com.linecorp.armeria:armeria-tomcat10")

        // spring boot starter
        implementation("org.springframework.boot:spring-boot-starter:3.2.0")

        // logging
        runtimeOnly("ch.qos.logback:logback-classic:1.4.11")
        implementation("org.slf4j:slf4j-api:2.0.9")

        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        implementation("com.google.guava:guava:32.1.3-jre")
    }
}