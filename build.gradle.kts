import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
  id("org.springframework.boot") version "3.1.0"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.8.21"
  kotlin("plugin.spring") version "1.8.21"
  id("com.diffplug.spotless") version "6.19.0"
  kotlin("kapt") version "1.8.0"
}

group = "com.afidalgo"

version = "0.0.1-SNAPSHOT"

java { sourceCompatibility = JavaVersion.VERSION_17 }

val spaceUsername: String? by project
val spacePassword: String? by project
val userName: String? = System.getenv("SPACE_USERNAME")
val passWord: String? = System.getenv("SPACE_PASSWORD")
val usr = userName ?: spaceUsername // checks env first
val psw = passWord ?: spacePassword // checks env first
val urlArtifactRepository = ext["jetbrains.url"].toString()
val sharedLibraryVersion = ext["shared.library.version"].toString()

repositories {
  mavenCentral()
  maven {
    url = uri(urlArtifactRepository)
    credentials {
      username = usr
      password = psw
    }
  }
}

ext {
  set("otelVersion", "1.17.0")
  set("springCloudVersion", "2022.0.3")
  set("testcontainersVersion", "1.18.0")
  set("testKeycloakVersion", "2.3.0")
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  // https://mvnrepository.com/artifact/org.yaml/snakeyaml
  implementation("org.yaml:snakeyaml:2.0")
  implementation("org.springframework.cloud:spring-cloud-starter-config")
  implementation("org.springframework.retry:spring-retry")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.flywaydb:flyway-core")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("io.micrometer:micrometer-registry-prometheus")
  runtimeOnly("io.opentelemetry.javaagent:opentelemetry-javaagent:${property("otelVersion")}")
  kapt("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.5")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation(
      "com.github.dasniko:testcontainers-keycloak:${property("testKeycloakVersion")}")
  implementation("org.flywaydb:flyway-core")
  implementation("com.afidalgo:shared-library:$sharedLibraryVersion")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> { useJUnitPlatform() }

configure<SpotlessExtension> {
  kotlin {
    // by default the target is every '.kt' and '.kts` file in the java sourcesets
    ktfmt() // has its own section below
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
  }
}

tasks.bootRun.configure { systemProperty("spring.profiles.active", "test-data") }

tasks.named<BootBuildImage>("bootBuildImage") {
  imageName.set(project.name)
  environment.set(environment.get() + mapOf("BP_JVM_VERSION" to "17"))
  docker {
    publishRegistry {
      username.set(project.findProperty("registryUsername").toString())
      password.set(project.findProperty("registryToken").toString())
      url.set(project.findProperty("registryUrl").toString())
    }
  }
}

dependencyManagement {
  imports {
    mavenBom(
        "org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
  }
}

springBoot { buildInfo() }
