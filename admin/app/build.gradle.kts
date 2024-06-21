plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.example.urcafe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.urcafe"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets")
            }
        }
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {


    implementation(libs.material)
    implementation(libs.activity)
    implementation ("com.firebaseui:firebase-ui-database:7.1.1")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.airbnb.android:lottie:5.2.0")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation ("androidx.core:core-ktx:1.6.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.firebase:firebase-firestore:23.0.3")
    implementation("com.github.1902shubh:SendMail:1.0.0")
    implementation(libs.appcompat)
    implementation ("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation(libs.firebase.auth)


}