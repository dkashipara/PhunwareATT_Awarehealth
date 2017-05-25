package com.awarepoint.androidaccuracytest.MobileConfiguration;

/**
 * Created by mspivey on 2/3/2016.
 * Copyright(c) 2016 Awarepoint. All rights reserved.
 */

public class InvalidBeaconZoneException extends Exception {
    public InvalidBeaconZoneException(String message) {
        super(message);
    }
    public InvalidBeaconZoneException(String message, Exception exc) {
        super(message, exc);
    }
}
