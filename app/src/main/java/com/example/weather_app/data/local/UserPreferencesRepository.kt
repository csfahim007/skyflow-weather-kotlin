package com.example.weather_app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.domain.model.TemperatureUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("skyflow_prefs", Context.MODE_PRIVATE)

    private val _temperatureUnit = MutableStateFlow(getSavedTemperatureUnit())
    val temperatureUnit: Flow<TemperatureUnit> = _temperatureUnit.asStateFlow()

    private val _speedUnit = MutableStateFlow(getSavedSpeedUnit())
    val speedUnit: Flow<SpeedUnit> = _speedUnit.asStateFlow()

    private val _savedCities = MutableStateFlow(getSavedCitiesList())
    val savedCities: Flow<List<CityLocation>> = _savedCities.asStateFlow()

    private val _lastSelectedCity = MutableStateFlow(getSavedLastSelectedCity())
    val lastSelectedCity: Flow<CityLocation?> = _lastSelectedCity.asStateFlow()

    private fun getSavedLastSelectedCity(): CityLocation? {
        val json = prefs.getString("last_selected_city", null)
        if (json.isNullOrBlank()) return null
        return try {
            gson.fromJson(json, CityLocation::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun setLastSelectedCity(city: CityLocation) {
        val json = gson.toJson(city)
        prefs.edit().putString("last_selected_city", json).apply()
        _lastSelectedCity.value = city
    }

    private fun getSavedTemperatureUnit(): TemperatureUnit {
        val name = prefs.getString("temp_unit", TemperatureUnit.CELSIUS.name)
        return try {
            TemperatureUnit.valueOf(name ?: TemperatureUnit.CELSIUS.name)
        } catch (e: Exception) {
            TemperatureUnit.CELSIUS
        }
    }

    fun setTemperatureUnit(unit: TemperatureUnit) {
        prefs.edit().putString("temp_unit", unit.name).apply()
        _temperatureUnit.value = unit
    }

    private fun getSavedSpeedUnit(): SpeedUnit {
        val name = prefs.getString("speed_unit", SpeedUnit.KMH.name)
        return try {
            SpeedUnit.valueOf(name ?: SpeedUnit.KMH.name)
        } catch (e: Exception) {
            SpeedUnit.KMH
        }
    }

    fun setSpeedUnit(unit: SpeedUnit) {
        prefs.edit().putString("speed_unit", unit.name).apply()
        _speedUnit.value = unit
    }

    private fun getSavedCitiesList(): List<CityLocation> {
        val json = prefs.getString("saved_cities", null)
        if (json.isNullOrBlank()) return getDefaultCities()
        return try {
            val type = object : TypeToken<List<CityLocation>>() {}.type
            val list: List<CityLocation>? = gson.fromJson(json, type)
            if (list.isNullOrEmpty()) getDefaultCities() else list
        } catch (e: Exception) {
            getDefaultCities()
        }
    }

    private fun saveCitiesList(cities: List<CityLocation>) {
        val json = gson.toJson(cities)
        prefs.edit().putString("saved_cities", json).apply()
        _savedCities.value = cities
    }

    fun addCity(city: CityLocation) {
        val currentList = _savedCities.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == city.id }
        val updatedCity = city.copy(isFavorite = true)
        if (index >= 0) {
            currentList[index] = updatedCity
        } else {
            currentList.add(updatedCity)
        }
        saveCitiesList(currentList)
    }

    fun removeCity(cityId: Long) {
        val currentList = _savedCities.value.filter { it.id != cityId }
        saveCitiesList(currentList)
    }

    private fun getDefaultCities(): List<CityLocation> {
        return listOf(
            CityLocation(
                id = 1001,
                name = "New York",
                country = "United States",
                countryCode = "US",
                adminRegion = "New York",
                latitude = 40.7128,
                longitude = -74.0060,
                isFavorite = true
            ),
            CityLocation(
                id = 1002,
                name = "London",
                country = "United Kingdom",
                countryCode = "GB",
                adminRegion = "England",
                latitude = 51.5074,
                longitude = -0.1278,
                isFavorite = true
            ),
            CityLocation(
                id = 1003,
                name = "Tokyo",
                country = "Japan",
                countryCode = "JP",
                adminRegion = "Tokyo",
                latitude = 35.6762,
                longitude = 139.6503,
                isFavorite = true
            ),
            CityLocation(
                id = 1004,
                name = "Paris",
                country = "France",
                countryCode = "FR",
                adminRegion = "Île-de-France",
                latitude = 48.8566,
                longitude = 2.3522,
                isFavorite = true
            )
        )
    }
}
