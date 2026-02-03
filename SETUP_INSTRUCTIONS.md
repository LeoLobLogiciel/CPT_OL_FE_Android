# Setup Instructions for CPT Mobile Android

## Step 1: Sync Gradle Dependencies

After opening the project in Android Studio:

1. **Wait for Gradle Sync**
   - Android Studio will automatically start syncing Gradle
   - This may take 5-10 minutes on first run (downloading dependencies)
   - Watch the bottom status bar for progress

2. **If Sync Fails:**
   - Click "File > Invalidate Caches / Restart"
   - After restart, click "File > Sync Project with Gradle Files"
   - Check "Build" window for error messages

3. **Common Sync Issues:**

   **Issue:** "Kotlin version mismatch"
   - **Solution:** Update Kotlin plugin in Android Studio
   - Go to: File > Settings > Plugins > search "Kotlin" > Update

   **Issue:** "SDK not found"
   - **Solution:** File > Settings > Appearance & Behavior > System Settings > Android SDK
   - Install Android SDK Platform 35 and Build Tools

   **Issue:** "Java version incompatible"
   - **Solution:** File > Settings > Build, Execution, Deployment > Build Tools > Gradle
   - Set Gradle JDK to "Embedded JDK (JDK 17)"

## Step 2: Configure Emulator

1. **Open Device Manager**
   - Tools > Device Manager (or AVD Manager)

2. **Verify Pixel 6 API 33 Exists**
   - Should see "Pixel_6_API_33" in the list
   - If not, create it following the instructions from the previous setup

3. **Start Emulator**
   - Click the Play button next to Pixel_6_API_33
   - Wait for emulator to fully boot (1-2 minutes)

## Step 3: Verify API Server

Before running the Android app, ensure the API is running:

```bash
# Terminal 1: Start API server
cd /Users/leolob/Desarrollo/Clientes/CPT/cpt_api
npm run dev

# You should see:
# Server listening on port 8080
```

**Test API Connection from Terminal:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -H "LSIPass: CPT!" \
  -H "LSIToken: LSIToken1789" \
  -d '{"Username":"test","Password":"test123"}'
```

## Step 4: Build and Run

1. **Select Build Variant**
   - View > Tool Windows > Build Variants
   - Select "debug" (should be default)

2. **Run the App**
   - Click the Run button (green play icon) or press Ctrl+R
   - Select "Pixel_6_API_33" as target device
   - Wait for build and installation

3. **Expected Build Time:**
   - First build: 2-5 minutes
   - Incremental builds: 10-30 seconds

## Step 5: Test the App

### Initial Launch
1. App shows splash screen with CPT logo (placeholder for now)
2. After 1.5 seconds, navigates to Login screen

### Test Login Flow

**Scenario 1: Valid Login**
1. Enter valid username (e.g., `admin`)
2. Enter valid password
3. Click "Ingreso al sistema"
4. Should show loading indicator
5. On success, navigates to Home screen (placeholder)

**Scenario 2: Invalid Login**
1. Enter invalid credentials
2. Click "Ingreso al sistema"
3. Should show error message at bottom of screen
4. Message: "Usuario inexistente o contraseña incorrecta"

**Scenario 3: Empty Fields**
1. Leave username or password empty
2. Click "Ingreso al sistema"
3. Should show validation error

### Check Logs
1. Open Logcat in Android Studio (bottom toolbar)
2. Filter by "CPT" or "OkHttp" tags
3. Verify API calls are being made:
   ```
   D/OkHttp: POST http://10.0.2.2:8080/api/auth/login
   D/OkHttp: --> Header: LSIPass: CPT!
   D/OkHttp: --> Header: LSIToken: LSIToken1789
   ```

## Step 6: Troubleshooting

### Problem: "Unable to resolve dependency"
**Solution:**
1. Check internet connection
2. File > Invalidate Caches / Restart
3. Delete `.gradle` folder in project root
4. Sync project again

### Problem: "Cannot connect to API"
**Symptoms:** Login always fails with connection error

**Solutions:**
1. Verify API is running: `curl http://localhost:8080/api/auth/login`
2. Check emulator can reach host:
   ```bash
   # In emulator terminal (Tools > Device Manager > Terminal icon)
   adb shell
   curl http://10.0.2.2:8080/api/auth/login
   ```
