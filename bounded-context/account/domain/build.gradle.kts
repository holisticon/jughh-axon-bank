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

  // BANK
  api(contextModule("account", "api"))

  // SPRINGBOOT/AXON
  api(axonFramework("eventsourcing"))
  api(axonFramework("spring"))
  implementation(springBoot("starter-web"))

  // TEST
  testImplementation(springBoot("starter-test"))
  testImplementation(axonFramework("test"))
  testImplementation("ch.qos.logback:logback-classic")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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
