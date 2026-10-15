package com.example.weather_app.ui.main

import com.example.weather_app.data.local.UserPreferencesRepository
import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.CurrentWeather
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.domain.model.TemperatureUnit
import com.example.weather_app.domain.model.WeatherData
import com.example.weather_app.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val weatherRepository: WeatherRepository = mockk(relaxed = true)
    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)

    private val tempUnitFlow = MutableStateFlow(TemperatureUnit.CELSIUS)
    private val speedUnitFlow = MutableStateFlow(SpeedUnit.KMH)
    private val savedCitiesFlow = MutableStateFlow(listOf(WeatherUiState.defaultCity))

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userPreferencesRepository.temperatureUnit } returns tempUnitFlow
        every { userPreferencesRepository.speedUnit } returns speedUnitFlow
        every { userPreferencesRepository.savedCities } returns savedCitiesFlow

        val testCity = WeatherUiState.defaultCity
        val mockWeatherData = WeatherData(
            city = testCity,
            current = CurrentWeather(
                temperatureCelsius = 22.0,
                feelsLikeCelsius = 21.0,
                weatherCode = 0,
                conditionDescription = "Clear Sky",
                humidityPercentage = 45,
                windSpeedKmh = 12.0,
                windDirectionDegrees = 180,
                uvIndex = 5.0,
                pressureHpa = 1014.0,
                precipitationMm = 0.0,
                isDaytime = true,
                timeFormatted = "12:00 PM"
            ),
            hourly = emptyList(),
            daily = emptyList(),
            lastUpdatedFormatted = "Just now"
        )

        coEvery { weatherRepository.getWeatherData(any()) } answers {
            val city = firstArg<CityLocation>()
            Result.success(mockWeatherData.copy(city = city))
        }

        viewModel = WeatherViewModel(
            weatherRepository = weatherRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state updates after user preferences load`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.weatherData)
        assertEquals("New York", state.selectedCity.name)
        assertEquals(TemperatureUnit.CELSIUS, state.temperatureUnit)
        assertEquals(SpeedUnit.KMH, state.speedUnit)
    }

    @Test
    fun `toggleTemperatureUnit calls preferences repository`() = runTest {
        viewModel.toggleTemperatureUnit()

        coVerify { userPreferencesRepository.setTemperatureUnit(TemperatureUnit.FAHRENHEIT) }
    }

    @Test
    fun `toggleSpeedUnit calls preferences repository`() = runTest {
        viewModel.toggleSpeedUnit()

        coVerify { userPreferencesRepository.setSpeedUnit(SpeedUnit.MPH) }
    }

    @Test
    fun `selectCity loads weather data for chosen city`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val newCity = CityLocation(
            id = 200,
            name = "Tokyo",
            country = "Japan",
            latitude = 35.6762,
            longitude = 139.6503
        )

        viewModel.selectCity(newCity)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { weatherRepository.getWeatherData(newCity) }
        assertEquals("Tokyo", viewModel.uiState.value.selectedCity.name)
    }

    @Test
    fun `search dialog opens and closes correctly`() = runTest {
        viewModel.openSearchDialog()
        assertTrue(viewModel.uiState.value.isSearchDialogOpen)

        viewModel.closeSearchDialog()
        assertFalse(viewModel.uiState.value.isSearchDialogOpen)
    }
}
