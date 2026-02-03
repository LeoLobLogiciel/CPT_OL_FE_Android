# CPT Mobile - Android Development Guide

## Project Overview

CPT Mobile is the native Android application for the CPT business management system. It connects to the existing Node.js API and provides feature parity with the Vue.js/Quasar web application and iOS app.

**Package Name:** `ar.com.logiciel.cptmobile`
**Min SDK:** API 24 (Android 7.0 - Nougat)
**Target SDK:** API 35 (Android 15)
**Language:** Kotlin
**Architecture:** Clean Architecture + MVVM

## Technology Stack

### Core
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI framework
- **Material Design 3** - UI components and theming
- **Coroutines & Flow** - Asynchronous programming

### Architecture & DI
- **Hilt** - Dependency injection
- **MVVM** - Presentation layer pattern
- **Clean Architecture** - Multi-layer architecture (domain, data, presentation)

### Networking
- **Retrofit 2** - HTTP client
- **OkHttp** - Network layer with interceptors
- **Moshi** - JSON serialization/deserialization

### Data Persistence
- **DataStore** - Key-value storage for preferences
- Room (future) - Local database for offline support

### Navigation
- **Jetpack Navigation Compose** - Type-safe navigation

### Other Libraries
- **Timber** - Logging
- **Coil** - Image loading
- **Accompanist** - UI utilities (System UI Controller)
- **SplashScreen API** - Splash screen implementation

## Project Structure

```
app/src/main/java/ar/com/logiciel/cptmobile/
├── core/
│   ├── constants/      # API constants and configuration
│   ├── di/            # Hilt dependency injection modules
│   ├── network/       # Network utilities (NetworkResult wrapper)
│   └── util/          # Utility classes and extensions
├── data/
│   ├── local/         # Local data sources (DataStore, Room)
│   ├── remote/        # Remote data sources
│   │   ├── api/       # Retrofit API interfaces
│   │   ├── dto/       # Data Transfer Objects
│   │   └── interceptor/ # OkHttp interceptors
│   └── repository/    # Repository implementations
├── domain/
│   ├── model/         # Domain models (business entities)
│   ├── repository/    # Repository interfaces
│   └── usecase/       # Business logic use cases
├── presentation/
│   ├── splash/        # Splash screen
│   ├── login/         # Login screen and ViewModel
│   ├── home/          # Home screen (placeholder)
│   ├── navigation/    # Navigation setup
│   ├── components/    # Reusable UI components
│   └── theme/         # Material Design 3 theme
└── ui/theme/          # Legacy theme files (to be migrated)
```

## API Configuration

### Base URL
The API base URL is configured in `ApiConstants.kt`:

- **Emulator:** `http://10.0.2.2:8080/api/`
  (10.0.2.2 maps to localhost on the host machine)
- **Physical Device:** Use your computer's local IP address
  Example: `http://192.168.1.XXX:8080/api/`

### Authentication Headers
All API requests include these headers:
- `LSIPass: CPT!`
- `LSIToken: LSIToken1789`

These are automatically added by the `AuthInterceptor`.

## Dark Mode Support

The application fully supports both light and dark themes:

- Respects system theme preference by default
- Custom color schemes based on CPT branding
- Material Design 3 color system
- Dynamic colors disabled for consistent branding

### Theme Colors

**Light Theme:**
- Primary: #1976D2 (Blue)
- Secondary: #424242 (Dark Gray)
- Background: #FAFAFA

**Dark Theme:**
- Primary: #64B5F6 (Light Blue)
- Secondary: #9E9E9E (Gray)
- Background: #121212

## Sprint 1: Authentication (COMPLETED)

### Implemented Features
- ✅ Project setup with all dependencies
- ✅ Clean Architecture structure
- ✅ Hilt dependency injection
- ✅ Retrofit API configuration with LSIPass/LSIToken headers
- ✅ DataStore for local preferences
- ✅ Splash screen with auto-navigation
- ✅ Login screen with validation
- ✅ User authentication flow
- ✅ Dark mode support
- ✅ Material Design 3 theming

### Screen Flow
1. **Splash Screen** - Shows CPT logo, checks authentication status
2. **Login Screen** - Username/password form, connects to `/api/auth/login`
3. **Home Screen** - Placeholder for Sprint 2

### Key Files

