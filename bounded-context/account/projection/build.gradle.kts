import io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig
import Dependencies.axonFramework
import Dependencies.springBoot
import Dependencies.contextModule

plugins {
  id("java-library")
  id("io.freefair.lombok")
}

dependencies {
  implementation(platform(project(":_platform")))

  api(contextModule("account", "api"))

  api(axonFramework("eventsourcing"))
  api(axonFramework("spring"))

  implementation(springBoot("starter-web"))

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
