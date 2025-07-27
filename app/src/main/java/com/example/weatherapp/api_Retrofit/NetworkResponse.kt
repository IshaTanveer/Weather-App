package com.example.weatherapp.api_Retrofit

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T): NetworkResponse<T>()
    data class Failure(val message: String): NetworkResponse<Nothing>()
    object Loading: NetworkResponse<Nothing>()
}