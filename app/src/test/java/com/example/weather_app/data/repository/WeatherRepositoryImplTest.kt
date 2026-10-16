package com.example.weather_app.data.repository

import com.example.weather_app.data.api.GeocodingApiService
import com.example.weather_app.data.api.OpenMeteoApiService
import com.example.weather_app.data.dto.CitySearchResponseDto
import com.example.weather_app.data.dto.CitySearchResultDto
import com.example.weather_app.data.dto.CurrentDto
import com.example.weather_app.data.dto.WeatherResponseDto
import com.example.weather_app.data.local.UserPreferencesRepository
import com.example.weather_app.domain.model.CityLocation
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private val openMeteoApiService: OpenMeteoApiService = mockk()
    private val geocodingApiService: GeocodingApiService = mockk()
    private val userPreferencesRepository: UserPreferencesRepository = mockk()

    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(
            openMeteoApiService = openMeteoApiService,
            geocodingApiService = geocodingApiService,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @Test
    fun `getWeatherData returns success when API call succeeds`() = runTest {
        val city = CityLocation(1, "Paris", "France", "FR", "Île-de-France", 48.8566, 2.3522)
        val mockResponse = WeatherResponseDto(
            latitude = 48.8566,
            longitude = 2.3522,
            timezone = "Europe/Paris",
            current = CurrentDto(
                time = "2025-02-15T12:00",
                temperature2m = 18.0,
                relativeHumidity2m = 60.0,
                apparentTemperature = 17.5,
                isDay = 1,
                precipitation = 0.0,
                weatherCode = 0,
                surfacePressure = 1015.0,
                windSpeed10m = 8.0,
                windDirection10m = 120.0,
                uvIndex = 4.0
            ),
            hourly = null,
            daily = null
        )

        coEvery { openMeteoApiService.getWeatherForecast(latitude = city.latitude, longitude = city.longitude) } returns mockResponse

        val result = repository.getWeatherData(city)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertEquals("Paris", data?.city?.name)
        assertEquals(18.0, data?.current?.temperatureCelsius ?: 0.0, 0.1)
    }

    @Test
    fun `getWeatherData returns offline fallback data when API throws exception`() = runTest {
        val city = CityLocation(1, "Paris", "France", "FR", "Île-de-France", 48.8566, 2.3522)

        coEvery { openMeteoApiService.getWeatherForecast(any(), any()) } throws RuntimeException("Network timeout")

        val result = repository.getWeatherData(city)

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertEquals("Paris", data?.city?.name)
        assertTrue(data?.current?.conditionDescription?.contains("Offline") == true)
    }

    @Test
    fun `searchCities returns list of matching locations`() = runTest {
        val mockSearchResults = listOf(
            CitySearchResultDto(
                id = 1,
                name = "New York",
                latitude = 40.7128,
                longitude = -74.0060,
                country = "United States",
                countryCode = "US",
                admin1 = "New York"
            )
        )

        coEvery { geocodingApiService.searchCities(name = "New") } returns CitySearchResponseDto(mockSearchResults)
        every { userPreferencesRepository.savedCities } returns MutableStateFlow(emptyList())

        val result = repository.searchCities("New")

        assertTrue(result.isSuccess)
        val cities = result.getOrNull() ?: emptyList()
        assertEquals(1, cities.size)
        assertEquals("New York", cities.first().name)
    }
}
