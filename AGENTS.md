# CPT Mobile - Android Agent Guide

> Este archivo contiene toda la información necesaria para que un agente de IA pueda trabajar eficientemente en el proyecto CPT Mobile Android.

## 🎯 Project Overview

**CPT Mobile** es la aplicación Android nativa para el sistema de gestión empresarial CPT. Conecta con la API Node.js existente y provee funcionalidad equivalente a la web app (Vue.js/Quasar) y la app iOS.

| Atributo | Valor |
|----------|-------|
| **Package** | `ar.com.logiciel.cptmobile` |
| **Min SDK** | API 24 (Android 7.0) |
| **Target SDK** | API 35 (Android 15) |
| **Language** | Kotlin 100% |
| **UI Framework** | Jetpack Compose + Material Design 3 |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Hilt |
| **Current Version** | 1.0.0 (Build 21) |

---

## 🏗️ Architecture

### Clean Architecture Layers

```
app/src/main/java/ar/com/logiciel/cptmobile/
├── core/                 # Core utilities, constants, DI
│   ├── constants/        # ApiConstants
│   ├── di/              # Hilt modules (NetworkModule, RepositoryModule, etc.)
│   ├── network/         # NetworkResult wrapper
│   └── util/            # Extension functions
├── data/                # Data layer
│   ├── local/           # DataStore (PreferencesManager)
│   ├── remote/          # API layer
│   │   ├── api/         # Retrofit interfaces
│   │   ├── dto/         # Data Transfer Objects
│   │   └── interceptor/ # AuthInterceptor
│   └── repository/      # Repository implementations
├── domain/              # Domain layer
│   ├── model/           # Domain models + tablero models
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Use cases
├── presentation/        # UI layer (Compose)
│   ├── splash/          # Splash screen
│   ├── login/           # Login screen
│   ├── home/            # Home with drawer navigation
│   ├── tablero/         # Dashboard widgets
│   ├── ventas/          # Sales module
│   ├── pedidos/         # Orders module
│   ├── clientes/        # Clients module
│   ├── profile/         # User profile
│   ├── navigation/      # NavGraph + Screen sealed class
│   └── common/          # Reusable components (DateFilterSheet)
└── ui/theme/            # Material Design 3 theme
```

### Dependency Injection Setup

**Hilt Modules** (`core/di/`):
- `NetworkModule`: Retrofit, OkHttp, Moshi, API clients
- `RepositoryModule`: Binds interfaces to implementations
- `DataStoreModule`: Preferences DataStore
- `ThemeModule`: ThemeManager para dark/light mode

---

## 🛠️ Technology Stack

### Core
- **Kotlin** - Primary language
- **Jetpack Compose** - Declarative UI
- **Material Design 3** - UI components
- **Coroutines + Flow** - Async programming

### Architecture
- **Hilt** - Dependency injection
- **MVVM** - Presentation pattern
- **Clean Architecture** - Layer separation

### Networking
- **Retrofit 2** - HTTP client
- **OkHttp** - Network layer with interceptors
- **Moshi** - JSON serialization (with KSP codegen)

### Data Persistence
- **DataStore** - Key-value preferences (2 instancias: app + theme)

### Navigation
- **Navigation Compose** - Type-safe navigation

### Other Libraries
- **Timber** - Logging (prefijo "CPT-" en tags)
- **Coil** - Image loading
- **Accompanist** - System UI Controller
- **SplashScreen API** - Android 12+ splash

---

## 🔌 API Configuration

### Base URLs (BuildConfig)
- **Debug**: `http://10.0.2.2:8080/api/` (emulador → localhost)
- **Release**: `https://logiciel.cptoficina.com.ar:8123/api/`

### Authentication Headers
Todas las requests incluyen:
```
LSIPass: CPT!
LSIToken: LSIToken1789
```

Agregados automáticamente por `AuthInterceptor`.

### Timeout
- Connect: 30s
- Read: 30s
- Write: 30s

