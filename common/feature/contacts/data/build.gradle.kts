plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.artem_obrazumov.contacts.data"
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
}

dependencies {

    // modules
    implementation(project(Modules.Core.Domain))
    implementation(project(Modules.Common.Feature.Contacts.Domain))

    // bundles
    implementation(libs.bundles.android.base)
    implementation(libs.bundles.dagger)
    kapt(libs.bundles.dagger.compiler)
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.testing.android)
}