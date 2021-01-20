package com.example.inertial_hr

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidplot.xy.*
import java.io.File
import java.io.FileWriter

class MainActivity : AppCompatActivity(), SensorEventListener{
    private lateinit var sensorManager: SensorManager
    private var Acc: Sensor? = null
    var Acc_X = arrayListOf<Float>()
    var Acc_Y = arrayListOf<Float>()
    var Acc_Z = arrayListOf<Float>()
    var writer : FileWriter? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        writer  = FileWriter(File(this.getExternalFilesDir(null), "sensors_" + "Test1" + ".txt"))
        Toast.makeText(this,this.getExternalFilesDir(null).toString(),Toast.LENGTH_SHORT).show()
        writer!!.write("X,Y,Z" + "\n")



        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        Log.d("sensors", deviceSensors.toString())

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.d("sensors", "Linear accelerometer found!")
            Acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        } else {
            Log.d("sensors", "No linear accelerometer found!")
        }
        sensorManager.registerListener(this, Acc, SensorManager.SENSOR_DELAY_GAME);

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.

    }

    override fun onSensorChanged(event: SensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        Log.d("ACC", event.values[0].toString() +"|"+ event.values[1]+"|"+event.values[2] )
        Acc_X.add(event.values[0])
        Acc_Y.add(event.values[1])
        Acc_Z.add(event.values[2])

        var X_field : TextView  = findViewById(R.id.X_value)
        var Y_field : TextView  = findViewById(R.id.Y_value)
        var Z_field : TextView  = findViewById(R.id.Z_value)

        X_field.text = "X: " + event.values[0].toString()
        Y_field.text = "Y: " + event.values[1].toString()
        Z_field.text = "Z: " + event.values[2].toString()
        if (writer != null) {
            writer!!.write(event.values[0].toString() + "," + event.values[1].toString() + "," + event.values[0].toString() + "\n")
        }




        if(Acc_X.size == 500){
            Acc_X.removeAt(0)
            Acc_Y.removeAt(0)
            Acc_Z.removeAt(0)
        }
        var Acc_plot : XYPlot =  findViewById(R.id.ACC_plot)
        Acc_plot.setDomainBoundaries(0,100, BoundaryMode.AUTO)
        Acc_plot.setRangeBoundaries(1,1, BoundaryMode.AUTO)
        PanZoom.attach(Acc_plot, PanZoom.Pan.BOTH, PanZoom.Zoom.STRETCH_BOTH);

        val series_X : XYSeries = SimpleXYSeries(Acc_X, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "X")
        val series_Y : XYSeries = SimpleXYSeries(Acc_Y, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Y")
        val series_Z : XYSeries = SimpleXYSeries(Acc_Z, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Z")


        var series_X_format = LineAndPointFormatter(Color.RED, null, null, null); // Line, Fill, Point
        var series_Y_format = LineAndPointFormatter(Color.GREEN, null, null, null); // Line, Fill, Point
        var series_Z_format = LineAndPointFormatter(Color.BLUE, null, null, null); // Line, Fill, Point

        Acc_plot.clear()
        Acc_plot.addSeries(series_X,series_X_format)
        Acc_plot.addSeries(series_Y,series_Y_format)
        Acc_plot.addSeries(series_Z,series_Z_format)
        Acc_plot.redraw()

        // Do something with this sensor value.
    }

    override fun onResume() {

        super.onResume()
            sensorManager.registerListener(this, Acc, SensorManager.SENSOR_DELAY_NORMAL)



    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        writer?.close()
    }

}

