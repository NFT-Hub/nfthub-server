import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("kapt") version "1.5.0"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.0"
}

repositories {
    mavenCentral()
}
group = "com.nftuhb.api"
java.sourceCompatibility = JavaVersion.VERSION_11
val querydslVersion = "5.0.0"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

kotlin.sourceSets {
    main {
        setBuildDir("$buildDir")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // map-struct
    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")

    // database
    implementation("org.postgresql:postgresql")
    runtimeOnly("mysql:mysql-connector-java")

    // aws
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.5.RELEASE")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.5.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // JPA 엔티티 히스토리
    implementation("org.springframework.data:spring-data-envers")

    // spring security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // jwt
    implementation("com.auth0:java-jwt:3.18.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    // devtool
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
    developmentOnly("com.h2database:h2")
    testImplementation("com.h2database:h2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.ninja-squad:springmockk:3.0.1")

    //testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}


val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = false