package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/18/2016.
 */
public class FloorConfig {
    public static final String TABLE_NAME = "floorsConfig";

    public static final String KEY_FLOOR_SITE_ID = "siteId";
    public static final String MAP_KEY = "mapKey";
    public static final String KEY_FLOOR_ID = "id";
    public static final String KEY_FLOOR_SCALE = "scale";
    public static final String KEY_FLOOR_LATITUDE = "latitude";
    public static final String KEY_FLOOR_LONGITUDE = "longitude";
    public static final String KEY_FLOOR_LATITUDE_OFFSET = "x";
    public static final String KEY_FLOOR_LONGITUDE_OFFSET = "y";
    public static final String KEY_FLOOR_ROTATION_ANGLE = "angle";

    public static final String KEY_FLOOR_VERTICES = "vertices";
    public static final String KEY_FLOOR_HEIGHT = "height";
    public static final String KEY_FLOOR_LATNLONINFO = "latLngInfo";

    private int siteId;
    private long floorId;
    private float scale;
    private double latitude;
    private double longitude;
    private double latitudeOffset;
    private double longitudeOffset;
    private double rotationAngle;
    private long height;


    public FloorConfig() {
    }


    public FloorConfig(int siteId, long floorId, float scale, double latitude, double longitude, double latitudeOffset, double longitudeOffset, double rotationAngle, long height) {
        this.siteId = siteId;
        this.floorId = floorId;
        this.scale = scale;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitudeOffset = latitudeOffset;
        this.longitudeOffset = longitudeOffset;
        this.rotationAngle = rotationAngle;

        this.height = height;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_FLOOR_SITE_ID + " INTEGER, " + KEY_FLOOR_ID + " INTEGER, " + KEY_FLOOR_SCALE + " REAL, " + KEY_FLOOR_LATITUDE + " REAL, "
                    + KEY_FLOOR_LONGITUDE + " REAL, " + KEY_FLOOR_LATITUDE_OFFSET + " REAL, "
                    + KEY_FLOOR_LONGITUDE_OFFSET + " REAL, " + KEY_FLOOR_ROTATION_ANGLE + " REAL, " + KEY_FLOOR_HEIGHT + " INTEGER, "
                    + " PRIMARY KEY (" + KEY_FLOOR_SITE_ID + "," + KEY_FLOOR_ID + "))";
            db.execSQL(tblMAP);
        } catch (SQLException sqlerr) {
            Log.e("createTable: " + TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public void dropTable(SQLiteDatabase db) {
        try {
            String tblMAP = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(tblMAP);
        } catch (SQLException sqlerr) {
            Log.e("dropTable " + TABLE_NAME, sqlerr.getMessage());
        }
    }

    public long getFloorId() {
        return floorId;
    }

    public float getScale() {
        return scale;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitudeOffset() {
        return latitudeOffset;
    }

    public double getLongitudeOffset() {
        return longitudeOffset;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public long getHeight() {
        return height;
    }

}
