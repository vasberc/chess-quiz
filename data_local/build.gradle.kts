plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vasberc.data_local"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

dependencies {
    ksp(libs.koinKsp)
    ksp(libs.roomKsp)
    implementation(libs.bundles.core)
    implementation(libs.bundles.dataLocal)
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.androidTesting)
}