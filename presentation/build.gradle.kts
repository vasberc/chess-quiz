plugins {
    id("com.android.library")
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vasberc.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

//Commented out because the presentation module cant see the data_local module
//and at the moment the koin cannot understand at compile time that the interfaces in presentation module will be
//resolved at run time by the data_local module
//ksp {
//    arg("KOIN_CONFIG_CHECK", "true")
//}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    ksp(libs.koinKsp)
    implementation(libs.bundles.core)
    implementation(libs.bundles.presentation)
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.androidTesting)
    debugImplementation(libs.bundles.presentationDebug)
}