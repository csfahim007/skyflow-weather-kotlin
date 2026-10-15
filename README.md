# 🌤️ SkyFlow Weather

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/DI-Hilt-008080?style=for-the-badge&logo=google&logoColor=white)](https://dagger.dev/hilt/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

**SkyFlow Weather** is a modern, responsive, and feature-packed Android weather application built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and **Clean Architecture**. It provides real-time weather updates, detailed hourly forecasts, a 7-day outlook, dynamic city search, unit conversion (°C/°F), and offline fallback support—powered by the open-source [Open-Meteo API](https://open-meteo.com/).

---

## ✨ Features

- **📍 Real-Time Weather Data**: Instant temperature, "feels like" reading, humidity, wind speed/direction, surface pressure, UV index, and precipitation.
- **⏱️ Hourly Forecast**: Interactive 24-hour horizontal scroll bar showing hourly temperature trends, condition icons, and precipitation probabilities.
- **📅 7-Day Outlook**: Daily forecasts with high/low temperatures, weather descriptions, max UV index, and rain chances.
- **🔍 Dynamic City Search**: Fast geocoding search powered by Open-Meteo Geocoding API to discover and select cities worldwide.
- **⭐ Saved Favorite Cities**: Quick-select chip row for saved cities with persistent local storage.
- **🔄 Unit Switching**: Seamless toggle between **Metric (°C, km/h)** and **Imperial (°F, mph)** units with instantaneous UI re-calculation.
- **📶 Offline Cache & Resilience**: Graceful error handling and offline cached weather data generation to keep the UI functional without an internet connection.
- **🎨 Modern Material 3 UI**: Dynamic color themes, smooth animations, hero weather cards, and clean typography designed for Android devices.

---

## 🏗️ Architecture & Key Technologies

SkyFlow Weather strictly adheres to **Android Clean Architecture** guidelines paired with the **MVVM (Model-View-ViewModel)** architectural pattern and **Unidirectional Data Flow (UDF)**.

```
├── app
│   ├── data               # API Services, DTOs, Mappers, Repositories & Local Storage
│   │   ├── api            # Retrofit interfaces (Open-Meteo API & Geocoding)
│   │   ├── dto            # Data Transfer Objects
│   │   ├── local          # User Preferences Repository (DataStore / SharedPreferences)
│   │   ├── mapper         # Data Mappers converting DTOs -> Domain Models
│   │   └── repository     # WeatherRepository implementation
│   ├── di                 # Dagger Hilt Modules (NetworkModule, RepositoryModule)
│   ├── domain             # Pure Kotlin Domain Layer
│   │   ├── model          # Core entities (WeatherData, CurrentWeather, CityLocation, etc.)
│   │   └── repository     # WeatherRepository interface contract
│   └── ui                 # UI Layer (Jetpack Compose)
│       ├── main           # Screen composables, ViewModels, UI State definitions
│       │   └── components # Reusable UI components (Hero Card, Forecast Lists, Search)
│       └── theme          # Material 3 Color scheme, Typography, and Themes
```

### Tech Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/)
- **Architecture**: Clean Architecture + MVVM + Unidirectional Data Flow (UDF)
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) (Dagger)
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) + [OkHttp 3](https://square.github.io/okhttp/) + [Gson](https://github.com/google/gson)
- **Async & Reactive**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/)
- **Weather Provider**: [Open-Meteo API](https://open-meteo.com/) *(Free, no API key required)*
- **Testing**: JUnit 4, Kotlinx Coroutines Test, MockK, Turbine

---

## 🛠️ Getting Started

### Prerequisites

- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: Java 11 or 17
- **Min SDK**: Android 8.0 (API Level 26)
- **Target SDK**: Android 15 (API Level 35)

### Build & Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/csfahim007/skyflow-weather-kotlin.git
   cd skyflow-weather-kotlin
   ```

2. **Open in Android Studio**:
   Open Android Studio and select **Open**, then choose the cloned project folder.

3. **Build & Deploy**:
   - Run Gradle sync.
   - Select an emulator or connected physical Android device.
   - Click **Run (`Shift + F10`)**.

> 💡 **Note**: No API keys are required to run this app! Open-Meteo is open and free to use out-of-the-box.

---

## 🧪 Testing

The repository includes unit test coverage for data mappers, repository fallback logic, and ViewModel UI state transformations.

Run unit tests directly via Gradle:
```bash
./gradlew test
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
