package caneryilmazapps.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.city_find_dialog.*
import kotlinx.android.synthetic.main.history_dialog.*
import retrofit2.*
import java.lang.Exception
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    private var isTypeLocation = true
    private var selectedCity = ""
    private lateinit var weatherAdapter: WeatherRecyclerViewAdapter
    private lateinit var database: ApplicationDatabase
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        database = ApplicationDatabase(this@HomeActivity)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@HomeActivity)
        val savedLastCity = sharedPreferences.getString("LastCity", "")
        if (savedLastCity == "") {
            val alertDialog = AlertDialog.Builder(this@HomeActivity)
            alertDialog.setTitle("Choose Action")
            alertDialog.setMessage("Choose Weather App start type")
            alertDialog.setPositiveButton("Find My Location") { dialog, _ ->
                try {
                    isTypeLocation = true
                    getLocation()
                    dialog.dismiss()
                } catch (e: Exception) {
                    isTypeLocation = false
                    Toast.makeText(this@HomeActivity, "Weather app cannot find you, please enter a city name.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    showCityAddDialog()
                    e.stackTrace
                }
            }
            alertDialog.setNegativeButton("Find A City") { dialog, _ ->
                isTypeLocation = false
                showCityAddDialog()
                dialog.dismiss()
            }
            alertDialog.show()
        } else {
            getWeatherInfo(savedLastCity!!)
        }

    }

    private fun getLocation() {
        try {
            val location = LocationService(this@HomeActivity).getCurrentLocation()
            getWeatherInfo(location?.longitude!!, location.latitude)
        } catch (e: Exception) {
            Toast.makeText(this@HomeActivity, "Weather app cannot find you, please enter a city name.", Toast.LENGTH_SHORT).show()
            showCityAddDialog()
            e.stackTrace
        }
    }

    private fun getWeatherInfo(cityName: String) {

        val retrofit = RetrofitInstance().getInstance()
        val apiInterfaceCurrent = retrofit.create(ApiInterface::class.java)
        val callCurrent = apiInterfaceCurrent.getCurrentByCityName(cityName, "780fb7a637c9432757293c9e90775788")
        callCurrent.enqueue(object : Callback<CurrentWeatherModel> {
            override fun onFailure(call: Call<CurrentWeatherModel>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CurrentWeatherModel>, response: Response<CurrentWeatherModel>) {
                if (response.isSuccessful) {
                    loadCurrentWeatherData(response.body()!!)
                } else {
                    Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
                }
            }

        })

        var forecastWeatherModelList: ForecastWeatherModel
        val apiInterfaceForecast = retrofit.create(ApiInterface::class.java)
        val callForecast = apiInterfaceForecast.getForecastByCityName(cityName, "780fb7a637c9432757293c9e90775788")
        callForecast.enqueue(object : Callback<ForecastWeatherModel> {
            override fun onFailure(call: Call<ForecastWeatherModel>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ForecastWeatherModel>, response: Response<ForecastWeatherModel>) {
                if (response.isSuccessful) {
                    forecastWeatherModelList = response.body()!!
                    fillForecastWeather(forecastWeatherModelList)
                } else {
                    Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getWeatherInfo(longitude: Double, latitude: Double) {

        val retrofit = RetrofitInstance().getInstance()
        val apiInterfaceCurrent = retrofit.create(ApiInterface::class.java)
        val callCurrent = apiInterfaceCurrent.getCurrentByCoordinates(latitude.toString(), longitude.toString(), "780fb7a637c9432757293c9e90775788")
        callCurrent.enqueue(object : Callback<CurrentWeatherModel> {
            override fun onFailure(call: Call<CurrentWeatherModel>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CurrentWeatherModel>, response: Response<CurrentWeatherModel>) {
                if (response.isSuccessful) {
                    loadCurrentWeatherData(response.body()!!)
                } else {
                    Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
                }
            }

        })

        var forecastWeatherModelList: ForecastWeatherModel
        val apiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiInterface.getForecastByCoordinates(latitude.toString(), longitude.toString(), "780fb7a637c9432757293c9e90775788")
        call.enqueue(object : Callback<ForecastWeatherModel> {
            override fun onFailure(call: Call<ForecastWeatherModel>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ForecastWeatherModel>, response: Response<ForecastWeatherModel>) {
                if (response.isSuccessful) {
                    forecastWeatherModelList = response.body()!!
                    fillForecastWeather(forecastWeatherModelList)
                } else {
                    Toast.makeText(this@HomeActivity, "Weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun loadCurrentWeatherData(currentWeatherModel: CurrentWeatherModel) {
        cityNameHome_tv.text = currentWeatherModel.name.toUpperCase(Locale.getDefault())
        weatherInfoHome_tv.text = currentWeatherModel.weather[0].main.toUpperCase(Locale.getDefault()) + ", " + currentWeatherModel.weather[0].description.toUpperCase(Locale.getDefault())
        weatherDegreeHome_tv.text = ((currentWeatherModel.main.temp - 273.15)).toInt().toString() + "°C"
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.UK)
        var timestamp = Timestamp(currentWeatherModel.dt)
        var date = Date(timestamp.time * 1000L)
        dateInfoHome_tv.text = dateFormat.format(date)
        windInfoHome_tv.text = "Wind: ${currentWeatherModel.wind.speed} m/s, ${currentWeatherModel.wind.deg}"
        cloudinessInfoHome_tv.text = "Cloudiness : ${currentWeatherModel.clouds.all} %"
        pressureInfoHome_tv.text = "Pressure: ${currentWeatherModel.main.pressure} hpa"
        humidityInfoHome_tv.text = "Humidity: ${currentWeatherModel.main.humidity} %"
        timestamp = Timestamp(currentWeatherModel.sys.sunrise)
        date = Date(timestamp.time * 1000L)
        val sunrise = dateFormat.format(date)

        timestamp = Timestamp(currentWeatherModel.sys.sunset)
        date = Date(timestamp.time * 1000L)
        val sunset = dateFormat.format(date)
        sunInfoHome_tv.text = "Sunrise: $sunrise\nSunset: $sunset"
        val iconID = resources.getIdentifier("w" + currentWeatherModel.weather[0].icon, "drawable", this.packageName)
        weatherIconHome_iv.setImageResource(iconID)
    }

    private fun fillForecastWeather(forecastWeatherModelList: ForecastWeatherModel) {
        weatherAdapter = WeatherRecyclerViewAdapter(this@HomeActivity, forecastWeatherModelList.city.name, forecastWeatherModelList.list)
        city_rv.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayout.VERTICAL, false)
        city_rv.adapter = weatherAdapter
        city_rv.scrollToPosition(0)
    }

    private fun showCityAddDialog() {
        val cityAddDialog = Dialog(this@HomeActivity)
        cityAddDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        cityAddDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cityAddDialog.setContentView(R.layout.city_find_dialog)
        cityAddDialog.show()
        cityAddDialog.closeAddNewCity_btn.setOnClickListener {
            cityAddDialog.dismiss()
        }
        cityAddDialog.addNewCity_btn.setOnClickListener {
            if (cityAddDialog.cityName_et.text.toString() != "") {
                checkCityAvailable(cityAddDialog.cityName_et.text.toString())
                selectedCity = cityAddDialog.cityName_et.text.toString()
                val editor = sharedPreferences.edit()
                editor.putString("LastCity", selectedCity)
                editor.apply()
            }
            cityAddDialog.dismiss()
        }
    }

    private fun checkCityAvailable(cityName: String) {
        val retrofit = RetrofitInstance().getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiInterface.getForecastByCityName(cityName, "780fb7a637c9432757293c9e90775788")
        call.enqueue(object : Callback<ForecastWeatherModel> {
            override fun onFailure(call: Call<ForecastWeatherModel>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "City weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ForecastWeatherModel>, response: Response<ForecastWeatherModel>) {
                if (response.isSuccessful) {
                    getWeatherInfo(selectedCity)
                    database.saveCity(selectedCity)
                } else {
                    Toast.makeText(this@HomeActivity, "City weather information could not be retrieved.", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun showHistoryDialog() {
        val historyDialog = Dialog(this@HomeActivity)
        historyDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        historyDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        historyDialog.setContentView(R.layout.history_dialog)
        historyDialog.show()
        val historyList = database.getCities()
        val adapter = ArrayAdapter<String>(this@HomeActivity, android.R.layout.simple_list_item_1, historyList)
        historyDialog.history_lv.adapter = adapter
        historyDialog.history_lv.setOnItemClickListener { _, _, position, _ ->
            selectedCity = historyDialog.history_lv.getItemAtPosition(position).toString()
            getWeatherInfo(historyDialog.history_lv.getItemAtPosition(position).toString())
            historyDialog.dismiss()
            val editor = sharedPreferences.edit()
            editor.putString("LastCity", selectedCity)
            editor.apply()
        }
        historyDialog.history_lv.setOnItemLongClickListener { _, _, position, _ ->
            val alertDialog = AlertDialog.Builder(this@HomeActivity)
            alertDialog.setTitle("Are you sure?")
            alertDialog.setMessage("Do you want this record?")
            alertDialog.setNegativeButton("Clear History") { dialog, _ ->
                database.deleteAllCity()
                adapter.clear()
                adapter.notifyDataSetChanged()
                dialog.dismiss()
                historyDialog.dismiss()
            }
            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                database.deleteCity(historyDialog.history_lv.getItemAtPosition(position).toString())
                adapter.remove(historyDialog.history_lv.getItemAtPosition(position).toString())
                adapter.notifyDataSetChanged()
                dialog.dismiss()
                historyDialog.dismiss()
            }
            alertDialog.show()
            return@setOnItemLongClickListener true

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.refresh_menu -> {
                if (isTypeLocation)
                    getLocation()
                else
                    getWeatherInfo(selectedCity)
            }
            R.id.cityHistory_menu -> {
                showHistoryDialog()
            }
            R.id.findCity_menu -> {
                item.isChecked = true
                isTypeLocation = false
                showCityAddDialog()
            }
            R.id.findLocation_menu -> {
                item.isChecked = true
                isTypeLocation = true
                getLocation()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
