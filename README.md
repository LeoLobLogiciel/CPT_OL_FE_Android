# CPT Mobile - Android

Native Android application for the CPT business management system.

## Quick Info

- **Platform:** Android 7.0+ (API 24+)
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material Design 3
- **Architecture:** Clean Architecture + MVVM
- **Dependency Injection:** Hilt
- **Network:** Retrofit + OkHttp + Moshi

## Current Status: Sprint 1 COMPLETED

### Implemented Features
- Project setup with modern Android architecture
- Authentication flow (Login, Splash)
- Dark mode support
- API integration with LSIPass/LSIToken headers
- Local data persistence with DataStore

### Next: Sprint 2
- Dashboard/Home screen
- Main feature modules
- Offline support with Room
- Push notifications

## Quick Start

### Prerequisites
- Android Studio 2024.2.1 or later
- JDK 17 (embedded with Android Studio)
- Android SDK Platform 35
- Pixel 6 API 33 emulator (or any device with API 24+)
- CPT API server running on http://localhost:8080

### Running the App

1. **Start API Server**
   ```bash
   cd ../cpt_api
   npm run dev
   ```

2. **Open Project in Android Studio**
   - File > Open > Select `cpt_online_android`
   - Wait for Gradle sync

3. **Run on Emulator**
   - Start Pixel 6 API 33 emulator
   - Click Run (Ctrl+R)

### Testing Login

- Open the app in the emulator
- Enter valid credentials from the API
- Click "Ingreso al sistema"
- Should navigate to Home screen on success

## Project Structure

```
app/src/main/java/ar/com/logiciel/cptmobile/
├── core/           # Constants, DI, utilities
├── data/           # Repositories, API, DTOs
├── domain/         # Use cases, models, interfaces
├── presentation/   # UI (Screens, ViewModels)
└── ui/theme/       # Material Design 3 theme
```

## Key Technologies

- **Jetpack Compose** - Modern declarative UI
- **Hilt** - Dependency injection
- **Retrofit** - REST API client
- **Moshi** - JSON serialization
- **Coroutines & Flow** - Async programming
- **Navigation Compose** - Screen navigation
- **DataStore** - Preferences storage
- **Timber** - Logging

## Documentation

- **[SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md)** - Detailed setup and troubleshooting
- **[DEVELOPMENT.md](DEVELOPMENT.md)** - Architecture and development guide
- **API Docs:** See `../cpt_api` project

## API Configuration

The app connects to the CPT API with these headers:
- `LSIPass: CPT!`
- `LSIToken: LSIToken1789`

**Base URL:**
- Emulator: `http://10.0.2.2:8080/api/`
- Physical device: Update to your computer's IP in `ApiConstants.kt`

## Features

### Sprint 1: Authentication (CURRENT)
- [x] Splash screen with auto-login
- [x] Login screen with validation
- [x] Secure credential storage
- [x] Dark mode support
- [x] Error handling and user feedback

### Sprint 2: Core Features (PLANNED)
- [ ] Home/Dashboard screen
- [ ] Navigation drawer
- [ ] Pedidos module
- [ ] Ventas module
- [ ] Offline data caching
- [ ] Push notifications

### Future Sprints
- [ ] Compras module
- [ ] Stock management
- [ ] Reports and analytics
- [ ] Camera integration
- [ ] Biometric authentication

## Development

### Building
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

### Code Style
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Maintain Clean Architecture separation
- Write meaningful commit messages

### Git Workflow
```bash
git checkout -b feature/your-feature-name
# Make changes
git add .
git commit -m "feat: description"
git push origin feature/your-feature-name
# Create pull request
```

## Troubleshooting

**Cannot connect to API:**
- Verify API is running: `curl http://localhost:8080/api`
- Check emulator network: use `10.0.2.2` instead of `localhost`
- For physical device: update IP in `ApiConstants.kt`

**Build errors:**
- Clean project: Build > Clean Project
- Invalidate caches: File > Invalidate Caches / Restart
- Check Logcat for details

**Sync issues:**
- Update Android Studio to latest version
- Verify internet connection
- Check Gradle JDK is set to JDK 17

## Support

For questions or issues:
1. Check `SETUP_INSTRUCTIONS.md` for common problems
2. Review `DEVELOPMENT.md` for architecture details
3. Check Logcat for error messages
4. Contact the development team

## Repository

**GitHub:** https://github.com/LeoLobLogiciel/CPT_OL_FE_Android

## Related Projects

- **API:** `../cpt_api` - Node.js REST API
- **Web App:** `../cpt_online` - Vue 3 + Quasar
- **iOS App:** `../cpt_online_iOS` - Native iOS

## License

Proprietary - CPT Internal Use Only

---

**Version:** 1.0.0
**Last Updated:** 2026-02-03
**Status:** Sprint 1 Complete - Authentication ✅
