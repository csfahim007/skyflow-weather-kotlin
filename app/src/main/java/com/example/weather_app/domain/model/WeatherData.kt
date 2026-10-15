package com.example.weather_app.domain.model

data class WeatherData(
    val city: CityLocation,
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val lastUpdatedFormatted: String
)
