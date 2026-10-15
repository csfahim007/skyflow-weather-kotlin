package com.example.weather_app.ui.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.FilterDrama
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbCloudy
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.weather_app.ui.theme.RainCyan
import com.example.weather_app.ui.theme.SnowWhite
import com.example.weather_app.ui.theme.StormViolet
import com.example.weather_app.ui.theme.SunGold

@Composable
fun WeatherIcon(
    weatherCode: Int,
    isDaytime: Boolean = true,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    val (icon, defaultTint) = when (weatherCode) {
        0 -> if (isDaytime) Icons.Rounded.WbSunny to SunGold else Icons.Rounded.NightsStay to Color(0xFFE2E8F0)
        1 -> if (isDaytime) Icons.Rounded.WbSunny to SunGold else Icons.Rounded.NightsStay to Color(0xFFCBD5E1)
        2 -> Icons.Rounded.WbCloudy to Color(0xFFCBD5E1)
        3 -> Icons.Rounded.Cloud to Color(0xFF94A3B8)
        45, 48 -> Icons.Rounded.FilterDrama to Color(0xFF94A3B8)
        51, 53, 55, 56, 57 -> Icons.Rounded.WaterDrop to RainCyan
        61, 63, 65, 66, 67, 80, 81, 82 -> Icons.Rounded.Grain to RainCyan
        71, 73, 75, 77, 85, 86 -> Icons.Rounded.AcUnit to SnowWhite
        95, 96, 99 -> Icons.Rounded.Thunderstorm to StormViolet
        else -> Icons.Rounded.WbCloudy to SunGold
    }

    Icon(
        imageVector = icon,
        contentDescription = "Weather condition icon",
        tint = tint ?: defaultTint,
        modifier = modifier
    )
}
