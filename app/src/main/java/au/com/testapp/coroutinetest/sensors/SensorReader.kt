package au.com.testapp.coroutinetest.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import timber.log.Timber
import java.util.Date

class SensorReader(private val context: Context) : SensorEventListener {

    private val gravitySensor: Sensor?
    private val accelerometerSensor: Sensor?
    private val gyroscopeSensor: Sensor?
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    companion object {
        private const val TAG = "SensorReader"
    }

    init {
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        sensorManager.registerListener(
            this,
            gravitySensor,
            SensorManager.SENSOR_DELAY_GAME
        )

        sensorManager.registerListener(
            this,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_GAME
        )

        sensorManager.registerListener(
            this,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    @SuppressLint("LogNotTimber")
    override fun onSensorChanged(event: SensorEvent?) {
        //Not logging to timber on purpose
        android.util.Log.i(TAG, "${event?.sensor?.name ?: "Sensor"} updated at ${Date()}")
    }

    @SuppressLint("LogNotTimber")
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //Not logging to timber on purpose
        android.util.Log.i(TAG, "${sensor?.name ?: "Sensor"} accuracy updated at ${Date()}")
    }

    fun dispose() {
        Timber.i("Sensor reader destroyed")
        sensorManager.unregisterListener(this)
    }
}