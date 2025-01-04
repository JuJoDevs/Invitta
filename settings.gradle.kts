@file:Suppress("UnstableApiUsage")

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Invitta"
include(":app")
include(":core:coroutines")
include(":core:design-system")
include(":core:domain")
include(":core:string-resources")
include(":core:testing")
include(":feature:home:api")
include(":feature:home:impl")
include(":library:logger:impl")
include(":library:logger:api")
include(":library:remote-config:api")
include(":library:remote-config:impl")
