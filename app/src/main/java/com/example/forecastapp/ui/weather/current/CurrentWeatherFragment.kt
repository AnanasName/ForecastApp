package com.example.forecastapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.forecastapp.R
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
        currentWeather.observe(this@CurrentWeatherFragment, Observer{
            if (it == null) return@Observer

            binding.textView.setText(it.toString())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}