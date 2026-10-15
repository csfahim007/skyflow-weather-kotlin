package com.example.weather_app.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weather_app.domain.model.DailyForecast
import com.example.weather_app.domain.model.TemperatureUnit
import com.example.weather_app.ui.theme.RainCyan
import com.example.weather_app.ui.theme.SkyGlassCardBackground
import com.example.weather_app.ui.theme.SkyGlassCardBorder
import com.example.weather_app.ui.theme.SunGold

@Composable
fun DailyForecastList(
    dailyList: List<DailyForecast>,
    temperatureUnit: TemperatureUnit,
    modifier: Modifier = Modifier
) {
    if (dailyList.isEmpty()) return

    val weekMinTemp = dailyList.minOf { it.minTemperature(temperatureUnit) }
    val weekMaxTemp = dailyList.maxOf { it.maxTemperature(temperatureUnit) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SkyGlassCardBackground)
            .border(1.dp, SkyGlassCardBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "7-DAY FORECAST",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Weekly outlook",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.4f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        dailyList.forEachIndexed { index, item ->
            DailyItemRow(
                item = item,
                temperatureUnit = temperatureUnit,
                weekMinTemp = weekMinTemp,
                weekMaxTemp = weekMaxTemp
            )

            if (index < dailyList.size - 1) {
                HorizontalDivider(
                    color = Color(0x1AFFFFFF),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun DailyItemRow(
    item: DailyForecast,
    temperatureUnit: TemperatureUnit,
    weekMinTemp: Int,
    weekMaxTemp: Int
) {
    val minVal = item.minTemperature(temperatureUnit)
    val maxVal = item.maxTemperature(temperatureUnit)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Day Name
        Text(
            text = item.dayName,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(90.dp)
        )

        // Icon & Rain Chance
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(70.dp)
        ) {
            WeatherIcon(
                weatherCode = item.weatherCode,
                isDaytime = true,
                modifier = Modifier.size(24.dp)
            )

            if (item.precipitationProbabilityMax > 15) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${item.precipitationProbabilityMax}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = RainCyan,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Min Temp
        Text(
            text = "$minVal${temperatureUnit.symbol}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.End,
            modifier = Modifier.width(42.dp)
        )

        // Temp Range Bar
        val range = (weekMaxTemp - weekMinTemp).coerceAtLeast(1)
        val startFraction = ((minVal - weekMinTemp).toFloat() / range).coerceIn(0f, 0.8f)
        val endFraction = ((maxVal - weekMinTemp).toFloat() / range).coerceIn(startFraction + 0.15f, 1f)

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0x22FFFFFF))
        ) {
            val barWidth = maxWidth * (endFraction - startFraction)
            val startOffset = maxWidth * startFraction
            Box(
                modifier = Modifier
                    .padding(start = startOffset)
                    .width(barWidth)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(RainCyan, SunGold)
                        )
                    )
            )
        }

        // Max Temp
        Text(
            text = "$maxVal${temperatureUnit.symbol}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.width(42.dp)
        )
    }
}
