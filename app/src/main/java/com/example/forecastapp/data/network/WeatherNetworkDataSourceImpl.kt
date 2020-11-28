package com.example.forecastapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastapp.data.internal.NoConnectivityException
import com.example.forecastapp.data.network.response.CurrentWeatherResponse

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            val fetchedCurrentWeather = apixuWeatherApiService
                .getCurrentWeather(location)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch(exp: NoConnectivityException){
            Log.e("Internet connection", "No internet connection. ", exp)
        }
    }
}