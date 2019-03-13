package caneryilmazapps.weatherapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.lang.Exception

class ApplicationDatabase(private val context:Context): SQLiteOpenHelper(context,"AppDatabase",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val cityQuery = "CREATE TABLE IF NOT EXISTS CITY(cityId INTEGER PRIMARY KEY AUTOINCREMENT, CityName VARCHAR(128))"
        db?.execSQL(cityQuery)
    }

    fun saveCity(cityName:String){
        try {
            val database = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("CityName",cityName)
            val result = database.insert("CITY",null,contentValues)
            if (result != -1L)
                Toast.makeText(context,"Saving successful.",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context,"Saving unsuccessful, try again.",Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.stackTrace
        }catch (e:SQLiteException){
            e.stackTrace
        }
    }

    fun getCities(): ArrayList<String> {

        val listOfCities = ArrayList<String>()
        val database=this.readableDatabase
        val query = "SELECT * FROM CITY"
        val cursor = database.rawQuery(query,null)
        if (cursor.moveToFirst()){
            do {
                val cityName = cursor.getString(1)
                listOfCities.add(cityName)
            }while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return listOfCities
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun deleteCity(cityName: String) {
        try {
            val database = this.writableDatabase
            val result = database.delete("CITY","CityName = '$cityName'",null)
            if (result != -1)
                Toast.makeText(context,"Deleting successful.",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context,"Deleting unsuccessful, try again.",Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.stackTrace
        }catch (e:SQLiteException){
            e.stackTrace
        }
    }
    fun deleteAllCity() {
        try {
            val database = this.writableDatabase
            val result = database.delete("CITY",null,null)
            if (result != -1)
                Toast.makeText(context,"Deleting successful.",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context,"Deleting unsuccessful, try again.",Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.stackTrace
        }catch (e:SQLiteException){
            e.stackTrace
        }
    }
}