package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/18/2016.
 */
public class RoomConfigNeighbors {
    public static final String TABLE_NAME = "roomNeighbors";

    public static final String KEY_SITE_ID = "siteId";
    public static final String KEY_ROOM_ID = "roomId";
    public static final String KEY_ROOM_NEIGHBOR_ID = "neighborId";


    private int siteId;
    private long roomId;
    private long neighborId;

    public RoomConfigNeighbors() {
    }

    public RoomConfigNeighbors(int siteId, long roomId, long neighborId) {
        this.siteId = siteId;
        this.roomId = roomId;
        this.neighborId = neighborId;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblMAP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_SITE_ID + " INTEGER," + KEY_ROOM_ID + " INTEGER, " + KEY_ROOM_NEIGHBOR_ID + " INTEGER )";
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

    public long getNeighborId() {
        return neighborId;
    }
}
