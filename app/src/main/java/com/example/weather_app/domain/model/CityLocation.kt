package com.example.weather_app.domain.model

data class CityLocation(
    val id: Long,
    val name: String,
    val country: String,
    val countryCode: String? = null,
    val adminRegion: String? = null,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false
) {
    val displayName: String
        get() = if (!adminRegion.isNullOrBlank() && adminRegion != name) {
            "$name, $adminRegion, $country"
        } else {
            "$name, $country"
        }
}
