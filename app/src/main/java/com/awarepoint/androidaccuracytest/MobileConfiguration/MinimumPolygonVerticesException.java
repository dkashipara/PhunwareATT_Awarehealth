package com.awarepoint.androidaccuracytest.MobileConfiguration;

/**
 * Created by mspivey on 2/3/2016.
 * Copyright(c)2016 Awarepoint. All rights reserved.
 */

public class MinimumPolygonVerticesException extends Exception {
    public MinimumPolygonVerticesException() {
        super("Closed polygons need at least 2 vertices.");
    }
}
