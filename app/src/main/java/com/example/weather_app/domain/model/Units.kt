package com.example.weather_app.domain.model

enum class TemperatureUnit(val symbol: String, val label: String) {
    CELSIUS("°C", "Celsius"),
    FAHRENHEIT("°F", "Fahrenheit")
}

enum class SpeedUnit(val symbol: String, val label: String) {
    KMH("km/h", "Kilometers per hour"),
    MPH("mph", "Miles per hour")
}
