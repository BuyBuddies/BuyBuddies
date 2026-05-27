# BuyBuddies

BuyBuddies is an Android application designed to simplify your grocery shopping experience. It helps you create and manage shopping lists, share them with others, and keep track of your items.

## 🚧 (Work in progress) 🚧

## Features

*   **Grocery List Management:** Easily create, edit, and manage multiple grocery lists.
*   **User Authentication:** Supports both registered user accounts and a guest mode for quick access.
*   **Data Persistence:** Your lists are saved locally on your device using a Room database.
*   **Barcode Scanning:** Scan barcodes to quickly add items to your list.
*   **Shared Lists:** Collaborate on lists with friends and family.

## Architecture

This project follows modern Android architecture best practices and principles.

*   **UI Layer:** Built entirely with **Jetpack Compose** for a modern, declarative UI.
*   **Architecture Pattern:** Uses **MVVM (Model-View-ViewModel)** to separate UI from business logic.
*   **Dependency Injection:** **Hilt** is used for managing dependencies throughout the app.
*   **Navigation:** **Jetpack Navigation for Compose** handles all in-app navigation.
*   **Asynchronous Operations:** **Kotlin Coroutines and Flow** are used for managing background tasks and asynchronous data streams.
*   **Data Layer:**
    *   **Room:** For local data persistence.
    *   **Retrofit:** For handling network requests.
    *   **Repository Pattern:** To abstract data sources.

## Tech Stack & Dependencies

*   [Jetpack Compose](https://developer.android.com/jetpack/compose): For building the UI.
*   [Kotlin](https://kotlinlang.org/): The programming language.
*   [Hilt](https://dagger.dev/hilt/): For dependency injection.
*   [Room](https://developer.android.com/training/data-storage/room): For local database storage.
*   [Retrofit](https://square.github.io/retrofit/): For networking.
*   [Firebase Authentication](https://firebase.google.com/docs/auth): For user authentication.
*   [Jetpack Navigation](https://developer.android.com/guide/navigation): For in-app navigation.
*   [Coroutines & Flow](https://developer.android.com/kotlin/coroutines): For asynchronous programming.
*   [Coil](https://coil-kt.github.io/coil/): For image loading.
*   [ML Kit Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning): For scanning barcodes.

## How to Build

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Let Android Studio download the Gradle dependencies.
4.  If you are using Firebase, you will need to add your own `google-services.json` file to the `app/` directory.
5.  Build and run the app on an emulator or a physical device.
