import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springVersion = "3.2.3"

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.noarg") version "1.9.23"
    id("org.flywaydb.flyway") version "9.22.3"
    idea
}

group = "com.itavgur.omul"
version = "0.0.3"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //spring boot
    implementation("org.springframework.boot:spring-boot-configuration-processor:$springVersion")
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springVersion")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$springVersion")
    //AOP
    implementation("org.springframework.boot:spring-boot-starter-aop:$springVersion")
    implementation("org.yaml:snakeyaml:2.2")
    //kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    //DB
    implementation("org.springframework.boot:spring-boot-starter-jdbc:$springVersion")
    implementation("org.postgresql:postgresql:${project.properties["postgres.version"]}")
    implementation("org.flywaydb:flyway-core:${project.properties["flydb.version"]}")
    //monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:${project.properties["micrometer.version"]}")
    //tests
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.mockito:mockito-core:${project.properties["mockito-core.version"]}")
    testImplementation("com.tngtech.archunit:archunit-junit5:${project.properties["arch-junit.version"]}")
    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.properties["springdoc-webmvc.version"]}")
    //security
    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    implementation("io.jsonwebtoken:jjwt-api:${project.properties["json-webtoken-libraries.version"]}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${project.properties["json-webtoken-libraries.version"]}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${project.properties["json-webtoken-libraries.version"]}")
    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    //web
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    //cache
    implementation("org.springframework.boot:spring-boot-starter-cache:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:$springVersion")
    //transport
    implementation("org.springframework.kafka:spring-kafka:${project.properties["spring-kafka.version"]}")
    //util
    implementation("org.modelmapper:modelmapper:${project.properties["model-mapper.version"]}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

flyway {
    url = "jdbc:postgresql://localhost/omul"
    user = "schedule"
    password = "schedule"
    schemas = arrayOf("schedule")
}

noArg {
    annotation("NoArgConstructor")
}