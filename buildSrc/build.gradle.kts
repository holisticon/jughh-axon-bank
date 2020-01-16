import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

apply {
    from("../gradle/repositories.gradle.kts")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
  implementation("org.springframework.boot:spring-boot-gradle-plugin:2.2.2.RELEASE")
  implementation("io.freefair.gradle:lombok-plugin:4.1.6")

  // docs with orchid
  implementation("gradle.plugin.com.eden:orchidPlugin:0.17.7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
    }
}
