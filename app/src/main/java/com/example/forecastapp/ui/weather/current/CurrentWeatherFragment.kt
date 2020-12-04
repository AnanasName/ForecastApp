package com.example.forecastapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.forecastapp.R
import com.example.forecastapp.data.db.entity.glide.GlideApp
import com.example.forecastapp.data.network.ApixuWeatherApiService
import com.example.forecastapp.data.network.ConnectivityInterceptorImpl
import com.example.forecastapp.data.network.WeatherNetworkDataSourceImpl
import com.example.forecastapp.databinding.CurrentWeatherFragmentBinding
import com.example.forecastapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.locks.Condition

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private var _binding: CurrentWeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch{
        val currentWeather = viewModel.weather.await()

        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if(location == null) return@Observer
            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer{
            if (it == null) return@Observer

            binding.groupLoading.visibility = View.GONE
            updateWeatherDescription(it.weatherDescriptions[0])
            updateDateToToday()
            updateTemperatures(it.temperature, it.feelslike)
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)
            updateCloudCover(it.cloudcover)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(binding.imageViewConditionIcon)
        })
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double){
        binding.textViewTemperature.text = "$temperature °C"
        binding.textViewFeelsLikeTemperature.text = "Feels like $feelsLike °C"
    }

    private fun updatePrecipitation(precipitationVolume: Double){
        binding.textViewPrecipitation.text = "Precipitation: $precipitationVolume mm"
    }

    private fun updateWind(windDirection: String, windSpeed: Double){
        binding.textViewWind.text = "Wind: $windDirection, $windSpeed m/s"
    }

    private fun updateVisibility(visibilityDistance: Double){
        binding.textViewVisibility.text = "Visibility: ${visibilityDistance} km"
    }

    private fun updateCloudCover(cloudCover: Double){
        binding.textViewPressure.text = "Cloudcover: ${cloudCover}"
    }

    private fun updateWeatherDescription(weatherDescription: String){
        binding.textViewWeatherDescription.text = weatherDescription
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}