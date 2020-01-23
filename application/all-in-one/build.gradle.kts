import io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig
import Dependencies.axonFramework
import Dependencies.springBoot
import Dependencies.contextModule

plugins {
  id("java-library")
  id("org.springframework.boot")
}

dependencies {
  implementation(platform(project(":_platform")))

  implementation(contextModule("account","domain"))
  implementation(contextModule("account","projection"))

  implementation(contextModule("customer","domain"))
  implementation(contextModule("customer","projection"))

  implementation(springBoot("starter-web"))
  implementation(axonFramework("spring-boot-starter"))

  implementation("io.toolisticon.springboot:springboot-swagger-starter:0.0.4")
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
