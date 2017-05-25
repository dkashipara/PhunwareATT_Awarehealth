package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class RoomConfigHandler {

    DatabaseManager databaseManager;
    int siteId;

    public RoomConfigHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //RoomConfig table
    public boolean addRecordRooms(RoomConfig room) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(RoomConfig.KEY_ROOM_SITE_ID, siteId);
            tblValues.put(RoomConfig.KEY_ROOM_ID, room.getRoomId());
            tblValues.put(RoomConfig.KEY_ROOM_CATEGORY, room.getRoomCategory());
            tblValues.put(RoomConfig.KEY_ROOM_HALLWAY_ID, room.getHallwayId());

            RoomConfig checkRoom = checkRecordRooms(room.getRoomId());

            if (checkRoom == null) {
                db.insert(RoomConfig.TABLE_NAME, null, tblValues);
            } else {
                db.update(RoomConfig.TABLE_NAME, tblValues, RoomConfig.KEY_ROOM_SITE_ID + "=? AND " + RoomConfig.KEY_ROOM_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(room.getRoomId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert RoomConfig: " + room.getRoomId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert RoomConfig: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public RoomConfig checkRecordRooms(long roomId) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(RoomConfig.TABLE_NAME, null, RoomConfig.KEY_ROOM_SITE_ID + "=? AND " + RoomConfig.KEY_ROOM_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(roomId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    String roomCategory = cursor.getString(cursor.getColumnIndex(RoomConfig.KEY_ROOM_CATEGORY));
                    long hallwayId = cursor.getLong(cursor.getColumnIndex(RoomConfig.KEY_ROOM_HALLWAY_ID));

                    return new RoomConfig(siteId, roomId, roomCategory, hallwayId);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordRooms", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<RoomConfig> checkTableRooms() {
        List<RoomConfig> roomConfigArrayList = new ArrayList<RoomConfig>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + RoomConfig.TABLE_NAME + " where " + RoomConfig.KEY_ROOM_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    long roomId = cursor.getLong(cursor.getColumnIndex(RoomConfig.KEY_ROOM_ID));
                    String roomCategory = cursor.getString(cursor.getColumnIndex(RoomConfig.KEY_ROOM_CATEGORY));
                    long hallwayId = cursor.getLong(cursor.getColumnIndex(RoomConfig.KEY_ROOM_HALLWAY_ID));


                    RoomConfig region = new RoomConfig(siteId, roomId, roomCategory, hallwayId);

                    roomConfigArrayList.add(region);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableRooms", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableRooms", err.getMessage());
            err.printStackTrace();
        }

        return roomConfigArrayList;
    }


    public void clearTableRooms() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + RoomConfig.TABLE_NAME + " WHERE " + RoomConfig.KEY_ROOM_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableRooms " + RoomConfig.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
