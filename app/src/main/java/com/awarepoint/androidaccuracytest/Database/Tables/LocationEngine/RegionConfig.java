package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/18/2016.
 */
public class RegionConfig {
    public static final String TABLE_NAME = "regionsConfig";

    public static final String KEY_REGION_SITE_ID = "siteId";
    public static final String KEY_REGION_ID = "id";
    public static final String KEY_REGION_TYPE = "type";
    public static final String KEY_REGION_PARENT_ID = "parentId";
    public static final String KEY_REGION_NAME = "name";

    public static final String KEY_REGION_VERTICES = "areaPoints";
    public static final String KEY_REGION_DELETED = "deleted";


    private int siteId;
    private long regionId;
    private String regionType;
    private long parentId;
    private String regionName;

    public RegionConfig() {
    }


    public RegionConfig(int siteId, long regionId, String regionType, long parentId, String regionName) {
        this.siteId = siteId;
        this.regionId = regionId;
        this.regionType = regionType;
        this.parentId = parentId;
        this.regionName = regionName;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_REGION_SITE_ID + " INTEGER, " + KEY_REGION_ID + " INTEGER, " + KEY_REGION_TYPE + " TEXT, " + KEY_REGION_PARENT_ID + " INTEGER " +
                    ", " + KEY_REGION_NAME + " TEXT, " +
                    " PRIMARY KEY (" + KEY_REGION_SITE_ID + "," + KEY_REGION_ID + "))";
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

    public String getRegionType() {
        return regionType;
    }

    public long getParentId() {
        return parentId;
    }

    public String getRegionName() {
        return regionName;
    }
}