### SSL Workaround
El servidor prod tiene certificado mal configurado. `NetworkModule` incluye `TrustManager` que acepta cualquier certificado (solo para prod interno).

---

## 📱 Features & Screens

### Authentication Flow
1. **SplashScreen** → Auto-login check → Login o Home
2. **LoginScreen** → Valida credenciales → Guarda en DataStore → Home
3. **HomeScreen** → Drawer navigation + contenido principal

### Modules Implemented

| Módulo | Descripción | API Base |
|--------|-------------|----------|
| **Tablero** | Dashboard con widgets: Ventas Web ML/Empresas, Venta por Zona, Deuda Clientes/Proveedores, Resmas | `/tableroPrincipal/*` |
| **Ventas** | Listado de ventas con filtros y agrupación | `/ventas` |
| **Pedidos** | Panel de pedidos con filtros avanzados | `/pedidosPanel` |
| **Clientes** | Listado de clientes | `/clientes` |

### Navigation Structure
```kotlin
// NavGraph.kt - Top level navigation
Splash -> Login -> Home

// HomeScreen - Drawer navigation
- Tablero (Dashboard)
- Ventas
- Pedidos  
- Clientes
- Perfil (configuración)
```

---

## 💾 DataStore Keys

### User Preferences (`cpt_preferences`)
```kotlin
KEY_USER_TOKEN       // Auth token
KEY_USER_ID          // User ID
KEY_USER_EMAIL       // Email
KEY_USER_NAME        // Display name
KEY_IS_LOGGED_IN     // Boolean
```

### Tablero State (`theme_preferences` para theme, `cpt_preferences` para fechas)
```kotlin
// Fechas (epoch day)
KEY_VENTAS_WEB_FECHA_DESDE/HASTA
KEY_VENTAS_FECHA_DESDE/HASTA
KEY_VENTAS_RUBRO_FECHA_DESDE/HASTA

// Expansion state (boolean)
KEY_VENTAS_WEB_EXPANDED
KEY_VENTAS_ZONA_EXPANDED
KEY_DEUDA_CLIENTES_EXPANDED
KEY_VENTAS_RUBRO_EXPANDED
KEY_DEUDA_PROVEEDORES_EXPANDED
KEY_RESMAS_EXPANDED
```

---

## 🎨 Theme & Design

### Material Design 3
- **Dynamic colors**: Disabled (consistent branding)
- **Dark mode**: Full support (LIGHT/DARK/SYSTEM)
- **Status bar**: Primary color, icons adaptativos

### Brand Colors
```kotlin
Primary:    #1976D2 (Blue)
Secondary:  #424242 (Dark Gray)
Accent:     #2196F3 (Light Blue)
```

### Color Schemes
Ver `ui/theme/Color.kt` y `ui/theme/Theme.kt` para esquemas completos light/dark.

---

## 🔧 Development Setup

### Prerequisites
- Android Studio 2024.2.1+
- JDK 17 (embedded con Android Studio)
- Android SDK Platform 35

### Running
```bash
# Start API (from cpt_api directory)
npm run dev

# Build debug
./gradlew assembleDebug

# Build release AAB
./gradlew bundleRelease

# Install debug
./gradlew installDebug
```

### Scripts
- `upload_playstore.sh` - Build AAB + upload via Fastlane

### Fastlane
```bash
fastlane deploy_internal  # Upload to Internal Testing
```

---

## 📋 Important Implementation Notes

### 1. API Response Handling
El backend devuelve respuestas en formato:
```json
{
  "status": "OK" | "ERROR",
  "errors": [],
  "data": { ... },
  "mode": "P"
}
```

Los repositorios usan `NetworkResult<T>` wrapper:
```kotlin
when (result) {
    is NetworkResult.Success -> { /* usar result.data */ }
    is NetworkResult.Error -> { /* manejar error */ }
    is NetworkResult.Loading -> { /* loading state */ }
}
```

### 2. Nullable Fields in Models
**CRITICAL**: Todos los campos numéricos en modelos de tablero SON NULLABLE porque el backend devuelve `null` cuando no hay datos.

