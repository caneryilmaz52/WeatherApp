package caneryilmazapps.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseURL = "http://api.openweathermap.org/data/2.5/"

class RetrofitInstance {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}