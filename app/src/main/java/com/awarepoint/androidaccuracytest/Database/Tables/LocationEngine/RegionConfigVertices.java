package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/18/2016.
 */
public class RegionConfigVertices {
    public static final String TABLE_NAME = "regionVertices";

    public static final String KEY_SITE_ID = "siteId";
    public static final String KEY_REGION_ID = "regionId";
    public static final String KEY_REGION_X = "x";
    public static final String KEY_REGION_Y = "y";


    private int siteId;
    private long regionId;
    private double x;
    private double y;

    public RegionConfigVertices() {
    }

    public RegionConfigVertices(int siteId, long regionId, double x, double y) {
        this.siteId = siteId;
        this.regionId = regionId;
        this.x = x;
        this.y = y;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_SITE_ID + " INTEGER," + KEY_REGION_ID + " INTEGER," + KEY_REGION_X + " REAL, " + KEY_REGION_Y + " REAL )";
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


    public long getRegionId() {
        return regionId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
