# Sprint 2: Home Screen con Menú y Configuración

## Implementación Completada

Se ha implementado exitosamente el Sprint 2, que incluye la pantalla principal con menú de navegación y configuración, basándose en la aplicación iOS existente.

## Archivos Creados

### 1. Navegación y Menú

**MenuItem.kt**
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/home/MenuItem.kt`
- Sealed class que representa cada opción del menú
- Incluye: PedidosPanel, ClientesPanel, ClientesTablero, ClientesVentas, Utilitarios, Logout
- Cada ítem tiene su ícono, color y sección correspondiente
- Estructura basada en iOS `SideMenuView.swift`

**NavigationDrawerContent.kt**
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/home/components/NavigationDrawerContent.kt`
- Componente del navigation drawer
- Header con avatar placeholder y título "CPT Mobile - Sistema de Gestión"
- Secciones organizadas: Pedidos, Clientes y Ventas, Utilitarios
- Opción de Cerrar Sesión al final
- Soporte para tema claro/oscuro

### 2. ViewModels

**HomeViewModel.kt**
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/home/HomeViewModel.kt`
- Maneja el estado del drawer y la selección de menú
- Obtiene el usuario actual del AuthRepository
- Logs detallados para debugging
- Controla la apertura/cierre del drawer

**ProfileViewModel.kt**
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/profile/ProfileViewModel.kt`
- Maneja el logout del usuario
- Muestra diálogo de confirmación antes de cerrar sesión
- Limpia el DataStore al hacer logout
- Logs detallados del proceso de logout

### 3. Pantallas

**HomeScreen.kt** (Actualizado)
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/home/HomeScreen.kt`
- Pantalla principal con ModalNavigationDrawer
- Top bar con:
  - Botón hamburguesa (izquierda) para abrir el drawer
  - Título de la pantalla actual (centro)
  - Avatar de perfil (derecha) para ir a configuración
- Navegación entre secciones sin cambiar de activity
- Detecta cuando el usuario hace logout y navega a login

**ProfileScreen.kt**
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/profile/ProfileScreen.kt`
- Pantalla de configuración/perfil
- Sección "Mi Cuenta" con:
  - Avatar placeholder
  - Nombre del usuario (de DataStore)
  - Email del usuario
- Sección "Información" con versión de la app
- Sección "Acciones" con botón de Cerrar Sesión
- Diálogo de confirmación antes de logout
- Navega automáticamente a login después del logout

**Pantallas Placeholder:**
- `PedidosScreen.kt` - Pedidos Panel
- `ClientesPanelScreen.kt` - Clientes y Ventas Panel
- `TableroScreen.kt` - Clientes y Ventas Tablero
- `VentasScreen.kt` - Clientes y Ventas Ventas

Todas las pantallas placeholder tienen:
- Diseño centrado con ícono
- Título descriptivo
- Mensaje indicando que se implementarán en futuros sprints
- Soporte para tema claro/oscuro

### 4. Navegación

**Screen.kt** (Actualizado)
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/navigation/Screen.kt`
- Agregadas rutas: Pedidos, ClientesPanel, Tablero, Ventas, Profile
- Nota: Estas rutas se usan internamente en HomeScreen, no requieren composables separados en NavGraph

**NavGraph.kt** (Actualizado)
- Ubicación: `app/src/main/java/ar/com/logiciel/cptmobile/presentation/navigation/NavGraph.kt`
- Documentación agregada explicando que las pantallas internas se manejan dentro de HomeScreen

## Características Implementadas

### Menú de Navegación (Drawer)
- Navigation drawer deslizante desde la izquierda
- Header con avatar y título
- Secciones organizadas como en iOS:
  - Pedidos: Panel
  - Clientes y Ventas: Tablero, Ventas
  - Utilitarios: Configuración
  - Cerrar Sesión (separado)
- Indicador visual del ítem seleccionado (fondo de color + check)
- Cierre automático al seleccionar una opción

### Top Bar
- Botón hamburguesa para abrir el drawer
- Título centrado que cambia según la pantalla actual
- Avatar de perfil en la derecha (hace clic para ir a configuración)
- Diseño Material 3

### Gestión de Usuario
- Obtiene y muestra el nombre del usuario desde DataStore
- Muestra email si está disponible
- Avatar placeholder (se puede extender con fotos de perfil)

### Logout
- Opción en el drawer que abre la pantalla de perfil
- Botón destacado en rojo en ProfileScreen
- Diálogo de confirmación: "¿Estás seguro que deseas cerrar sesión?"
- Al confirmar:
  - Limpia todo el DataStore
  - Logs detallados del proceso
  - Navega automáticamente a LoginScreen
  - Limpia el backstack para evitar volver atrás

### Logging
- Logs detallados en HomeViewModel:
  - Inicialización
  - Apertura/cierre del drawer
  - Selección de ítems del menú
  - Carga del usuario actual
- Logs detallados en ProfileViewModel:
  - Inicialización
  - Mostrar/ocultar diálogo de logout
  - Proceso completo de logout

### Dark Mode Support
- Todos los componentes usan colores del MaterialTheme
- Soporte automático para tema claro/oscuro del sistema
- Colores personalizados de los ítems del menú se mantienen en ambos temas

## Estructura del Código

```
presentation/
├── home/
│   ├── HomeScreen.kt (Pantalla principal con drawer)
│   ├── HomeViewModel.kt (ViewModel del home)
│   ├── MenuItem.kt (Sealed class para ítems del menú)
│   └── components/
│       └── NavigationDrawerContent.kt (Contenido del drawer)
├── profile/
│   ├── ProfileScreen.kt (Pantalla de configuración)
│   └── ProfileViewModel.kt (ViewModel del perfil)
├── pedidos/
│   └── PedidosScreen.kt (Placeholder)
├── clientes/
│   └── ClientesPanelScreen.kt (Placeholder)
├── tablero/
│   └── TableroScreen.kt (Placeholder)
├── ventas/
│   └── VentasScreen.kt (Placeholder)
└── navigation/
    ├── Screen.kt (Rutas actualizadas)
    └── NavGraph.kt (NavHost con comentarios)
