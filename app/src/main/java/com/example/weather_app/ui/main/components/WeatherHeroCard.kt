package com.example.weather_app.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.domain.model.CityLocation
import com.example.weather_app.domain.model.CurrentWeather
import com.example.weather_app.domain.model.DailyForecast
import com.example.weather_app.domain.model.TemperatureUnit
import com.example.weather_app.ui.theme.SkyGlassCardBackground
import com.example.weather_app.ui.theme.SkyGlassCardBorder
import com.example.weather_app.ui.theme.SunGold

@Composable
fun WeatherHeroCard(
    city: CityLocation,
    current: CurrentWeather,
    todayForecast: DailyForecast?,
    temperatureUnit: TemperatureUnit,
    onToggleFavorite: (CityLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val tempVal = current.temperature(temperatureUnit)
    val feelsLikeVal = current.feelsLike(temperatureUnit)
    val maxTemp = todayForecast?.maxTemperature(temperatureUnit) ?: tempVal
    val minTemp = todayForecast?.minTemperature(temperatureUnit) ?: tempVal

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SkyGlassCardBackground,
                        Color(0x11FFFFFF)
                    )
                )
            )
            .border(1.dp, SkyGlassCardBorder, RoundedCornerShape(32.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Location Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "Location icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Column {
                        Text(
                            text = city.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (!city.adminRegion.isNullOrBlank() && city.adminRegion != city.name) {
                                "${city.adminRegion}, ${city.country}"
                            } else {
                                city.country
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(
                    onClick = { onToggleFavorite(city) }
                ) {
                    Icon(
                        imageVector = if (city.isFavorite) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                        contentDescription = "Toggle favorite",
                        tint = if (city.isFavorite) SunGold else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Weather Icon & Temperature Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeatherIcon(
                    weatherCode = current.weatherCode,
                    isDaytime = current.isDaytime,
                    modifier = Modifier.size(96.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Text(
                        text = "$tempVal${temperatureUnit.symbol}",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 72.sp
                    )
                    Text(
                        text = current.conditionDescription,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // High/Low & Feels Like Badge Row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x18FFFFFF))
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Feels like $feelsLikeVal${temperatureUnit.symbol}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Text(
                    text = "H: $maxTemp${temperatureUnit.symbol}  L: $minTemp${temperatureUnit.symbol}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun WeatherHeroCardPreview() {
    com.example.weather_app.ui.theme.SkyFlowTheme {
        WeatherHeroCard(
            city = CityLocation(1, "New York", "United States", "US", "New York", 40.7128, -74.0060, isFavorite = true),
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
            todayForecast = DailyForecast("2025-02-15", "Today", 25.0, 18.0, 0, "Clear Sky", 0, 12.0, 5.0),
            temperatureUnit = TemperatureUnit.CELSIUS,
            onToggleFavorite = {}
        )
    }
}
