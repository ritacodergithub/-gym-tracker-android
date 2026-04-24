plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Compose compiler plugin (Kotlin 2.x)
    alias(libs.plugins.ksp)            // Room annotation processor
}

android {
    // NOTE: when you're ready to publish, rename this to something like
    // "com.yourname.gymtracker" (both namespace and applicationId must match
    // the directory under src/main/java after rename).
    namespace = "com.example.e_commerce"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.e_commerce"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }

        // Read the Gemini API key from local.properties (gitignored) and
        // expose it as BuildConfig.GEMINI_API_KEY. Fall back to empty so
        // the app still compiles without a key; the AI screen will show
        // a friendly "add your key" state in that case.
        val geminiKey: String = providers
            .fileContents(rootProject.layout.projectDirectory.file("local.properties"))
            .asText
            .map { text ->
                text.lineSequence()
                    .firstOrNull { it.startsWith("gemini.api.key=") }
                    ?.substringAfter("=")
                    ?.trim()
                    ?: ""
            }
            .orElse("")
            .get()
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // --- Core / AppCompat (kept from template) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    // --- Activity + Lifecycle + Compose ---
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // --- Compose BOM: one BOM pins every Compose artifact version. ---
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Navigation ---
    implementation(libs.androidx.navigation.compose)

    // --- Room (KSP for annotation processing) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.android)

    // --- Coil (image loading for offline exercise previews) ---
    implementation(libs.coil.compose)

    // --- WorkManager (daily reminder) ---
    implementation(libs.androidx.work.runtime.ktx)

    // --- Retrofit + Moshi + OkHttp logging (AI coach) ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging)

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}