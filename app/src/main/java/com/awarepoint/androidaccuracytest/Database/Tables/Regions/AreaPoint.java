package com.awarepoint.androidaccuracytest.Database.Tables.Regions;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/17/2016.
 */
public class AreaPoint {

    public static final String TABLE_NAME = "AreaPoint";

    // Columns names
    public static final String KEY_AREA_SITE_ID = "siteId";
    public static final String KEY_AREA_ID = "areaId";
    public static final String KEY_AREA_X = "x";
    public static final String KEY_AREA_Y = "y";
    public static final String KEY_AREA_POLY_INDEX = "polyIndex";

    private int siteId;
    private long areaId;
    private double X;
    private double Y;
    private int polyIndex;


    public AreaPoint() {
    }

    public AreaPoint(int siteId, long areaId, double x, double y, int polyIndex) {
        this.siteId = siteId;
        this.areaId = areaId;
        X = x;
        Y = y;
        this.polyIndex = polyIndex;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblAreaPoints = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_AREA_SITE_ID + " INTEGER, " + KEY_AREA_ID + " INTEGER " +
                    ", " + KEY_AREA_X + " REAL, " + KEY_AREA_Y + " REAL, " + KEY_AREA_POLY_INDEX + " INTEGER )";
            db.execSQL(tblAreaPoints);
        } catch (SQLException sqlerr) {
            Log.e("createTable: " + TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public void dropTable(SQLiteDatabase db) {
        try {
            String tblAreaPoints = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(tblAreaPoints);
        } catch (SQLException sqlerr) {
            Log.e("dropTable " + TABLE_NAME, sqlerr.getMessage());
        }
    }


    public int getSiteId() {
        return siteId;
    }

    public long getAreaId() {
        return areaId;
    }


    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getPolyIndex() {
        return polyIndex;
    }
}
