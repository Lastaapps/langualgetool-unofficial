@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aboutLibraries)
}

android {
    namespace = "cz.lastaapps.languagetool"
    compileSdk = 34

    defaultConfig {
        applicationId = "cz.lastaapps.languagetool"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {

        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true

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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.arrowkt.bom))
    implementation(platform(libs.ktor.bom))

    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.arrowkt)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.ktor.client)

    implementation(libs.aboutLibraries.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.emoji2.bundled)
    implementation(libs.androidx.emoji2.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.ui)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}