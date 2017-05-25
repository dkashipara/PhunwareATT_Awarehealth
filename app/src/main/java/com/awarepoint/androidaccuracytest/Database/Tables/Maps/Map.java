package com.awarepoint.androidaccuracytest.Database.Tables.Maps;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Blob;

/**
 * Created by ureyes on 3/18/2016.
 */
public class Map {
    public static final String TABLE_NAME = "Map";

    // Columns names
    public static final String KEY_MAP_SITE_ID = "siteId";
    public static final String KEY_MAP_AREA_ID = "areaId";
    public static final String KEY_MAP_GRAPHIC = "graphic";
    public static final String KEY_MAP_GRAPHIC_CHECKSUM = "graphicChecksum";
    public static final String KEY_MAP_SCALE = "scale";
    public static final String KEY_MAP_WIDTH = "width";
    public static final String KEY_MAP_HEIGHT = "height";


    private int siteId;
    private long areaId;
    private byte[] graphic;
    private int graphicChecksum;
    private double scale;
    private int width;
    private int height;

    public Map() {
    }

    public Map(int siteId, long areaId, byte[] graphic, double scale, int graphicChecksum, int width, int height) {
        this.siteId = siteId;
        this.areaId = areaId;
        this.graphic = graphic;
        this.graphicChecksum = graphicChecksum;
        this.scale = scale;
        this.width = width;
        this.height = height;

    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_MAP_SITE_ID + " INTEGER, " + KEY_MAP_AREA_ID + " INTEGER, " + KEY_MAP_GRAPHIC + " BLOB " +
                    ", " + KEY_MAP_GRAPHIC_CHECKSUM + " INTEGER, " + KEY_MAP_SCALE + " INTEGER, " + KEY_MAP_WIDTH + " INTEGER, " + KEY_MAP_HEIGHT + " INTEGER, " +
                    " PRIMARY KEY (" + KEY_MAP_SITE_ID + "," + KEY_MAP_AREA_ID + "))";
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

    public byte[] getGraphic() {
        return graphic;
    }

    public int getGraphicChecksum() {
        return graphicChecksum;
    }

    public double getScale() {
        return scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
