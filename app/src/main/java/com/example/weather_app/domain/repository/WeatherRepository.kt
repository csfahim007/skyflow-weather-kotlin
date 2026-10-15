package com.example.weather_app.domain.repository

import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherData(city: CityLocation): Result<WeatherData>
    suspend fun searchCities(query: String): Result<List<CityLocation>>
    fun getSavedCities(): Flow<List<CityLocation>>
    suspend fun saveCity(city: CityLocation)
    suspend fun removeCity(cityId: Long)
}
