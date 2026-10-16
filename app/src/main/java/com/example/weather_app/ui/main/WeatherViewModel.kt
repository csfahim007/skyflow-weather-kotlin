package com.example.weather_app.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.api.GeocodingApiService
import com.example.weather_app.data.api.OpenMeteoApiService
import com.example.weather_app.data.local.UserPreferencesRepository
import com.example.weather_app.data.repository.WeatherRepositoryImpl
import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.domain.model.TemperatureUnit
import com.example.weather_app.domain.repository.WeatherRepository
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        observeUserPreferences()
    }

    private fun observeUserPreferences() {
        viewModelScope.launch {
            userPreferencesRepository.temperatureUnit.collectLatest { unit ->
                _uiState.update { it.copy(temperatureUnit = unit) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.speedUnit.collectLatest { unit ->
                _uiState.update { it.copy(speedUnit = unit) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.savedCities.collectLatest { cities ->
                _uiState.update { state ->
                    val currentSelected = state.selectedCity
                    val isFav = cities.any { it.id == currentSelected.id }
                    val updatedSelected = currentSelected.copy(isFavorite = isFav)
                    state.copy(
                        savedCities = cities,
                        selectedCity = updatedSelected
                    )
                }
            }
        }
        viewModelScope.launch {
            var isInitialLoaded = false
            userPreferencesRepository.lastSelectedCity.collectLatest { savedLastCity ->
                if (!isInitialLoaded) {
                    isInitialLoaded = true
                    val targetCity = savedLastCity
                        ?: userPreferencesRepository.savedCities.first().firstOrNull()
                        ?: WeatherUiState.defaultCity
                    selectCity(targetCity)
                }
            }
        }
    }

    fun loadWeatherForCity(city: CityLocation, isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            } else {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, selectedCity = city) }
            }

            weatherRepository.getWeatherData(city).fold(
                onSuccess = { weatherData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            weatherData = weatherData,
                            selectedCity = weatherData.city,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = error.localizedMessage ?: "Failed to connect to weather server. Check network connection."
                        )
                    }
                }
            )
        }
    }

    fun refreshWeather() {
        loadWeatherForCity(_uiState.value.selectedCity, isRefresh = true)
    }

    fun selectCity(city: CityLocation) {
        val isFav = _uiState.value.savedCities.any { it.id == city.id }
        val updatedCity = city.copy(isFavorite = isFav)
        _uiState.update { it.copy(selectedCity = updatedCity) }
        userPreferencesRepository.setLastSelectedCity(updatedCity)
        loadWeatherForCity(updatedCity)
    }

    fun toggleTemperatureUnit() {
        val newUnit = if (_uiState.value.temperatureUnit == TemperatureUnit.CELSIUS) {
            TemperatureUnit.FAHRENHEIT
        } else {
            TemperatureUnit.CELSIUS
        }
        userPreferencesRepository.setTemperatureUnit(newUnit)
    }

    fun toggleSpeedUnit() {
        val newUnit = if (_uiState.value.speedUnit == SpeedUnit.KMH) {
            SpeedUnit.MPH
        } else {
            SpeedUnit.KMH
        }
        userPreferencesRepository.setSpeedUnit(newUnit)
    }

    fun toggleFavorite(city: CityLocation) {
        viewModelScope.launch {
            if (city.isFavorite) {
                weatherRepository.removeCity(city.id)
            } else {
                weatherRepository.saveCity(city)
            }
            if (_uiState.value.selectedCity.id == city.id) {
                val updated = city.copy(isFavorite = !city.isFavorite)
                _uiState.update { it.copy(selectedCity = updated) }
                userPreferencesRepository.setLastSelectedCity(updated)
            }
        }
    }

    fun openSearchDialog() {
        _uiState.update { it.copy(isSearchDialogOpen = true, searchQuery = "", searchResults = emptyList()) }
    }

    fun closeSearchDialog() {
        _uiState.update { it.copy(isSearchDialogOpen = false, searchQuery = "", searchResults = emptyList()) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.trim().length < 2) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(350) // Debounce search
            _uiState.update { it.copy(isSearching = true) }
            weatherRepository.searchCities(query.trim()).fold(
                onSuccess = { results ->
                    _uiState.update { it.copy(searchResults = results, isSearching = false) }
                },
                onFailure = {
                    _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
                }
            )
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val appContext = context.applicationContext
                val gson = GsonBuilder().create()
                val prefsRepo = UserPreferencesRepository(appContext, gson)
                val okHttp = OkHttpClient.Builder().build()

                val openMeteo = Retrofit.Builder()
                    .baseUrl("https://api.open-meteo.com/")
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(OpenMeteoApiService::class.java)

                val geocoding = Retrofit.Builder()
                    .baseUrl("https://geocoding-api.open-meteo.com/")
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(GeocodingApiService::class.java)

                val weatherRepo = WeatherRepositoryImpl(openMeteo, geocoding, prefsRepo)
                return WeatherViewModel(weatherRepo, prefsRepo) as T
            }
        }
    }
}
