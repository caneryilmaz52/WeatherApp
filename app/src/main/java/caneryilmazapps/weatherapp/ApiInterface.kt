package caneryilmazapps.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("forecast")
    fun getForecastByCityName(@Query("q") city:String, @Query("appid") appId:String): Call<ForecastWeatherModel>

    @GET("weather")
    fun getCurrentByCityName(@Query("q") city:String,@Query("appid") appId:String): Call<CurrentWeatherModel>

    @GET("weather")
    fun getCurrentByCoordinates(@Query("lat") latitude:String, @Query("lon") longitude:String, @Query("appid") appId:String): Call<CurrentWeatherModel>

    @GET("forecast")
    fun getForecastByCoordinates(@Query("lat") latitude:String, @Query("lon") longitude:String, @Query("appid") appId:String): Call<ForecastWeatherModel>
}
