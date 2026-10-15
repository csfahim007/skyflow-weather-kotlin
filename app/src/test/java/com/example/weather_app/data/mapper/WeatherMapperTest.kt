package com.example.weather_app.data.mapper

import com.example.weather_app.data.dto.CitySearchResultDto
import com.example.weather_app.data.dto.CurrentDto
import com.example.weather_app.data.dto.DailyDto
import com.example.weather_app.data.dto.HourlyDto
import com.example.weather_app.data.dto.WeatherResponseDto
import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.domain.model.TemperatureUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun `getWeatherConditionDescription returns human readable text for weather codes`() {
        assertEquals("Clear Sky", WeatherMapper.getWeatherConditionDescription(0))
        assertEquals("Mainly Clear", WeatherMapper.getWeatherConditionDescription(1))
        assertEquals("Partly Cloudy", WeatherMapper.getWeatherConditionDescription(2))
        assertEquals("Overcast", WeatherMapper.getWeatherConditionDescription(3))
        assertEquals("Fog", WeatherMapper.getWeatherConditionDescription(45))
        assertEquals("Slight Rain", WeatherMapper.getWeatherConditionDescription(61))
        assertEquals("Heavy Rain", WeatherMapper.getWeatherConditionDescription(65))
        assertEquals("Slight Snow", WeatherMapper.getWeatherConditionDescription(71))
        assertEquals("Thunderstorm", WeatherMapper.getWeatherConditionDescription(95))
        assertEquals("Unknown", WeatherMapper.getWeatherConditionDescription(999))
    }

    @Test
    fun `mapToCityLocation maps DTO correctly and sets favorite status`() {
        val dto = CitySearchResultDto(
            id = 100,
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "United Kingdom",
            countryCode = "GB",
            admin1 = "England"
        )

        val cityNonFav = WeatherMapper.mapToCityLocation(dto, favoriteIds = emptySet())
        assertEquals("London", cityNonFav.name)
        assertEquals("United Kingdom", cityNonFav.country)
        assertEquals("London, England, United Kingdom", cityNonFav.displayName)
        assertFalse(cityNonFav.isFavorite)

        val cityFav = WeatherMapper.mapToCityLocation(dto, favoriteIds = setOf(100L))
        assertTrue(cityFav.isFavorite)
    }

    @Test
    fun `mapToWeatherData converts DTO to domain model correctly`() {
        val city = CityLocation(
            id = 1,
            name = "Tokyo",
            country = "Japan",
            latitude = 35.6762,
            longitude = 139.6503
        )

        val dto = WeatherResponseDto(
            latitude = 35.6762,
            longitude = 139.6503,
            timezone = "Asia/Tokyo",
            current = CurrentDto(
                time = "2025-02-15T12:00",
                temperature2m = 20.0,
                relativeHumidity2m = 50.0,
                apparentTemperature = 19.5,
                isDay = 1,
                precipitation = 0.0,
                weatherCode = 0,
                surfacePressure = 1013.25,
                windSpeed10m = 10.0,
                windDirection10m = 180.0,
                uvIndex = 5.0
            ),
            hourly = HourlyDto(
                time = listOf("2025-02-15T12:00", "2025-02-15T13:00"),
                temperature2m = listOf(20.0, 21.0),
                relativeHumidity2m = listOf(50.0, 48.0),
                precipitationProbability = listOf(0, 10),
                weatherCode = listOf(0, 1),
                isDay = listOf(1, 1)
            ),
            daily = DailyDto(
                time = listOf("2025-02-15", "2025-02-16"),
                weatherCode = listOf(0, 2),
                temperature2mMax = listOf(22.0, 20.0),
                temperature2mMin = listOf(12.0, 10.0),
                uvIndexMax = listOf(6.0, 5.0),
                precipitationProbabilityMax = listOf(0, 20),
                windSpeed10mMax = listOf(12.0, 15.0)
            )
        )

        val weatherData = WeatherMapper.mapToWeatherData(dto, city)

        assertEquals("Tokyo", weatherData.city.name)
        assertEquals(20, weatherData.current.temperature(TemperatureUnit.CELSIUS))
        assertEquals(68, weatherData.current.temperature(TemperatureUnit.FAHRENHEIT))
        assertEquals(10, weatherData.current.windSpeed(SpeedUnit.KMH))
        assertEquals(6, weatherData.current.windSpeed(SpeedUnit.MPH))
        assertEquals(2, weatherData.hourly.size)
        assertEquals(2, weatherData.daily.size)
    }
}
