import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    id("com.google.devtools.ksp") version "2.3.10"
}

// GIPHY API key: put  GIPHY_API_KEY=your_key  in local.properties (not committed),
// or pass -PGIPHY_API_KEY=your_key on the Gradle command line.
val giphyApiKey: String = run {
    val props = Properties()
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { props.load(it) }
    props.getProperty("GIPHY_API_KEY")
        ?: (project.findProperty("GIPHY_API_KEY") as String?)
        ?: ""
}

android {
    namespace = "com.flasskdev.vibe"

    // Стабильная базовая 37-я версия
    compileSdk = 37

    defaultConfig {
        applicationId = "com.flasskdev.vibe"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Exposed to code as BuildConfig.GIPHY_API_KEY
        buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    // Главная настройка Java 21 для проекта
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// ПРАВИЛЬНОЕ МЕСТО: Настройки компилятора Kotlin для Gradle 9.x+ задаются вне блока android
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.lottie.compose)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.chrisbanes.haze)
    implementation(libs.liquid.glass)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // Image Loading
    implementation(libs.coil.compose)
    
    // WorkManager
    implementation(libs.work.runtime.ktx)
    
    // DocumentFile
    implementation("androidx.documentfile:documentfile:1.0.1")
    
    // Media3 (ExoPlayer)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.media3.ui)

    // Coil Video
    implementation(libs.coil)
    implementation(libs.coil.video)   // ← без этого обложек не будет
    implementation(libs.coil.gif)
}