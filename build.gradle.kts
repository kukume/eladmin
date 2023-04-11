plugins {
    val kotlinVersion = "1.8.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.lombok") version kotlinVersion
    id("io.freefair.lombok") version "6.4.0"
}

group = "me.kuku"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        maven("https://nexus.kuku.me/repository/maven-public/")
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("org.jetbrains.kotlin.plugin.lombok")
        plugin("io.freefair.lombok")
    }

    repositories {
        maven("https://nexus.kuku.me/repository/maven-public/")
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("me.kuku:utils:2.2.4.0")
        implementation("org.springframework.boot:spring-boot-starter-cache")
        implementation("org.springframework.boot:spring-boot-starter-websocket")
        implementation("org.springframework.boot:spring-boot-starter-mail")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.alibaba:druid-spring-boot-starter:1.2.16")
        implementation("com.querydsl:querydsl-core:5.0.0")
        implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
        implementation("cn.dev33:sa-token-spring-boot3-starter:1.34.0")
        implementation("cn.dev33:sa-token-jwt:1.34.0")
        implementation("cn.dev33:sa-token-dao-redis-jackson:1.34.0")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
        implementation("org.netbeans.external:com-jcraft-jsch:RELEASE170")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.apache.poi:poi-ooxml:5.2.3")
        kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
        kapt("org.springframework.boot:spring-boot-configuration-processor")
        kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
        implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
        kapt("com.google.dagger:dagger-compiler:2.45")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
//        implementation("com.h2database:h2:2.1.214")
        implementation("com.mysql:mysql-connector-j:8.0.32")
//        implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5-jakarta")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate6:2.15.0-rc1")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.compileKotlin {
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict", "-Xcontext-receivers")
    }

    tasks.compileJava {
        options.encoding = "utf-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    kotlin {
        jvmToolchain(17)
    }

    kapt {
        keepJavacAnnotationProcessors = true
    }


}
kotlinLombok {
    lombokConfigurationFile(file("lombok.config"))
}