3. If using physical device, update `ApiConstants.BASE_URL` with your computer's IP:
   ```kotlin
   // In ApiConstants.kt
   const val BASE_URL = "http://192.168.1.XXX:8080/api/"
   ```

### Problem: "Manifest merger failed"
**Solution:**
1. Build > Clean Project
2. Build > Rebuild Project

### Problem: App crashes on launch
**Check:**
1. Logcat for stack trace
2. Verify all Hilt modules are properly configured
3. Check that CPTMobileApplication is set in AndroidManifest.xml

### Problem: Dark mode not working
**Verify:**
1. System theme is set correctly on emulator
2. Settings > Display > Dark theme
3. Or use quick settings dropdown

## Step 7: Making Changes

### Modify API Base URL
File: `app/src/main/java/ar/com/logiciel/cptmobile/core/constants/ApiConstants.kt`

```kotlin
object ApiConstants {
    // For emulator
    const val BASE_URL = "http://10.0.2.2:8080/api/"

    // For physical device (replace with your IP)
    // const val BASE_URL = "http://192.168.1.100:8080/api/"

    // For production
    // const val BASE_URL = "https://api.example.com/api/"
}
```

### Modify Authentication Headers
File: `app/src/main/java/ar/com/logiciel/cptmobile/data/remote/interceptor/AuthInterceptor.kt`

```kotlin
val modifiedRequest = originalRequest.newBuilder()
    .addHeader(ApiConstants.HEADER_LSI_PASS, ApiConstants.DEFAULT_LSI_PASS)
    .addHeader(ApiConstants.HEADER_LSI_TOKEN, ApiConstants.DEFAULT_LSI_TOKEN)
    // Add custom headers here
    .build()
```

### Enable Detailed Logging
File: `app/src/main/java/ar/com/logiciel/cptmobile/core/di/NetworkModule.kt`

```kotlin
fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor { message ->
        Timber.tag("OkHttp").d(message)
    }.apply {
        // Change to BODY for detailed request/response logging
        level = HttpLoggingInterceptor.Level.BODY
    }
}
```

## Step 8: Git Workflow

### Before Committing
```bash
# Check status
git status

# Add files (be specific, avoid adding build artifacts)
git add app/src/main/java/...
git add gradle/libs.versions.toml
git add app/build.gradle.kts

# Commit
git commit -m "feat: implement login screen and authentication"

# Push
git push origin main
```

### Files to NEVER Commit
- `.gradle/`
- `build/`
- `local.properties`
- `.idea/` (except version control settings)
- `*.apk`
- `*.aab`

## Step 9: Next Development Session

### Quick Start Checklist
- [ ] Start API server: `cd cpt_api && npm run dev`
- [ ] Open Android Studio
- [ ] Start emulator (Pixel_6_API_33)
- [ ] Run app (Ctrl+R)
- [ ] Verify login works
- [ ] Check Git status: `git status`

## Need Help?

### Useful Commands
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Check dependencies
./gradlew app:dependencies
```

### Documentation Links
- [Android Developers](https://developer.android.com)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose/documentation)
- [Hilt Guide](https://developer.android.com/training/dependency-injection/hilt-android)
- [Retrofit Guide](https://square.github.io/retrofit/)

### Project Documentation
- `DEVELOPMENT.md` - Detailed architecture and development guide
- `README.md` - Project overview
- API docs: Check `cpt_api` project

---

**Last Updated:** 2026-02-03
**Android Studio Version:** 2024.2.1 or later recommended
**Gradle Version:** 8.x
