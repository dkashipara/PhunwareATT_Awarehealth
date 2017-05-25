package com.awarepoint.androidaccuracytest.LocationEngine.Sensors;

/**
 * Created by ureyes on 8/28/2015.
 */
public interface AccelerometerListener {
    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);
}
