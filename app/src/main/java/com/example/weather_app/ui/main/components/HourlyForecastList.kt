package com.example.weather_app.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weather_app.domain.model.HourlyForecast
import com.example.weather_app.domain.model.TemperatureUnit
import com.example.weather_app.ui.theme.RainCyan
import com.example.weather_app.ui.theme.SkyGlassCardBackground
import com.example.weather_app.ui.theme.SkyGlassCardBorder

@Composable
fun HourlyForecastList(
    hourlyList: List<HourlyForecast>,
    temperatureUnit: TemperatureUnit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SkyGlassCardBackground)
            .border(1.dp, SkyGlassCardBorder, RoundedCornerShape(24.dp))
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HOURLY FORECAST",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Next 24 Hours",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.4f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(hourlyList) { index, item ->
                HourlyItemCard(
                    item = item,
                    temperatureUnit = temperatureUnit,
                    isNow = index == 0
                )
            }
        }
    }
}

@Composable
private fun HourlyItemCard(
    item: HourlyForecast,
    temperatureUnit: TemperatureUnit,
    isNow: Boolean
) {
    val tempVal = item.temperature(temperatureUnit)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isNow) Color(0x3A0F82FF) else Color(0x11FFFFFF))
            .border(
                1.dp,
                if (isNow) MaterialTheme.colorScheme.primary else Color(0x22FFFFFF),
                RoundedCornerShape(18.dp)
            )
            .padding(vertical = 12.dp, horizontal = 6.dp)
    ) {
        Text(
            text = if (isNow) "Now" else item.timeFormatted,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = if (isNow) 1f else 0.7f),
            fontWeight = if (isNow) FontWeight.Bold else FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(8.dp))

        WeatherIcon(
            weatherCode = item.weatherCode,
            isDaytime = item.isDaytime,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$tempVal${temperatureUnit.symbol}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        if (item.precipitationProbabilityPercentage > 15) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${item.precipitationProbabilityPercentage}%",
                style = MaterialTheme.typography.labelMedium,
                color = RainCyan,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
