package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfigNeighbors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class RoomConfigNeighborsHandler {

    DatabaseManager databaseManager;
    int siteId;

    public RoomConfigNeighborsHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //RoomConfigNeighbors table
    public boolean addRecordRoomNeighbors(RoomConfigNeighbors roomConfigNeighbors) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(RoomConfigNeighbors.KEY_SITE_ID, siteId);
            tblValues.put(RoomConfigNeighbors.KEY_ROOM_ID, roomConfigNeighbors.getRoomId());
            tblValues.put(RoomConfigNeighbors.KEY_ROOM_NEIGHBOR_ID, roomConfigNeighbors.getNeighborId());

            db.insert(RoomConfigNeighbors.TABLE_NAME, null, tblValues);

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert RoomConfigNeighbors: " + roomConfigNeighbors.getRoomId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert RoomConfigNeighbors: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public List<RoomConfigNeighbors> checkTableRoomNeighbors(long roomId) {
        List<RoomConfigNeighbors> roomConfigNeighborsArrayList = new ArrayList<RoomConfigNeighbors>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + RoomConfigNeighbors.TABLE_NAME + " WHERE "
                            + RoomConfigNeighbors.KEY_SITE_ID + " =" + String.valueOf(siteId) + " AND "
                            + RoomConfigNeighbors.KEY_ROOM_ID + " =" + String.valueOf(roomId)
                    , null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    long neighborId = cursor.getLong(cursor.getColumnIndex(RoomConfigNeighbors.KEY_ROOM_NEIGHBOR_ID));

                    RoomConfigNeighbors region = new RoomConfigNeighbors(siteId, roomId, neighborId);

                    roomConfigNeighborsArrayList.add(region);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableRoomNeighbors", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableRoomNeighbors", err.getMessage());
            err.printStackTrace();
        }

        return roomConfigNeighborsArrayList;
    }


    public void clearTableRoomNeighbors() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + RoomConfigNeighbors.TABLE_NAME + " WHERE " + RoomConfigNeighbors.KEY_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableRoomNeighbors " + RoomConfigNeighbors.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
