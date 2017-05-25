package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/18/2016.
 */
public class RoomConfig {
    public static final String TABLE_NAME = "roomsConfig";

    public static final String KEY_ROOM_SITE_ID = "siteId";
    public static final String KEY_ROOM_ID = "id";
    public static final String KEY_ROOM_CATEGORY = "category";
    public static final String KEY_ROOM_HALLWAY_ID = "hallway";

    public static final String KEY_ROOM_NEIGHBOR_ROOMS = "firstOrderRooms";

    private int siteId;
    private long roomId;
    private String roomCategory;
    private long hallwayId;

    public RoomConfig() {
    }


    public RoomConfig(int siteId, long roomId, String roomCategory, long hallwayId) {
        this.siteId = siteId;
        this.roomId = roomId;
        this.roomCategory = roomCategory;
        this.hallwayId = hallwayId;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_ROOM_SITE_ID + " INTEGER, " + KEY_ROOM_ID + " INTEGER, " + KEY_ROOM_CATEGORY + " TEXT, " + KEY_ROOM_HALLWAY_ID + " INTEGER " +
                    ", PRIMARY KEY (" + KEY_ROOM_SITE_ID + "," + KEY_ROOM_ID + "))";
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


    public long getRoomId() {
        return roomId;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public long getHallwayId() {
        return hallwayId;
    }
}
