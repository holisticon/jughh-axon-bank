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

}

dependencies {
  subprojects.forEach {
    archives(it)
  }
}
