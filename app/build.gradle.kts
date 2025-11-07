import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose")
}

// Read local.properties safely (no internal AGP API)
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) FileInputStream(f).use { load(it) }
}
val DEV_BASE_URL = (localProps.getProperty("DEV_BASE_URL") ?: "http://10.0.2.2:8080/").trim()
val PROD_BASE_URL = (localProps.getProperty("PROD_BASE_URL")).trim()

android {
    namespace = "com.example.prm392project"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.prm392project"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            buildConfigField("String", "DEV_BASE_URL", "\"$DEV_BASE_URL\"")
            buildConfigField("String", "PROD_BASE_URL", "\"$PROD_BASE_URL\"")
        }
        create("prod") {
            buildConfigField("String", "DEV_BASE_URL", "\"$DEV_BASE_URL\"")
            buildConfigField("String", "PROD_BASE_URL", "\"$PROD_BASE_URL\"")
        }
    }

    buildTypes {
        debug { isDebuggable = true }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    // AGP 8+ requires Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    // Optional: ensure toolchain uses JDK 17
    kotlin {
        jvmToolchain(17)
    }

    composeOptions { kotlinCompilerExtensionVersion = "1.5.1" }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.browser)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:<compose_version>")


    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}