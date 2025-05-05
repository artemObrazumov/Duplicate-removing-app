pluginManagement {
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

rootProject.name = "My contacts"
include(":app")
include(":common:feature:contacts:aidl")
include(":common:feature:contacts:domain")
include(":core:domain")
include(":core:presentation")
include(":common:feature:contacts:data")
include(":feature:contacts:presentation")
include(":feature:contacts:duplicates_cleaning")