Ejemplo correcto:
```kotlin
@JsonClass(generateAdapter = true)
data class VentaPorZona(
    @Json(name = "netoTotalFacturado") val netoTotalFacturado: Double?,  // NULLABLE
    @Json(name = "ticketPromedio") val ticketPromedio: Double?,          // NULLABLE
    // ...
)
```

Uso en UI:
```kotlin
Text(formatearMoneda(zona.netoTotalFacturado ?: 0.0))
```

### 3. Date Handling
- UI usa `java.time.LocalDate`
- API espera formato ISO: `YYYY-MM-DD`
- DataStore guarda como epoch day (Long)

### 4. Widget State Persistence
Los widgets del tablero persisten:
- Fechas seleccionadas (por widget)
- Estado expandido/colapsado

Implementado en `TableroViewModel` usando DataStore.

### 5. Logging
Usar `Timber` con tag prefix "CPT-":
```kotlin
Timber.d("Message")  // Logcat: D/CPT-Tag: Message
```

HTTP logging nivel BASIC (solo request/line, no body completo).

---

## 🧩 Key Components

### Widgets (presentation/tablero/components/)
- `WidgetCard` - Card base con expand/collapse
- `VentasWebWidget` - Ventas ML + Empresas con tabs
- `VentasPorZonaWidget` - Gráfico pie + tabla
- `VentasPorRubroWidget` - Ventas completas + ML
- `DeudaClientesWidget` - Pie chart + tabla
- `DeudaProveedoresWidget` - Lista simple
- `ResmasWidget` - Stock de resmas

### Charts
- `SimpleBarChart` - Barras horizontales
- `SimplePieChart` - Gráfico donut con leyenda

### Common Components
- `DateFilterSheet` - BottomSheet para selección de fechas
- `StatRow` - Fila de estadística (label + valor)

---

## ⚠️ Common Issues & Solutions

### Build Errors
```bash
# Limpiar y rebuild
./gradlew clean
./gradlew build

# Si persiste: Invalidate Caches en Android Studio
```

### API Connection Issues
- Verificar API corriendo: `curl http://localhost:8080/api`
- Emulador usa `10.0.2.2`, NO `localhost`
- Dispositivo físico: usar IP de la máquina

### Moshi Parsing Errors
Si hay error `Expected a double but was NULL`:
1. Hacer el campo nullable en el data class: `Double?`
2. Actualizar uso en UI: `valor ?: 0.0`
3. Rebuild

### Version Code Conflict
Si Play Store rechaza por version code duplicado:
```kotlin
// app/build.gradle.kts
defaultConfig {
    versionCode = 22  // Incrementar
}
```

---

## 🚀 Release Process

1. **Update versionCode** en `app/build.gradle.kts`
2. **Test** build local: `./gradlew bundleRelease`
3. **Commit** cambios
4. **Run** `./upload_playstore.sh`
5. **Verify** en Play Console → Internal Testing
6. **Promote** a Production cuando esté listo

---

## 📚 Related Documentation

- `README.md` - Quick start y overview
- `DEVELOPMENT.md` - Guía de desarrollo detallada
- `SETUP_INSTRUCTIONS.md` - Troubleshooting
- `SPRINT_1_SUMMARY.md`, `SPRINT_2_IMPLEMENTATION.md` - Historial

---

## 🔗 Links

- **GitHub**: https://github.com/LeoLobLogiciel/CPT_OL_FE_Android
- **Play Console**: https://play.google.com/console
- **API Project**: `../cpt_api`
- **Web App**: `../cpt_online`
- **iOS App**: `../cpt_online_iOS`

---

## 👤 Team

- **Android Development**: Leo Lob
- **Backend/API**: CPT Team
- **UX Reference**: Quasar Web App

---

**Last Updated**: 2026-02-06
**Version**: 1.0.0
**Status**: Sprint 2 - Tablero Dashboard COMPLETED
