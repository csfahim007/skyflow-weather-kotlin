package com.example.weather_app.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weather_app.ui.main.components.DailyForecastList
import com.example.weather_app.ui.main.components.HourlyForecastList
import com.example.weather_app.ui.main.components.SavedCitiesChips
import com.example.weather_app.ui.main.components.SearchCityDialog
import com.example.weather_app.ui.main.components.UnitToggleBar
import com.example.weather_app.ui.main.components.WeatherErrorView
import com.example.weather_app.ui.main.components.WeatherHeroCard
import com.example.weather_app.ui.main.components.WeatherLoadingView
import com.example.weather_app.ui.main.components.WeatherMetricsGrid
import com.example.weather_app.ui.theme.ClearDayGradientEnd
import com.example.weather_app.ui.theme.ClearDayGradientStart
import com.example.weather_app.ui.theme.ClearNightGradientEnd
import com.example.weather_app.ui.theme.ClearNightGradientStart
import com.example.weather_app.ui.theme.CloudyGradientEnd
import com.example.weather_app.ui.theme.CloudyGradientStart
import com.example.weather_app.ui.theme.RainGradientEnd
import com.example.weather_app.ui.theme.RainGradientStart
import com.example.weather_app.ui.theme.SnowGradientEnd
import com.example.weather_app.ui.theme.SnowGradientStart
import com.example.weather_app.ui.theme.StormGradientEnd
import com.example.weather_app.ui.theme.StormGradientStart

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundGradient = getDynamicBackgroundGradient(
        weatherCode = uiState.weatherData?.current?.weatherCode ?: 0,
        isDaytime = uiState.weatherData?.current?.isDaytime ?: true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top App Bar
            TopAppBarRow(
                lastUpdated = uiState.weatherData?.lastUpdatedFormatted ?: "",
                isRefreshing = uiState.isRefreshing,
                temperatureUnit = uiState.temperatureUnit,
                speedUnit = uiState.speedUnit,
                onRefresh = { viewModel.refreshWeather() },
                onToggleTemperatureUnit = { viewModel.toggleTemperatureUnit() },
                onToggleSpeedUnit = { viewModel.toggleSpeedUnit() }
            )

            // Saved Cities Chips Row
            SavedCitiesChips(
                savedCities = uiState.savedCities,
                selectedCity = uiState.selectedCity,
                onSelectCity = { viewModel.selectCity(it) },
                onOpenSearch = { viewModel.openSearchDialog() },
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // Content Area
            if (uiState.isLoading) {
                WeatherLoadingView()
            } else if (uiState.errorMessage != null && uiState.weatherData == null) {
                WeatherErrorView(
                    errorMessage = uiState.errorMessage!!,
                    onRetry = { viewModel.loadWeatherForCity(uiState.selectedCity) }
                )
            } else if (uiState.weatherData != null) {
                val data = uiState.weatherData!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WeatherHeroCard(
                        city = data.city,
                        current = data.current,
                        todayForecast = data.daily.firstOrNull(),
                        temperatureUnit = uiState.temperatureUnit,
                        onToggleFavorite = { viewModel.toggleFavorite(it) }
                    )

                    WeatherMetricsGrid(
                        current = data.current,
                        speedUnit = uiState.speedUnit
                    )

                    HourlyForecastList(
                        hourlyList = data.hourly,
                        temperatureUnit = uiState.temperatureUnit
                    )

                    DailyForecastList(
                        dailyList = data.daily,
                        temperatureUnit = uiState.temperatureUnit
                    )

                    // Footer text
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Data provided by Open-Meteo REST API • SkyFlow Weather",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Search Dialog Overlay
        if (uiState.isSearchDialogOpen) {
            SearchCityDialog(
                searchQuery = uiState.searchQuery,
                searchResults = uiState.searchResults,
                isSearching = uiState.isSearching,
                onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                onSelectCity = { viewModel.selectCity(it) },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onDismiss = { viewModel.closeSearchDialog() }
            )
        }
    }
}

@Composable
private fun TopAppBarRow(
    lastUpdated: String,
    isRefreshing: Boolean,
    temperatureUnit: com.example.weather_app.domain.model.TemperatureUnit,
    speedUnit: com.example.weather_app.domain.model.SpeedUnit,
    onRefresh: () -> Unit,
    onToggleTemperatureUnit: () -> Unit,
    onToggleSpeedUnit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "SkyFlow",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            if (lastUpdated.isNotEmpty()) {
                Text(
                    text = "Updated $lastUpdated",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UnitToggleBar(
                temperatureUnit = temperatureUnit,
                speedUnit = speedUnit,
                onToggleTemperatureUnit = onToggleTemperatureUnit,
                onToggleSpeedUnit = onToggleSpeedUnit
            )

            Spacer(modifier = Modifier.size(8.dp))

            IconButton(onClick = onRefresh) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Refresh weather",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

private fun getDynamicBackgroundGradient(
    weatherCode: Int,
    isDaytime: Boolean
): Brush {
    val (startColor, endColor) = when (weatherCode) {
        0, 1 -> if (isDaytime) ClearDayGradientStart to ClearDayGradientEnd else ClearNightGradientStart to ClearNightGradientEnd
        2, 3, 45, 48 -> CloudyGradientStart to CloudyGradientEnd
        51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> RainGradientStart to RainGradientEnd
        71, 73, 75, 77, 85, 86 -> SnowGradientStart to SnowGradientEnd
        95, 96, 99 -> StormGradientStart to StormGradientEnd
        else -> if (isDaytime) ClearDayGradientStart to ClearDayGradientEnd else ClearNightGradientStart to ClearNightGradientEnd
    }

    return Brush.verticalGradient(
        colors = listOf(startColor, endColor)
    )
}
