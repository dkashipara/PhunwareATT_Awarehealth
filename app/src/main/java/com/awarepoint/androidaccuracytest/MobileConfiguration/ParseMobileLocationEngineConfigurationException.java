package com.awarepoint.androidaccuracytest.MobileConfiguration;

/**
 * Created by mspivey on 2/3/2016.
 * Copyright(c)2016 Awarepoint. All rights reserved.
 */

public class ParseMobileLocationEngineConfigurationException extends Exception {
    public ParseMobileLocationEngineConfigurationException() {
        super("Unsuccessful attempt at parsing JSON in to a configuration object. Check that the JSON is well formed.");
    }

    public ParseMobileLocationEngineConfigurationException(Exception exception) {
        super("Unsuccessful attempt at parsing JSON in to a configuration object. Check that the JSON is well formed.", exception);
    }

    public ParseMobileLocationEngineConfigurationException(Exception exception, String message) {
        super(message, exception);
    }

    public ParseMobileLocationEngineConfigurationException(String message) {
        super(message);
    }
}
