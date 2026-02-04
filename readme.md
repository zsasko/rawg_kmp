# RAWG Games KMP App

The application displays a list of games by selected genres from the RAWG backend.

This app is built using **Kotlin Multiplatform (KMP)** and **Android Compose**, with separate layouts for Android and iOS.
- Android layout is implemented in **Android Compose**.
- iOS layout is implemented in **Swift**.

The app demonstrates how a shared KMP module can fetch, display, and store data on native Android (Kotlin) and iOS (Swift) platforms.

---

## Screenshots

### Android
| Games                                                                                     | Game Details                                                                                     |
|-------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| ![](https://raw.githubusercontent.com/zsasko/rawg_kmp/main/docs/images/android_games.png) | ![](https://raw.githubusercontent.com/zsasko/rawg_kmp/main/docs/images/android_game_details.png) |

### iOS
| Games                                                                                     | Game Details                                                                                     | Navigation Drawer                                                                                 | Select Genres                                                                                     |
|-------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| ![](https://raw.githubusercontent.com/zsasko/rawg_kmp/main/docs/images/iphone_games.png)  | ![](https://raw.githubusercontent.com/zsasko/rawg_kmp/main/docs/images/iphone_game_details.png) | ![](https://raw.githubusercontent.com/zsasko/rawg_kmp/main/docs/images/iphone_navigation_drawer.png) | ![](https://raw.githubusercontent.com/zsasko/rawg_kmp/main/docs/images/iphone_select_genres.png) |

---

## Installation Instructions

To run the app, you need to add a RAWG API key:

1. Register your account at [RAWG](https://rawg.io) and get an API key.
2. Add your RAWG API key in the `shared` module, in the `Constants.kt` file (located in `shared/src/commonMain/kotlin/com/zsasko.rawg_kmp.common/Constants.kt`)

---

## Implementation Details

- **Architecture:** MVI pattern where each ViewModel contains a `handleIntent` method that executes business logic.
    - `StateFlow` is used to notify the UI when data is available.
    - Screens display layouts based on state (error, success, loading).
- **Genre Selection:** Selected genres are saved in the local database.
    - Clicking a genre automatically updates its selected/deselected state in Room.
- **Offline Handling:** Displays an error layout if data cannot be fetched due to no internet connection.
- **Loading Indicators:** Shown while data is being loaded.

---

## Android Implementation Details

- Uses **Pagination3** for automatic pagination and data loading in the main games screen.
- Uses **Navigation3** for navigation between screens.

---

## Shared (Common) Module Implementation Details

- **Dependency Injection:** Koin
- **Networking:** Ktor and Ktorfit (similar to Retrofit on Android)
- **Persistence:** Selected genres saved in Room database
- **Coroutines & StateFlow:**
    - `com.rickclephas.kmp.nativecoroutines` plugin to support StateFlow and Coroutines on both Android and iOS.
    - `kmp-observableviewmodel-core` library to expose Android Compose ViewModels to Swift.

---

## iOS Implementation Details

- Screens display all three states: loading, success, and error (like Android).
- Custom navigation drawer based on:  
  [Navigation Drawer Side Menu in SwiftUI](https://medium.com/@jakir/navigation-drawer-side-menu-in-swiftui-66db892f7000)
- Navigation handling based on:  
  [Navigation in SwiftUI: Complete Guide](https://21zerixpm.medium.com/navigation-in-swiftui-the-complete-guide-to-moving-between-screens-fa487f879cff)
- Supports Android Compose ViewModel, Coroutines, and StateFlow via `observableviewmodel` and `nativecoroutines` libraries.

---

## Libraries & Tools Used

- **UI & Architecture:** Android Compose, SwiftUI, MVI
- **Persistence:** Room
- **Networking:** Ktor, Ktorfit
- **Dependency Injection:** Koin
- **Multiplatform Support:** Kotlin Multiplatform, NativeCoroutines, ObservableViewModel
- **Pagination & Navigation:** Pagination3, Navigation3  


