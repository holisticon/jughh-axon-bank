import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.embeddedKotlinVersion

object Versions {

  val java = JavaVersion.VERSION_1_8.toString()
  val springBoot = "2.2.2.RELEASE"
  val axon = "4.2.1"
}

fun DependencyHandlerScope.axonframework(artifact:String) = "org.axonframework:axon-$artifact:${Versions.axon}"
