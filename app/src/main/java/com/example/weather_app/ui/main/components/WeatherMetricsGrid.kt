package com.example.weather_app.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Compress
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weather_app.domain.model.CurrentWeather
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.ui.theme.RainCyan
import com.example.weather_app.ui.theme.SkyGlassCardBackground
import com.example.weather_app.ui.theme.SkyGlassCardBorder
import com.example.weather_app.ui.theme.SunGold

@Composable
fun WeatherMetricsGrid(
    current: CurrentWeather,
    speedUnit: SpeedUnit,
    modifier: Modifier = Modifier
) {
    val windVal = current.windSpeed(speedUnit)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                icon = Icons.Rounded.WaterDrop,
                iconTint = RainCyan,
                label = "Humidity",
                value = "${current.humidityPercentage}%",
                subValue = if (current.humidityPercentage > 70) "High humidity" else "Comfortable",
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                icon = Icons.Rounded.Air,
                iconTint = Color(0xFF818CF8),
                label = "Wind Speed",
                value = "$windVal ${speedUnit.symbol}",
                subValue = "Dir: ${current.windDirectionDegrees}°",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                icon = Icons.Rounded.WbSunny,
                iconTint = SunGold,
                label = "UV Index",
                value = String.format("%.1f", current.uvIndex),
                subValue = getUvCategory(current.uvIndex),
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                icon = Icons.Rounded.Compress,
                iconTint = Color(0xFF34D399),
                label = "Pressure",
                value = "${current.pressureHpa.toInt()} hPa",
                subValue = "Normal level",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricCard(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    subValue: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SkyGlassCardBackground)
            .border(1.dp, SkyGlassCardBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.65f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = subValue,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

private fun getUvCategory(uvIndex: Double): String {
    return when {
        uvIndex <= 2.0 -> "Low"
        uvIndex <= 5.0 -> "Moderate"
        uvIndex <= 7.0 -> "High"
        uvIndex <= 10.0 -> "Very High"
        else -> "Extreme"
    }
}
