package com.example.forecastapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.forecastapp.data.internal.lazyDeferred
import com.example.forecastapp.data.repository.ForecastRepository

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

}