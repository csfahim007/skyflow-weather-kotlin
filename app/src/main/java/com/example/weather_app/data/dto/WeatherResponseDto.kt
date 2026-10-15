package com.example.weather_app.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("current") val current: CurrentDto?,
    @SerializedName("hourly") val hourly: HourlyDto?,
    @SerializedName("daily") val daily: DailyDto?
)

data class CurrentDto(
    @SerializedName("time") val time: String?,
    @SerializedName("temperature_2m") val temperature2m: Double?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: Double?,
    @SerializedName("apparent_temperature") val apparentTemperature: Double?,
    @SerializedName("is_day") val isDay: Int?,
    @SerializedName("precipitation") val precipitation: Double?,
    @SerializedName("weather_code") val weatherCode: Int?,
    @SerializedName("surface_pressure") val surfacePressure: Double?,
    @SerializedName("wind_speed_10m") val windSpeed10m: Double?,
    @SerializedName("wind_direction_10m") val windDirection10m: Double?,
    @SerializedName("uv_index") val uvIndex: Double?
)

data class HourlyDto(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("temperature_2m") val temperature2m: List<Double>?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: List<Double>?,
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int>?,
    @SerializedName("weather_code") val weatherCode: List<Int>?,
    @SerializedName("is_day") val isDay: List<Int>?
)

data class DailyDto(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("weather_code") val weatherCode: List<Int>?,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>?,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>?,
    @SerializedName("uv_index_max") val uvIndexMax: List<Double>?,
    @SerializedName("precipitation_probability_max") val precipitationProbabilityMax: List<Int>?,
    @SerializedName("wind_speed_10m_max") val windSpeed10mMax: List<Double>?
)
