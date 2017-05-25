package com.awarepoint.androidaccuracytest.LocationEngine.Sensors;

/**
 * Created by ureyes on 8/28/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


import com.awarepoint.androidaccuracytest.R;

import java.util.List;

public class AccelerometerManager {

    private static Context aContext = null;


    /**
     * Accuracy configuration
     */
    private static float threshold = 15.0f;
    private static int interval = 200;

    private static Sensor sensorAccelerometer;
    private static Sensor sensorMagnetic;
    private static SensorManager sensorManager;
    private static AccelerometerListener listener;

    private static Boolean supported;
    private static boolean running = false;

    /**
     * Returns true if the manager is listening to orientation changes
     */
    public static boolean isListening() {
        return running;
    }

    /**
     * Unregisters listeners
     */
    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Returns true if at least one Accelerometer sensor is available
     */
    public static boolean isSupported(Context context) {
        aContext = context;
        if (supported == null) {
            if (aContext != null) {

                try {
                    sensorManager = (SensorManager) aContext.getSystemService(Context.SENSOR_SERVICE);

                    // Get all sensors in device
                    List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

                    supported = new Boolean(sensors.size() > 0);
                } catch (Exception err) {
                    supported = Boolean.FALSE;
                }


            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }

    /**
     * Configure the listener for shaking
     *
     * @param threshold minimum acceleration variation for considering shaking
     * @param interval  minimum interval between to shake events
     */
    public static void configure(int threshold, int interval) {
        AccelerometerManager.threshold = threshold;
        AccelerometerManager.interval = interval;
    }

    /**
     * Registers a listener and start listening
     *
     * @param accelerometerListener callback for accelerometer events
     */
    public static void startListening(AccelerometerListener accelerometerListener) {

        sensorManager = (SensorManager) aContext.getSystemService(Context.SENSOR_SERVICE);

        // Take all sensors in device
        List<Sensor> sensorsAccelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        //List<Sensor> sensorsMagneticField = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);

        if (sensorsAccelerometer.size() > 0) {

            sensorAccelerometer = sensorsAccelerometer.get(0);
            // Register Accelerometer Listener
            running = sensorManager.registerListener(sensorEventListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            listener = accelerometerListener;
        }


    }

    /**
     * Configures threshold and interval
     * And registers a listener and start listening
     *
     * @param accelerometerListener callback for accelerometer events
     * @param threshold             minimum acceleration variation for considering shaking
     * @param interval              minimum interval between to shake events
     */
    public static void startListening(AccelerometerListener accelerometerListener, int threshold, int interval) {
        configure(threshold, interval);
        startListening(accelerometerListener);
    }

    /**
     * The listener that listen to events from the accelerometer listener
     */
    private static SensorEventListener sensorEventListener =
            new SensorEventListener() {

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }

                private float x = 0;
                private float y = 0;
                private float z = 0;

                float[] mGravity;

                float mAccel = 0.00f;
                float mAccelCurrent = (float) 9.80665;
                float mAccelLast = (float) 9.80665;

                public void onSensorChanged(SensorEvent event) {

                    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        mGravity = event.values.clone();
                        x = mGravity[0];
                        y = mGravity[1];
                        z = mGravity[2];

                        //          mAccelLast = mAccelCurrent;
                        //           mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                        //           float delta = mAccelCurrent - mAccelLast;
                        //          mAccel = mAccel * 0.9f + delta;
                        // Make this higher (less sensitive) or lower according to how much motion you want to detect
                        //           if(mAccel > 1.0){
                        listener.onShake(mAccel);
                        listener.onAccelerationChanged(x, y, z);
                       /* }else{
                            listener.onAccelerationChanged(x,y,z);
                        }*/

                    }

                }


            };

    public static class MainAccelerometer extends Activity implements AccelerometerListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Check onResume Method to start accelerometer listener
        }

        public void onAccelerationChanged(float x, float y, float z) {
            // TODO Auto-generated method stub

        }

        public void onShake(float force) {

            // Do your stuff here

            // Called when Motion Detected
            // Toast.makeText(getBaseContext(), "Motion detected", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onResume() {
            super.onResume();
            if (isSupported(this)) {
                startListening(this);
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            if (isListening()) {
                stopListening();
            }

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i("Sensor", "Service  destroy");

            if (isListening()) {
                stopListening();
            }

        }

    }
}