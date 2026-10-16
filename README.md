# 🌤️ SkyFlow Weather


[![Release v1.0.0](https://img.shields.io/badge/Release-v1.0.0-blue?style=for-the-badge&logo=github)](https://github.com/csfahim007/skyflow-weather-kotlin/releases/tag/v1.0.0)
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/DI-Hilt-008080?style=for-the-badge&logo=google&logoColor=white)](https://dagger.dev/hilt/)

📦 **Latest Release:** [Download SkyFlow Weather v1.0.0](https://github.com/csfahim007/skyflow-weather-kotlin/releases/tag/v1.0.0)

---

**SkyFlow Weather** is a modern, responsive, and feature-packed Android weather application built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and **Clean Architecture**. It provides real-time weather updates, detailed hourly forecasts, a 7-day outlook, dynamic city search, unit conversion (°C/°F), and offline fallback support—powered by the open-source [Open-Meteo API](https://open-meteo.com/).

---

## 📲 Downloads & Release

Get the latest pre-compiled build directly from the GitHub Releases page:

👉 **[SkyFlow Weather Release v1.0.0](https://github.com/csfahim007/skyflow-weather-kotlin/releases/tag/v1.0.0)**

Download the APK file directly to your Android device to install and test the app.

---

## ✨ Features

- **🌦️ Real-Time Weather Data**: Instant temperature, "feels like" reading, humidity, wind speed/direction, surface pressure, UV index, and precipitation.
- **⏱️ Hourly Forecast**: Interactive 24-hour horizontal scroll showing hourly temperature trends, condition icons, and rain chances.
- **📅 7-Day Outlook**: Daily forecasts with max/min temperatures, weather condition descriptions, UV index max, and precipitation probability.
- **🔍 Dynamic City Search**: Fast geocoding search powered by Open-Meteo Geocoding API to discover and select cities worldwide.
- **⭐ Saved Favorite Cities**: Quick-select chip row for saved cities with persistent local storage.
- **🔄 Unit Switching**: Seamless toggle between **Metric (°C, km/h)** and **Imperial (°F, mph)** units with instantaneous UI re-calculation.
- **📶 Offline Cache & Resilience**: Graceful error handling and offline cached weather data generation to keep the UI functional without internet connectivity.
- **🎨 Modern Material 3 UI**: Dynamic color themes, smooth animations, hero weather cards, and clean typography.

---

## 🛠️ Tech Stack

| Category            | Technology / Library                                       |
| ------------------- | ---------------------------------------------------------- |
| **Language**        | Kotlin 100%                                                |
| **UI Framework**    | Jetpack Compose + Material Design 3                        |
| **Architecture**    | Clean Architecture + MVVM + Unidirectional Data Flow (UDF) |
| **DI**              | Dagger Hilt                                                |
| **Async / Reactive**| Kotlin Coroutines & StateFlow                              |
| **Networking**      | Retrofit 2 + OkHttp 3 + Gson                               |
| **Image Loading**   | Coil Compose                                               |
| **Data Provider**   | Open-Meteo Weather & Geocoding API (Free, no key required) |
| **Testing**         | JUnit 4, Coroutines Test, MockK, Turbine                   |

---

## 🏗️ Architecture & Project Structure

```text
weatherapp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/weather_app/
│   │   │   │   ├── data/          # API services, DTOs, Mappers, Repositories
│   │   │   │   ├── di/            # Hilt Dependency Injection Modules
│   │   │   │   ├── domain/        # Entities & Repository Contract
│   │   │   │   └── ui/            # Jetpack Compose UI, ViewModels, Themes
│   │   │   ├── res/               # Vector Drawables, Strings, XML Configs
│   │   │   └── AndroidManifest.xml
│   │   └── test/                  # Unit Tests for ViewModel, Repository, Mappers
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug or newer
- JDK 11 or 17
- Android SDK 35 (Min SDK 26)
- Android Emulator or physical device (Android 8.0+)

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/csfahim007/skyflow-weather-kotlin.git
   cd skyflow-weather-kotlin
   ```

2. Open the project in Android Studio.

3. Run Gradle sync.

4. Select a device and click **Run (`Shift + F10`)**.

---

## 🧪 Testing

Run unit tests via Gradle:
```bash
./gradlew test
```

---

## 👨‍💻 Author

**Ahmed Fahim Kabir**
- GitHub: [@csfahim007](https://github.com/csfahim007)
