package com.example.weather_app.ui.main

import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.domain.model.TemperatureUnit
import com.example.weather_app.domain.model.WeatherData

data class WeatherUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val weatherData: WeatherData? = null,
    val selectedCity: CityLocation = defaultCity,
    val savedCities: List<CityLocation> = emptyList(),
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val speedUnit: SpeedUnit = SpeedUnit.KMH,
    val searchQuery: String = "",
    val searchResults: List<CityLocation> = emptyList(),
    val isSearching: Boolean = false,
    val isSearchDialogOpen: Boolean = false,
    val errorMessage: String? = null
) {
    companion object {
        val defaultCity = CityLocation(
            id = 1001,
            name = "New York",
            country = "United States",
            countryCode = "US",
            adminRegion = "New York",
            latitude = 40.7128,
            longitude = -74.0060,
            isFavorite = true
        )
    }
}
