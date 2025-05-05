plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.artem_obrazumov.contacts.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // modules
    implementation(project(Modules.Core.Domain))
    implementation(project(Modules.Core.Presentation))
    implementation(project(Modules.Common.Feature.Contacts.Domain))
    implementation(project(Modules.Common.Feature.Contacts.Data))
    implementation(project(Modules.Common.Feature.Contacts.Aidl))
    implementation(project(Modules.Feature.Contacts.DuplicatesCleaning))

    // bom
    implementation(platform(libs.androidx.compose.bom))

    // bundles
    implementation(libs.bundles.android.base)
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.serialization)
    implementation(libs.bundles.dagger)
    kapt(libs.bundles.dagger.compiler)
    debugImplementation(libs.bundles.compose.debug)
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.testing.android)
}