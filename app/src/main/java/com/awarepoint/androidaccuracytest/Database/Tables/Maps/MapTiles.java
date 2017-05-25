package com.awarepoint.androidaccuracytest.Database.Tables.Maps;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/25/2016.
 */
public class MapTiles {
    public static final String TABLE_NAME = "MapTiles";

    // Columns names
    public static final String KEY_MAP_SITE_ID = "siteId";
    public static final String KEY_MAP_AREA_ID = "areaId";
    public static final String KEY_MAP_GRAPHIC = "graphic";
    public static final String KEY_MAP_ZOOM_LEVEL = "zoomLevel";
    public static final String KEY_MAP_X = "x";
    public static final String KEY_MAP_Y = "y";


    private int siteId;
    private long areaId;
    private byte[] graphic;
    private int zoomLevel;
    private int x;
    private int y;


    public MapTiles() {
    }

    public MapTiles(int siteId, long areaId, byte[] graphic, int zoomLevel, int x, int y) {
        this.siteId = siteId;
        this.areaId = areaId;
        this.graphic = graphic;
        this.zoomLevel = zoomLevel;
        this.x = x;
        this.y = y;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMapTiles = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_MAP_SITE_ID + " INTEGER, " + KEY_MAP_AREA_ID + " INTEGER, " + KEY_MAP_GRAPHIC + " BLOB " +
                    ", " + KEY_MAP_ZOOM_LEVEL + " INTEGER, " + KEY_MAP_X + " INTEGER, " + KEY_MAP_Y + " INTEGER ) ";
            db.execSQL(tblMapTiles);
        } catch (SQLException sqlerr) {
            Log.e("createTable: " + TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public void dropTable(SQLiteDatabase db) {
        try {
            String tblMapTiles = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(tblMapTiles);
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

    public byte[] getGraphic() {
        return graphic;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
