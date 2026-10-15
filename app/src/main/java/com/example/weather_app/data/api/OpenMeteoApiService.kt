package com.example.weather_app.data.api

import com.example.weather_app.data.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApiService {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,surface_pressure,wind_speed_10m,wind_direction_10m,uv_index",
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,precipitation_probability,weather_code,is_day",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max,wind_speed_10m_max",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponseDto
}
