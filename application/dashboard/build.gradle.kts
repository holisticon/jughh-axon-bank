import io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig
import Dependencies.axonFramework
import Dependencies.springBoot
import Dependencies.contextModule

plugins {
  id("java-library")
  id("org.springframework.boot")
  id("io.freefair.lombok")
}

dependencies {
  implementation(platform(project(":_platform")))

  implementation(contextModule("account","api"))
  implementation(contextModule("customer","api"))

  implementation(springBoot("starter-web"))
  implementation(axonFramework("spring-boot-starter"))
  implementation("io.toolisticon.springboot:springboot-swagger-starter:0.0.4")

  testImplementation("ch.qos.logback:logback-classic")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("org.assertj:assertj-core:3.11.1")
  testImplementation("org.awaitility:awaitility:4.0.2")
}


tasks {
  withType<JavaCompile> {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
  }
  withType<GenerateLombokConfig> {
    enabled = false
  }

  withType<Test> {
    useJUnitPlatform()
  }
}
