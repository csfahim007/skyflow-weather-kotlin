package com.example.weather_app.domain.model

import kotlin.math.roundToInt

data class DailyForecast(
    val dateIso: String,
    val dayName: String,
    val maxTempCelsius: Double,
    val minTempCelsius: Double,
    val weatherCode: Int,
    val conditionDescription: String,
    val precipitationProbabilityMax: Int,
    val maxWindSpeedKmh: Double,
    val uvIndexMax: Double
) {
    fun maxTemperature(unit: TemperatureUnit): Int {
        val valInUnit = when (unit) {
            TemperatureUnit.CELSIUS -> maxTempCelsius
            TemperatureUnit.FAHRENHEIT -> (maxTempCelsius * 9 / 5) + 32
        }
        return valInUnit.roundToInt()
    }

    fun minTemperature(unit: TemperatureUnit): Int {
        val valInUnit = when (unit) {
            TemperatureUnit.CELSIUS -> minTempCelsius
            TemperatureUnit.FAHRENHEIT -> (minTempCelsius * 9 / 5) + 32
        }
        return valInUnit.roundToInt()
    }
}
