import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project


object Versions {

  val java = JavaVersion.VERSION_1_8.toString()
  val springBoot = "2.2.2.RELEASE"
  val axon = "4.2.1"
}

object Dependencies {
  fun DependencyHandlerScope.axonFramework(artifact: String, version: String? = null) =
    "org.axonframework:axon-$artifact" + (version?.let { ":$version" } ?: "")

  fun DependencyHandlerScope.springBoot(module: String) = "org.springframework.boot:spring-boot-$module"

  fun DependencyHandler.contextModule(context: String, module:String): ProjectDependency = this.project(":bounded-context:$context:$context-$module")

}


