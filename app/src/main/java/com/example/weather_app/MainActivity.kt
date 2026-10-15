package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weather_app.ui.main.WeatherScreen
import com.example.weather_app.ui.main.WeatherViewModel
import com.example.weather_app.ui.theme.SkyBackgroundDark
import com.example.weather_app.ui.theme.SkyFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkyFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SkyBackgroundDark
                ) {
                    val viewModel: WeatherViewModel = hiltViewModel()
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}
