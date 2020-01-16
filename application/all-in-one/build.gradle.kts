plugins {
  id("java-library")
  id("org.springframework.boot")
}

dependencies {
  fun springBoot(module : String) = "org.springframework.boot:spring-boot-$module"

  implementation(
    platform(project(":_platform"))
  )

  implementation(springBoot("starter-web"))

}
