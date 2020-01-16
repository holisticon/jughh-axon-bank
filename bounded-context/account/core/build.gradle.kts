import io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig

plugins {
  id("java-library")
  id("io.freefair.lombok")
}

dependencies {
  fun springBoot(module: String) = "org.springframework.boot:spring-boot-$module"
  implementation(platform(project(":_platform")))

  api("org.axonframework:axon-eventsourcing")
  api("org.axonframework:axon-spring")

  implementation(springBoot("starter-web"))

  testImplementation(springBoot("starter-test"))
  testImplementation(axonframework("test"))

  testImplementation("ch.qos.logback:logback-classic")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("io.toolisticon.addons.axon:axon-jgiven")
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
