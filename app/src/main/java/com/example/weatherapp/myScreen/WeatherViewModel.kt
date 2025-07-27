package com.example.weatherapp.myScreen

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api_Retrofit.Constant
import com.example.weatherapp.api_Retrofit.NetworkResponse
import com.example.weatherapp.api_Retrofit.RetrofitInstance
import com.example.weatherapp.api_Retrofit.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel()  {
    private  val weatherApi = RetrofitInstance.weatherApi
    private val _weatherModel = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherModel: LiveData<NetworkResponse<WeatherModel>> = _weatherModel

    fun getData(city: String){
        Log.i("city name: ",city)
        viewModelScope.launch {
            _weatherModel.value = NetworkResponse.Loading
            val response = weatherApi.getWeather(Constant.apiKey, city)
            try {
                if(response.isSuccessful){
                    Log.i("My Response: ", response.body().toString() )
                    response.body()?.let {
                        _weatherModel.value = NetworkResponse.Success(it)
                    }
                }else{
                    Log.i("My Error: ", response.message())
                    Log.i("API Error Code", response.code().toString())
                    Log.i("API Error Message", response.message())
                    Log.i("API Error Body", response.errorBody()?.string() ?: "null")
                    _weatherModel.value = NetworkResponse.Failure("Failed to Load data.")
                }
            }catch (e: Exception){
                _weatherModel.value = NetworkResponse.Failure("Failed to Load data.")
            }
        }
    }
}