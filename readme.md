Application displays a list of games by selected genres from RAWG backend.

Application is built using KMP (Kotlin Multiplatform) and Android Compose.
It contains separate layouts for android and iOS. 
Layout for android is written in android compose and layout for iOS is written in Swift.
App demonstrates how shared KMP module can fetch, display and store data on native Android(Kotlin) and iOS(Swift) platforms.

# Installation instructions
In order to run the app please make sure that you add a RAWG API:
1) Register your account in https://rawg.io and get api key.
2) Add RAWG api key in 'shared' module, in 'Constants.kt' file (located in 'shared/src/commonMain/kotlin/com.zsasko.rawg_kmp.common/Constants.kt')

# Implementation details
- App architecture is MVI where each viewmodel contains 'handleIntent' method which receives an intent and does some business logic. StateFlow is used for notifying UI that data has been received. Screens contain their state variables based on which appropriate layouts are displayed (error, success, loading).
- Selected genres are saved in local database. When user clicks on genre, selected/deselected state is automatically saved in local Room db.
- If application is loaded without internet connection, on the screen where data is fetched from internet, an appropriate error layout is displayed.
- When data is being loaded, appropriate loading indicator is displayed.

# Android implementation details
- During app development latest 'pagination3' library is used in main/games screen which automatically invokes pagination (and loading data from backend).
- For navigation latest 'navigation3' library is used

# Shared (common) module implementation details
- For dependency injection an 'Koin' library is used and for networking requests 'Ktor' and 'Ktorfit' (library similar as 'Retrofit' android library) are used
- Data (selected game genre) is saved in Room db.
- In order to support StateFlow and Coroutines on both platforms (android and ios) an plugin 'com.rickclephas.kmp.nativecoroutines' is used
- In order to support Android Compose ViewModels in Swift, library 'kmp-observableviewmodel-core' is used.

# iOS implementation details
- Screens are displaying all three states: loading, success, error - like it's implemented on the android platform.
- Custom navigation drawer is created based on the following article:
  - https://medium.com/@jakir/navigation-drawer-side-menu-in-swiftui-66db892f7000
- Navigation handling is done based on the following article:
  - https://21zerixpm.medium.com/navigation-in-swiftui-the-complete-guide-to-moving-between-screens-fa487f879cff
- In order to support android compose viewmodel and coroutines and flow, as mentioned, 'observableviewmodel' and 'nativecoroutines' libraries are used 


