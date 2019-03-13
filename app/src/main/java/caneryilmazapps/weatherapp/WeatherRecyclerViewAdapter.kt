package caneryilmazapps.weatherapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class WeatherRecyclerViewAdapter(private val context: Context, private val cityName:String, private val forecastWeatherList: List<Forecast>) :
    RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.city_recyclerview_item, p0, false))
    }

    override fun getItemCount(): Int {
        return forecastWeatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cityName.text = cityName
        holder.weatherInfo.text = forecastWeatherList[position].weather[0].main.toUpperCase(Locale.getDefault()) + ", " + forecastWeatherList[position].weather[0].description.toUpperCase(Locale.getDefault())
        holder.weatherDegree.text = ((forecastWeatherList[position].main.temp - 273.15)).toInt().toString() + "°C"
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.UK)
        val timestamp = Timestamp(forecastWeatherList[position].dt)
        val date = Date(timestamp.time * 1000L)
        holder.dateInfo.text = dateFormat.format(date)
        holder.windInfo.text = "Wind: ${forecastWeatherList[position].wind.speed} m/s, ${forecastWeatherList[position].wind.deg}"
        holder.cloudinessInfo.text = "Cloudiness : ${forecastWeatherList[position].clouds.all} %"
        holder.pressureInfo.text = "Pressure: ${forecastWeatherList[position].main.pressure} hpa"
        holder.humidityInfo.text = "Humidity: ${forecastWeatherList[position].main.humidity} %"

        val iconID = context.resources.getIdentifier("w" + forecastWeatherList[position].weather[0].icon, "drawable", context.packageName)
        holder.weatherIcon.setImageResource(iconID)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon_iv)
        val weatherInfo = view.findViewById<TextView>(R.id.weatherInfo_tv)
        val weatherDegree = view.findViewById<TextView>(R.id.weatherDegree_tv)
        val cityName = view.findViewById<TextView>(R.id.cityName_tv)
        val dateInfo = view.findViewById<TextView>(R.id.dateInfo_tv)
        val windInfo = view.findViewById<TextView>(R.id.windInfo_tv)
        val cloudinessInfo = view.findViewById<TextView>(R.id.cloudinessInfo_tv)
        val pressureInfo = view.findViewById<TextView>(R.id.pressureInfo_tv)
        val humidityInfo= view.findViewById<TextView>(R.id.humidityInfo_tv)
    }
}