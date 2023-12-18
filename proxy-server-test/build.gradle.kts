plugins {
    id("java")
    id("java-library")
    id("org.springframework.boot") version("3.2.0") apply false
}

dependencies {
    testImplementation(project(":proxy-server-app"))
    testImplementation(project(":proxy-server-a"))
    testImplementation(project(":proxy-server-b"))
    testImplementation(project(":proxy-server-common"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // armeria core
    testImplementation("com.linecorp.armeria:armeria:1.26.4")
    // Integrating with Spring Boot
    testImplementation(platform("com.linecorp.armeria:armeria-bom:1.26.4"))
    testImplementation("com.linecorp.armeria:armeria-spring-boot3-starter")

    // spring boot starter
    testImplementation("org.springframework.boot:spring-boot-starter:3.2.0")

    // logging
    testRuntimeOnly("ch.qos.logback:logback-classic:1.4.11")
    testImplementation("org.slf4j:slf4j-api:2.0.9")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}
