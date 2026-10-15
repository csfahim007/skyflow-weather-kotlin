package com.example.weather_app.data.mapper

import com.example.weather_app.data.dto.CitySearchResultDto
import com.example.weather_app.data.dto.WeatherResponseDto
import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.CurrentWeather
import com.example.weather_app.domain.model.DailyForecast
import com.example.weather_app.domain.model.HourlyForecast
import com.example.weather_app.domain.model.WeatherData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object WeatherMapper {

    fun getWeatherConditionDescription(code: Int): String {
        return when (code) {
            0 -> "Clear Sky"
            1 -> "Mainly Clear"
            2 -> "Partly Cloudy"
            3 -> "Overcast"
            45 -> "Fog"
            48 -> "Depositing Rime Fog"
            51 -> "Light Drizzle"
            53 -> "Moderate Drizzle"
            55 -> "Dense Drizzle"
            56, 57 -> "Freezing Drizzle"
            61 -> "Slight Rain"
            63 -> "Moderate Rain"
            65 -> "Heavy Rain"
            66, 67 -> "Freezing Rain"
            71 -> "Slight Snow"
            73 -> "Moderate Snow"
            75 -> "Heavy Snow"
            77 -> "Snow Grains"
            80 -> "Slight Rain Showers"
            81 -> "Moderate Rain Showers"
            82 -> "Violent Rain Showers"
            85, 86 -> "Snow Showers"
            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with Hail"
            else -> "Unknown"
        }
    }

    fun mapToCityLocation(dto: CitySearchResultDto, favoriteIds: Set<Long> = emptySet()): CityLocation {
        return CityLocation(
            id = dto.id,
            name = dto.name,
            country = dto.country ?: "",
            countryCode = dto.countryCode,
            adminRegion = dto.admin1,
            latitude = dto.latitude,
            longitude = dto.longitude,
            isFavorite = favoriteIds.contains(dto.id)
        )
    }

    fun mapToWeatherData(
        dto: WeatherResponseDto,
        city: CityLocation
    ): WeatherData {
        val currentDto = dto.current
        val currentWeather = CurrentWeather(
            temperatureCelsius = currentDto?.temperature2m ?: 0.0,
            feelsLikeCelsius = currentDto?.apparentTemperature ?: currentDto?.temperature2m ?: 0.0,
            weatherCode = currentDto?.weatherCode ?: 0,
            conditionDescription = getWeatherConditionDescription(currentDto?.weatherCode ?: 0),
            humidityPercentage = currentDto?.relativeHumidity2m?.toInt() ?: 0,
            windSpeedKmh = currentDto?.windSpeed10m ?: 0.0,
            windDirectionDegrees = currentDto?.windDirection10m?.toInt() ?: 0,
            uvIndex = currentDto?.uvIndex ?: 0.0,
            pressureHpa = currentDto?.surfacePressure ?: 1013.25,
            precipitationMm = currentDto?.precipitation ?: 0.0,
            isDaytime = (currentDto?.isDay ?: 1) == 1,
            timeFormatted = formatIsoTime(currentDto?.time)
        )

        val hourlyList = mutableListOf<HourlyForecast>()
        dto.hourly?.let { hourly ->
            val times = hourly.time ?: emptyList()
            val temps = hourly.temperature2m ?: emptyList()
            val codes = hourly.weatherCode ?: emptyList()
            val probs = hourly.precipitationProbability ?: emptyList()
            val isDays = hourly.isDay ?: emptyList()

            // Filter starting from current hour up to 24 hours
            val count = minOf(times.size, temps.size, codes.size, 24)
            for (i in 0 until count) {
                hourlyList.add(
                    HourlyForecast(
                        timeIso = times[i],
                        timeFormatted = formatHourlyTime(times[i]),
                        temperatureCelsius = temps[i],
                        weatherCode = codes[i],
                        precipitationProbabilityPercentage = if (i < probs.size) probs[i] else 0,
                        isDaytime = if (i < isDays.size) isDays[i] == 1 else true
                    )
                )
            }
        }

        val dailyList = mutableListOf<DailyForecast>()
        dto.daily?.let { daily ->
            val times = daily.time ?: emptyList()
            val maxTemps = daily.temperature2mMax ?: emptyList()
            val minTemps = daily.temperature2mMin ?: emptyList()
            val codes = daily.weatherCode ?: emptyList()
            val probs = daily.precipitationProbabilityMax ?: emptyList()
            val windSpeeds = daily.windSpeed10mMax ?: emptyList()
            val uvs = daily.uvIndexMax ?: emptyList()

            val count = minOf(times.size, maxTemps.size, minTemps.size, codes.size)
            for (i in 0 until count) {
                dailyList.add(
                    DailyForecast(
                        dateIso = times[i],
                        dayName = formatDayName(times[i]),
                        maxTempCelsius = maxTemps[i],
                        minTempCelsius = minTemps[i],
                        weatherCode = codes[i],
                        conditionDescription = getWeatherConditionDescription(codes[i]),
                        precipitationProbabilityMax = if (i < probs.size) probs[i] else 0,
                        maxWindSpeedKmh = if (i < windSpeeds.size) windSpeeds[i] else 0.0,
                        uvIndexMax = if (i < uvs.size) uvs[i] else 0.0
                    )
                )
            }
        }

        val nowFormatted = try {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM d, h:mm a", Locale.getDefault()))
        } catch (e: Exception) {
            "Just now"
        }

        return WeatherData(
            city = city,
            current = currentWeather,
            hourly = hourlyList,
            daily = dailyList,
            lastUpdatedFormatted = nowFormatted
        )
    }

    private fun formatIsoTime(isoString: String?): String {
        if (isoString.isNullOrEmpty()) return "Now"
        return try {
            val dateTime = LocalDateTime.parse(isoString)
            dateTime.format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()))
        } catch (e: Exception) {
            "Now"
        }
    }

    private fun formatHourlyTime(isoString: String): String {
        return try {
            val dateTime = LocalDateTime.parse(isoString)
            dateTime.format(DateTimeFormatter.ofPattern("ha", Locale.getDefault())).lowercase()
        } catch (e: Exception) {
            isoString
        }
    }

    private fun formatDayName(isoDate: String): String {
        return try {
            val date = LocalDate.parse(isoDate)
            val today = LocalDate.now()
            if (date == today) {
                "Today"
            } else if (date == today.plusDays(1)) {
                "Tomorrow"
            } else {
                date.format(DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault()))
            }
        } catch (e: Exception) {
            isoDate
        }
    }
}
