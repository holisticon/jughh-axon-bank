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

  api(contextModule("customer", "api"))

  api(axonFramework("eventsourcing"))
  api(axonFramework("spring"))

  implementation(springBoot("starter-web"))

  testImplementation(springBoot("starter-test"))
  testImplementation(axonFramework("test"))

  testImplementation("ch.qos.logback:logback-classic")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")


  //testImplementation("io.toolisticon.addons.axon:axon-jgiven")
  //testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
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
