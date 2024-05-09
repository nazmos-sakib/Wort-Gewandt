plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.wortgewandt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wortgewandt"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.fragment:fragment:1.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //MultiMap
    implementation ("com.google.collections:google-collections:1.0-rc2")

    //volley
    implementation("com.android.volley:volley:1.2.1")

    //jsoup
    implementation("org.jsoup:jsoup:1.17.2")

    //apache file download
    implementation("commons-io:commons-io:2.16.1")


    // Serialize MultiMap
    //implementation ("com.google.code.gson:gson:2.10.1")
}