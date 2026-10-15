package com.example.weather_app.data.dto

import com.google.gson.annotations.SerializedName

data class CitySearchResponseDto(
    @SerializedName("results") val results: List<CitySearchResultDto>?
)

data class CitySearchResultDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("country") val country: String?,
    @SerializedName("country_code") val countryCode: String?,
    @SerializedName("admin1") val admin1: String?
)
