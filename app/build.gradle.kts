plugins {
    alias(libs.plugins.android.application)
    id("com.chaquo.python")
}

android {

    namespace = "com.example.loanprediction"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.loanprediction"

        minSdk = 24
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf(
                "arm64-v8a"
            )
        }
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }
}
chaquopy {
    defaultConfig {
        buildPython("py", "-3.10")
        version = "3.10"
        pip {
            install("numpy")
            install("pandas")
            install("scikit-learn")
            install("joblib")
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}