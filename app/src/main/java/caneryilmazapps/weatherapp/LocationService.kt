package caneryilmazapps.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import java.lang.Exception


class LocationService(private val context: Context) : LocationListener {
    // flag for GPS status
    private var isGPSEnabled = false
    // flag for network status
    private var isNetworkEnabled = false
    // flag for GPS status
    private var canGetLocation = false

    private var location: Location? = null // location
    private var latitude: Double = 0.toDouble() // latitude
    private var longitude: Double = 0.toDouble() // longitude

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 1.0F // 10 meters
    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES: Long = 1 // 1 minute
    // Declaring a Location Manager
    private var locationManager: LocationManager? = null

    fun getCurrentLocation(): Location? {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            // getting GPS status
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
            isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                if (isNetworkEnabled) {
                    location = null
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                        locationManager?.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                        )

                    }
                    if (locationManager != null) {
                        location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                        if (location != null) {
                            latitude = location?.latitude!!
                            longitude = location?.longitude!!
                        }
                    }
                }
                if (isGPSEnabled) {
                    location = null
                    if (location == null) {
                        locationManager?.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                        )
                    }
                    if (locationManager != null) {
                        location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                        if (location != null) {
                            latitude = location?.latitude!!
                            longitude = location?.longitude!!
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.stackTrace
        }
        return location
    }

    fun getCurrentLatitude(): Double {
        return latitude
    }

    fun getCurrentLongitude(): Double {
        return longitude
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}