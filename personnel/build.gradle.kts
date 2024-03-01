import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springVersion = "3.2.2"

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.noarg") version "1.9.22"
    id("org.flywaydb.flyway") version "9.22.3"
    idea
}

group = "com.itavgur.omul"
version = "0.0.2"

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
    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.properties["springdoc-webmvc.version"]}")
    //security
    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    //web
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    //cache
    implementation("org.springframework.boot:spring-boot-starter-cache:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:$springVersion")
    //transport
    implementation("org.springframework.kafka:spring-kafka:${project.properties["spring-kafka.version"]}")

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
    user = "personnel"
    password = "personnel"
    schemas = arrayOf("personnel")
}