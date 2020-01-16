import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
  `java-platform`
}

javaPlatform {
  allowDependencies()
}

dependencies {
  constraints {
    api(axonframework("configuration"))
    api(axonframework("eventsourcing"))
    api(axonframework("messaging"))
    api(axonframework("modelling"))
    api(axonframework("server-connector"))
    api(axonframework("spring"))
    api(axonframework("spring-boot-autoconfigure"))
    api(axonframework("spring-boot-starter"))
    api(axonframework("test"))
  }

  api(
    platform(SpringBootPlugin.BOM_COORDINATES)
  )
}
