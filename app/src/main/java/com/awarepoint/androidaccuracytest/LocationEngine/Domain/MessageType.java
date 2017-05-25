package com.awarepoint.androidaccuracytest.LocationEngine.Domain;

/**
 * Created by ureyes on 12/21/2015.
 */
public enum MessageType {
    CLOSEST_BEACON(0), ALL_BEACONS_BY_RSSI(1);
    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}