package caneryilmazapps.weatherapp

data class ForecastWeatherModel(
    var city: City,
    var cnt: Int,
    var cod: String,
    var list: List<Forecast>,
    var message: Double
)

data class City(
    var coord: Coord,
    var country: String,
    var id: Int,
    var name: String,
    var population: Int
)

data class Coord(
    var lat: Double,
    var lon: Double
)

data class Forecast(
    var clouds: Clouds,
    var dt: Long,
    var dtTxt: String,
    var main: Main,
    var rain: Rain,
    var sys: Sys,
    var weather: List<Weather>,
    var wind: Wind
)

data class Wind(
    var deg: Double,
    var speed: Double
)

data class Main(
    var grndLevel: Double,
    var humidity: Int,
    var pressure: Double,
    var seaLevel: Double,
    var temp: Double,
    var tempKf: Int,
    var tempMax: Double,
    var tempMin: Double
)

data class Weather(
    var description: String,
    var icon: String,
    var id: Int,
    var main: String
)

data class Sys(
    var pod: String
)

data class Rain(
    var h: Double
)

data class Clouds(
    var all: Int
)