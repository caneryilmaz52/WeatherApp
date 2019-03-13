package caneryilmazapps.weatherapp

data class CurrentWeatherModel(
    var base: String,
    var clouds: CurrentClouds,
    var cod: Int,
    var coord: CurrentCoord,
    var dt: Long,
    var id: Int,
    var main: CurrentMain,
    var name: String,
    var sys: CurrentSys,
    var visibility: Int,
    var weather: List<CurrentWeather>,
    var wind: CurrentWind
)

data class CurrentMain(
    var humidity: Int,
    var pressure: Int,
    var temp: Double,
    var tempMax: Double,
    var tempMin: Double
)

data class CurrentSys(
    var country: String,
    var id: Int,
    var message: Double,
    var sunrise: Long,
    var sunset: Long,
    var type: Int
)

data class CurrentWeather(
    var description: String,
    var icon: String,
    var id: Int,
    var main: String
)

data class CurrentWind(
    var deg: Int,
    var speed: Double
)

data class CurrentClouds(
    var all: Int
)

data class CurrentCoord(
    var lat: Double,
    var lon: Double
)