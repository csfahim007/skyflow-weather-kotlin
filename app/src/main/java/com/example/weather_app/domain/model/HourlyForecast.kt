package com.example.weather_app.domain.model

import kotlin.math.roundToInt

data class HourlyForecast(
    val timeIso: String,
    val timeFormatted: String,
    val temperatureCelsius: Double,
    val weatherCode: Int,
    val precipitationProbabilityPercentage: Int,
    val isDaytime: Boolean
) {
    fun temperature(unit: TemperatureUnit): Int {
        val valInUnit = when (unit) {
            TemperatureUnit.CELSIUS -> temperatureCelsius
            TemperatureUnit.FAHRENHEIT -> (temperatureCelsius * 9 / 5) + 32
        }
        return valInUnit.roundToInt()
    }
}
