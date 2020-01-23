rootProject.name = "jughh-axon-bank"

include("_platform")

include("application:all-in-one")
include("application:dashboard")

/**
 * We must ensure that two modules in two contexts (eg. "projection" in "forderungsdaten" and "verpflichtungsdetails"
 * have different effective project names, thats why we not just include but also configure the project name here.
 */
fun includeBoundedContext(context: String, modules: List<String>) = modules.forEach {
  val projectpath = "bounded-context:$context:$it"
  include(projectpath)
  project(":$projectpath").name = "$context-$it"
}


includeBoundedContext(
  "account",
  listOf(
    "api",
    "domain",
    "projection"
  )
)
includeBoundedContext(
  "customer",
  listOf(
    "api",
    "domain",
    "projection"
  )
)