```

## Dependencias Utilizadas

Todas las dependencias ya están en el proyecto:
- Jetpack Compose Material 3
- Hilt para inyección de dependencias
- Timber para logging
- DataStore para persistencia
- Navigation Compose

## Cómo Probar

1. **Compilar el proyecto:**
   ```bash
   ./gradlew assembleDebug
   ```

2. **Ejecutar en emulador o dispositivo:**
   ```bash
   ./gradlew installDebug
   ```

3. **Flujo de prueba:**
   - Iniciar la app (verás el splash)
   - Hacer login con credenciales válidas
   - La app navega automáticamente a HomeScreen
   - Verás el Tablero (pantalla por defecto)
   - Hacer clic en el botón hamburguesa (izquierda) para abrir el drawer
   - Navegar entre las diferentes opciones del menú
   - Hacer clic en el avatar (derecha) para ir directamente a Configuración
   - En Configuración, hacer clic en "Cerrar Sesión"
   - Confirmar el logout
   - Verificar que se navega automáticamente a LoginScreen

4. **Verificar logs:**
   ```bash
   adb logcat -s "CPT_MOBILE"
   ```

   Los logs mostrarán:
   - Inicialización de ViewModels
   - Apertura/cierre del drawer
   - Selección de ítems del menú
   - Proceso de logout completo
   - Navegación entre pantallas

## Diferencias con iOS

1. **Navegación:** En Android usamos ModalNavigationDrawer en lugar de ZStack con offset
2. **Top Bar:** Usamos TopAppBar de Material 3 en lugar de HStack personalizado
3. **Iconos:** Usamos Material Icons en lugar de SF Symbols
4. **Tema:** Material Theme en lugar de SwiftUI colors
5. **Avatar:** Por ahora es placeholder (se puede extender con Coil para cargar imágenes)

## Próximos Pasos (Sprint 3+)

1. Implementar PedidosScreen con funcionalidad real
2. Implementar TableroScreen con dashboard
3. Implementar VentasScreen con listado
4. Agregar soporte para cargar foto de perfil
5. Implementar ClientesPanelScreen
6. Agregar más opciones de configuración en ProfileScreen

## Notas Técnicas

- **Single Activity Architecture:** Todo se maneja dentro de MainActivity
- **MVVM Pattern:** Cada pantalla tiene su ViewModel con Hilt
- **State Management:** StateFlow para estados reactivos
- **Material Design 3:** Todos los componentes siguen MD3 guidelines
- **Accesibilidad:** Content descriptions en todos los íconos
- **Performance:** LazyColumn en drawer para mejor performance con muchos ítems

## Logs de Referencia

### HomeViewModel:
- `🏠 HomeViewModel initialized`
- `👤 Current user loaded: [nombre]`
- `📂 Opening navigation drawer`
- `📂 Closing navigation drawer`
- `📍 Menu item selected: [título] ([ruta])`

### ProfileViewModel:
- `👤 ProfileViewModel initialized`
- `👤 Current user loaded: [nombre]`
- `🚪 Showing logout confirmation dialog`
- `🚪 Logging out user...`
- `✅ Logout successful`

### HomeScreen:
- `🍔 Hamburger menu clicked`
- `👤 Profile icon clicked`
- `🚪 Logout option selected from drawer`
- `🔄 User logged out from HomeScreen, navigating to login`