#### Core Configuration
- `CPTMobileApplication.kt` - Application class with Hilt
- `ApiConstants.kt` - API base URL and configuration
- `NetworkModule.kt` - Retrofit, OkHttp, Moshi setup
- `DataStoreModule.kt` - DataStore configuration
- `RepositoryModule.kt` - Repository bindings

#### Data Layer
- `AuthApi.kt` - Login API interface
- `LoginRequest.kt` / `LoginResponse.kt` - DTOs
- `PreferencesManager.kt` - DataStore wrapper
- `AuthRepositoryImpl.kt` - Authentication repository

#### Domain Layer
- `User.kt` - User domain model
- `AuthRepository.kt` - Repository interface
- `LoginUseCase.kt` - Login business logic

#### Presentation Layer
- `MainActivity.kt` - Single activity with navigation
- `NavGraph.kt` - Navigation setup
- `SplashScreen.kt` / `SplashViewModel.kt` - Splash screen
- `LoginScreen.kt` / `LoginViewModel.kt` - Login screen
- `HomeScreen.kt` - Placeholder for next sprint

## Development Workflow

### Running the App

1. **Open in Android Studio**
   - File > Open > Select `/Users/leolob/Desarrollo/Clientes/CPT/cpt_online_android`
   - Wait for Gradle sync to complete

2. **Start the API Server**
   ```bash
   cd /Users/leolob/Desarrollo/Clientes/CPT/cpt_api
   npm run dev
   ```
   API should be running on `http://localhost:8080`

3. **Run the Android App**
   - Select the Pixel 6 API 33 emulator
   - Click Run (or press Ctrl+R)
   - The app will launch in the emulator

### Testing Login

**Test Credentials:**
- Ask the backend team for valid test credentials
- Or create a test user via the API

**Expected Flow:**
1. App shows splash screen for 1.5 seconds
2. If not logged in, navigates to Login screen
3. Enter username and password
4. Click "Ingreso al sistema"
5. On success, navigates to Home screen
6. On error, shows error message via Snackbar

### Debugging

**View Logs:**
- Android Studio Logcat
- Filter by "CPT" or "OkHttp" tags
- Timber logs include authentication flow and API calls

**Common Issues:**
- **Cannot connect to API:** Check that API is running and emulator is using 10.0.2.2
- **401 Unauthorized:** Check LSIPass/LSIToken headers in AuthInterceptor
- **Build errors:** Clean and rebuild project (Build > Clean Project, then Build > Rebuild)

## Code Style Guidelines

### Kotlin Conventions
- Use idiomatic Kotlin (data classes, extension functions, etc.)
- Prefer immutability (val over var)
- Use meaningful variable names
- Follow Clean Architecture principles

### Compose Best Practices
- Hoist state when possible
- Use remember for state within composables
- Prefer stateless composables
- Use ViewModel for business logic

### Testing
- Unit tests for ViewModels and Use Cases
- Integration tests for Repositories
- UI tests with Compose Testing library

## Next Steps: Sprint 2

### Planned Features
- Dashboard/Home screen with main features
- Navigation drawer or bottom navigation
- Feature modules (Pedidos, Ventas, Compras, etc.)
- Offline support with Room database
- Push notifications setup
- Real-time updates via Socket.io

### Technical Tasks
- Implement Room database for caching
- Add more API endpoints (articulos, clientes, etc.)
- Create reusable UI components
- Add unit and UI tests
- Implement error handling strategies
- Add analytics and crash reporting

## Resources

### Documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Hilt Documentation](https://dagger.dev/hilt/)
- [Retrofit](https://square.github.io/retrofit/)

### Project Links
- **API Repository:** `/Users/leolob/Desarrollo/Clientes/CPT/cpt_api`
- **Web Frontend:** `/Users/leolob/Desarrollo/Clientes/CPT/cpt_online`
- **iOS App:** `/Users/leolob/Desarrollo/Clientes/CPT/cpt_online_iOS`
- **Android App:** `/Users/leolob/Desarrollo/Clientes/CPT/cpt_online_android`
- **GitHub:** https://github.com/LeoLobLogiciel/CPT_OL_FE_Android

## Team Contacts

For questions about:
- **API Endpoints:** Backend team
- **UI/UX Specifications:** Check Quasar web app
- **iOS Feature Parity:** iOS team
- **Android Development:** Leo Lob

---

**Last Updated:** 2026-02-03
**Version:** 1.0.0
**Sprint:** 1 (Authentication) - COMPLETED
