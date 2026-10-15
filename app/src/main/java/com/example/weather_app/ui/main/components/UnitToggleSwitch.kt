package com.example.weather_app.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.weather_app.domain.model.SpeedUnit
import com.example.weather_app.domain.model.TemperatureUnit

@Composable
fun UnitToggleBar(
    temperatureUnit: TemperatureUnit,
    speedUnit: SpeedUnit,
    onToggleTemperatureUnit: () -> Unit,
    onToggleSpeedUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Temp Unit Toggle Chip
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
                .clickable { onToggleTemperatureUnit() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UnitChipOption(
                label = "°C",
                isSelected = temperatureUnit == TemperatureUnit.CELSIUS
            )
            Text(
                text = " / ",
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium
            )
            UnitChipOption(
                label = "°F",
                isSelected = temperatureUnit == TemperatureUnit.FAHRENHEIT
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        // Speed Unit Toggle Chip
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
                .clickable { onToggleSpeedUnit() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UnitChipOption(
                label = "km/h",
                isSelected = speedUnit == SpeedUnit.KMH
            )
            Text(
                text = " / ",
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium
            )
            UnitChipOption(
                label = "mph",
                isSelected = speedUnit == SpeedUnit.MPH
            )
        }
    }
}

@Composable
private fun UnitChipOption(
    label: String,
    isSelected: Boolean
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.6f),
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    )
}
