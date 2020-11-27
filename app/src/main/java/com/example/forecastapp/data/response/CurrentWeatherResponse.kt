package com.example.forecastapp.data.response

import com.example.forecastapp.data.db.entity.CurrentWeatherEntry
import com.example.forecastapp.data.db.entity.Location
import com.example.forecastapp.data.db.entity.Request
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location,
    val request: Request
)