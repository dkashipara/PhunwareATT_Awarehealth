package com.awarepoint.androidaccuracytest.MobileConfiguration;

/**
 * Created by mspivey on 2/3/2016.
 * Copyright(c) 2016 Awarepoint. All rights reserved.
 */

public class InvalidRoomCategoryException extends Exception {
    public InvalidRoomCategoryException(String message) {
        super(message);
    }
    public InvalidRoomCategoryException(String message, Exception exc) {
        super(message, exc);
    }
}
