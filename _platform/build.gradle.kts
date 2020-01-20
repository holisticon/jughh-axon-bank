import org.springframework.boot.gradle.plugin.SpringBootPlugin
import Dependencies.axonFramework

plugins {
  `java-platform`
}

javaPlatform {
  allowDependencies()
}

dependencies {
  constraints {
    api(axonFramework("configuration", Versions.axon))
    api(axonFramework("eventsourcing",Versions.axon))
    api(axonFramework("messaging",Versions.axon))
    api(axonFramework("modelling",Versions.axon))
    api(axonFramework("server-connector",Versions.axon))
    api(axonFramework("spring",Versions.axon))
    api(axonFramework("spring-boot-autoconfigure",Versions.axon))
    api(axonFramework("spring-boot-starter",Versions.axon))
    api(axonFramework("test",Versions.axon))

    //api("io.toolisticon.addons.axon:axon-jgiven:4.2.1-0.2-SNAPSHOT")
  }

  api(
    platform(SpringBootPlugin.BOM_COORDINATES)
  )
}
