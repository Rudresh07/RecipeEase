plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}



android {
    namespace = "com.example.recipeease"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.recipeease"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY")}\"")



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
    buildFeatures{
        viewBinding = true
        buildConfig = true
        //noinspection DataBindingWithoutKapt
        dataBinding=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation ("com.google.firebase:firebase-firestore:25.0.0")  // Replace with latest compatible version
        implementation ("com.google.firebase:firebase-auth:23.0.0")  // Replace with latest compatible version
        implementation ("com.google.firebase:firebase-storage:21.0.0")  // Replace with latest compatible version

        //retrofit dependency
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson converter for JSON parsing

        //image viewer
        implementation("com.squareup.picasso:picasso:2.71828")

        //lifecycle implementation
        implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
        implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

        //Gemini integration

        implementation("com.google.ai.client.generativeai:generativeai:0.2.0")

        implementation("com.google.android.material:material:1.11.0")

        implementation("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")


        implementation("de.hdodenhof:circleimageview:3.1.0")


        val room_version = "2.6.1"

        implementation("androidx.room:room-runtime:$room_version")
        annotationProcessor("androidx.room:room-compiler:$room_version")


        // To use Kotlin Symbol Processing (KSP)
        ksp("androidx.room:room-compiler:$room_version")

        // optional - Kotlin Extensions and Coroutines support for Room
        implementation("androidx.room:room-ktx:$room_version")

        // optional - RxJava2 support for Room
        implementation("androidx.room:room-rxjava2:$room_version")

        // optional - RxJava3 support for Room
        implementation("androidx.room:room-rxjava3:$room_version")

        // optional - Guava support for Room, including Optional and ListenableFuture
        implementation("androidx.room:room-guava:$room_version")

        // optional - Test helpers
        testImplementation("androidx.room:room-testing:$room_version")

        // optional - Paging 3 Integration
        implementation("androidx.room:room-paging:$room_version")

        implementation (libs.kotlinx.coroutines.android)
        implementation (libs.kotlinx.coroutines.core)


        implementation ("com.github.bumptech.glide:glide:4.13.2")
        annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")
    }
}