package com.example.andsorensen.quicktemp;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import java.util.List;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager globalSensorManager;
    private Sensor TempSensor;
    private Sensor PressSensor;

    private String appLabel = "QuickTemp";
    private Button farenheitButton;
    private Button celsiusButton;
    private TextView tempOutput;
    private TextView pressOutput;
    private char tempUnit = 'F';
    private float temp;
    private float press;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init the sensor manager and get a list of all sensors present on the device (only for debug purposes).
        globalSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> allSensors = globalSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.i(appLabel, "All Sensors:"+allSensors.toString());

        //Check for required sensors
        TempSensor = globalSensorManager.getDefaultSensor(65536);
        PressSensor = globalSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (TempSensor != null) {
            Log.i(appLabel, "Ambient Temperature sensor detected of type: "+TempSensor.getName());
            globalSensorManager.registerListener(this, TempSensor, 10000);
        } else {
            Log.i(appLabel, "No Ambient Temperature sensor found.");
        }
        if (PressSensor != null) {
            Log.i(appLabel, "Pressure sensor detected of type: "+PressSensor.getName());
            globalSensorManager.registerListener(this, PressSensor, 10000);
        } else {
            Log.i(appLabel, "No Pressure sensor found.");
        }
    }


    // =============== SENSOR EVENTS ============== //

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(appLabel, "Accuracy set to:"+accuracy);
    }

    public final void onSensorChanged(SensorEvent event) {
        Log.i(appLabel, "Sensor event for type: "+event.sensor.getStringType());
    }

    //Called when the app is resumed
    @Override
    protected void onResume() {
        super.onResume();
        globalSensorManager.registerListener(this, TempSensor, 10000);
        globalSensorManager.registerListener(this, PressSensor, 10000);
    }

    //Called when the app is paused
    @Override
    protected void onPause() {
        super.onPause();
        globalSensorManager.unregisterListener(this);
    }

}
