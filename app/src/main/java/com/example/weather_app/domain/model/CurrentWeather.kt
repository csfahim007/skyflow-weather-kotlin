package com.example.weather_app.domain.model

import kotlin.math.roundToInt

data class CurrentWeather(
    val temperatureCelsius: Double,
    val feelsLikeCelsius: Double,
    val weatherCode: Int,
    val conditionDescription: String,
    val humidityPercentage: Int,
    val windSpeedKmh: Double,
    val windDirectionDegrees: Int,
    val uvIndex: Double,
    val pressureHpa: Double,
    val precipitationMm: Double,
    val isDaytime: Boolean,
    val timeFormatted: String
) {
    fun temperature(unit: TemperatureUnit): Int {
        val valInUnit = when (unit) {
            TemperatureUnit.CELSIUS -> temperatureCelsius
            TemperatureUnit.FAHRENHEIT -> (temperatureCelsius * 9 / 5) + 32
        }
        return valInUnit.roundToInt()
    }

    fun feelsLike(unit: TemperatureUnit): Int {
        val valInUnit = when (unit) {
            TemperatureUnit.CELSIUS -> feelsLikeCelsius
            TemperatureUnit.FAHRENHEIT -> (feelsLikeCelsius * 9 / 5) + 32
        }
        return valInUnit.roundToInt()
    }

    fun windSpeed(unit: SpeedUnit): Int {
        val speed = when (unit) {
            SpeedUnit.KMH -> windSpeedKmh
            SpeedUnit.MPH -> windSpeedKmh * 0.621371
        }
        return speed.roundToInt()
    }
}
