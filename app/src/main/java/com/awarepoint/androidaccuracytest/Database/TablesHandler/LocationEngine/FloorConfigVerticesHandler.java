package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfigVertices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class FloorConfigVerticesHandler {

    DatabaseManager databaseManager;
    int siteId;

    public FloorConfigVerticesHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //FloorConfigVertices table
    public boolean addRecordFloorsVertices(FloorConfigVertices floorVertices) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(FloorConfigVertices.KEY_SITE_ID, siteId);
            tblValues.put(FloorConfigVertices.KEY_FLOOR_ID, floorVertices.getFloorId());
            tblValues.put(FloorConfigVertices.KEY_FLOOR_X, floorVertices.getX());
            tblValues.put(FloorConfigVertices.KEY_FLOOR_Y, floorVertices.getY());

            db.insert(FloorConfigVertices.TABLE_NAME, null, tblValues);

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert FloorConfigVertices: " + floorVertices.getFloorId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert FloorConfigVertices: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public List<FloorConfigVertices> checkTableFloorsVertices(long floorId) {
        List<FloorConfigVertices> FloorConfigVerticesArrayList = new ArrayList<FloorConfigVertices>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + FloorConfigVertices.TABLE_NAME + " WHERE "
                            + FloorConfigVertices.KEY_SITE_ID + " =" + String.valueOf(siteId) + " AND "
                            + FloorConfigVertices.KEY_FLOOR_ID + " =" + String.valueOf(floorId)
                    , null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    double x = cursor.getDouble(cursor.getColumnIndex(FloorConfigVertices.KEY_FLOOR_X));
                    double y = cursor.getDouble(cursor.getColumnIndex(FloorConfigVertices.KEY_FLOOR_Y));
                    FloorConfigVertices region = new FloorConfigVertices(siteId, floorId, x, y);

                    FloorConfigVerticesArrayList.add(region);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableFloorsVertices", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableFloorsVertices", err.getMessage());
            err.printStackTrace();
        }

        return FloorConfigVerticesArrayList;
    }


    public void clearTableFloorsVertices() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + FloorConfigVertices.TABLE_NAME + " WHERE " + FloorConfigVertices.KEY_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableFloorsVertices " + FloorConfigVertices.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
