package com.laetitia.chicote

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.laetitia.chicote.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        mediaPlayer = MediaPlayer.create(this, R.raw.whip)
        mediaPlayer.isLooping = false

        if (accelerometer == null) {

        }

        binding.btnWhip.setOnClickListener {
            playSound()
        }

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            sensorListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }

    private val sensorListener = object : SensorEventListener {
        private val SHAKE_THRESHOLD = 20.0f
        private var lastShakeTime: Long = 0

        override fun onSensorChanged(event: SensorEvent) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastShakeTime

            if (timeDifference > 1000) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH

                if (acceleration > SHAKE_THRESHOLD) {
                    playSound()
                    lastShakeTime = currentTime
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    private fun playSound(){
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

}