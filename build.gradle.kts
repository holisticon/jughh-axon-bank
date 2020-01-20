plugins {
  base
  idea
}

allprojects {
  extra["kotlin.version"] = embeddedKotlinVersion
  group = "de.holisticon.axon.bank"

  apply {
    from("${rootProject.rootDir}/gradle/repositories.gradle.kts")
  }
//
//
//  pluginManager.withPlugin("java-library") {
//    apply {
//      plugin("io.freefair.lombok")
//    }
//
//    tasks {
//      withType<JavaCompile> {
//        sourceCompatibility = Versions.java
//        targetCompatibility = Versions.java
//      }
//      withType<io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig> {
//        enabled = false
//      }
//
//      withType<Test> {
//        useJUnitPlatform()
//      }
//    }
//  }

}


dependencies {
  subprojects.forEach {
    archives(it)
  }
}
