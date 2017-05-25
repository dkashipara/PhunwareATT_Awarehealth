package com.awarepoint.androidaccuracytest.Database.Tables.Maps;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/25/2016.
 */
public class MapMetadata {
    public static final String TABLE_NAME = "MapMetadata";

    // Columns names
    public static final String KEY_MAP_METADATA_SITE_ID = "siteId";
    public static final String KEY_MAP_METADATA_AREA_ID = "areaId";
    public static final String KEY_MAP_METADATA_NATIVE_ZOOM = "nativeZoom";
    public static final String KEY_MAP_METADATA_WIDTH = "width";
    public static final String KEY_MAP_METADATA_HEIGHT = "height";
    public static final String KEY_MAP_METADATA_LAST_START_TIME = "lastTilingStartTime";
    public static final String KEY_MAP_METADATA_LAST_COMPLETE_TIME = "lastTilingCompletedTime";


    private int siteId;
    private long areaId;
    private int nativeZoom;
    private int width;
    private int height;
    private int lastTilingStartTime;
    private int lastTilingCompletedTime;

    public MapMetadata() {
    }

    public MapMetadata(int siteId, long areaId, int nativeZoom, int width, int height, int lastTilingStartTime, int lastTilingCompletedTime) {
        this.siteId = siteId;
        this.areaId = areaId;
        this.nativeZoom = nativeZoom;
        this.width = width;
        this.height = height;
        this.lastTilingStartTime = lastTilingStartTime;
        this.lastTilingCompletedTime = lastTilingCompletedTime;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_MAP_METADATA_SITE_ID + " INTEGER, " + KEY_MAP_METADATA_AREA_ID + " INTEGER, " + KEY_MAP_METADATA_NATIVE_ZOOM + " INTEGER " +
                    ", " + KEY_MAP_METADATA_WIDTH + " INTEGER, " + KEY_MAP_METADATA_HEIGHT + " INTEGER, " + KEY_MAP_METADATA_LAST_START_TIME + " INTEGER, " + KEY_MAP_METADATA_LAST_COMPLETE_TIME + " INTEGER, " +
                    " PRIMARY KEY (" + KEY_MAP_METADATA_SITE_ID + "," + KEY_MAP_METADATA_AREA_ID + "))";
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


    public int getSiteId() {
        return siteId;
    }

    public long getAreaId() {
        return areaId;
    }

    public int getNativeZoom() {
        return nativeZoom;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLastTilingStartTime() {
        return lastTilingStartTime;
    }

    public int getLastTilingCompletedTime() {
        return lastTilingCompletedTime;
    }
}
