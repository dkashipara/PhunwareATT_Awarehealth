package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class FloorConfigHandler {

    DatabaseManager databaseManager;
    int siteId;

    public FloorConfigHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //FloorConfig table
    public boolean addRecordFloorConfig(FloorConfig floor) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(FloorConfig.KEY_FLOOR_SITE_ID, siteId);
            tblValues.put(FloorConfig.KEY_FLOOR_ID, floor.getFloorId());
            tblValues.put(FloorConfig.KEY_FLOOR_SCALE, floor.getScale());
            tblValues.put(FloorConfig.KEY_FLOOR_LATITUDE, floor.getLatitude());
            tblValues.put(FloorConfig.KEY_FLOOR_LONGITUDE, floor.getLongitude());
            tblValues.put(FloorConfig.KEY_FLOOR_LATITUDE_OFFSET, floor.getLatitudeOffset());
            tblValues.put(FloorConfig.KEY_FLOOR_LONGITUDE_OFFSET, floor.getLongitudeOffset());
            tblValues.put(FloorConfig.KEY_FLOOR_ROTATION_ANGLE, floor.getRotationAngle());
            tblValues.put(FloorConfig.KEY_FLOOR_HEIGHT, floor.getHeight());

            FloorConfig checkFloor = checkRecordFloorConfig(floor.getFloorId());

            if (checkFloor == null) {
                db.insert(FloorConfig.TABLE_NAME, null, tblValues);
            } else {
                db.update(FloorConfig.TABLE_NAME, tblValues, FloorConfig.KEY_FLOOR_SITE_ID + "=? AND " + FloorConfig.KEY_FLOOR_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(floor.getFloorId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert FloorConfig: " + floor.getFloorId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert FloorConfig: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public FloorConfig checkRecordFloorConfig(long floorId) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(FloorConfig.TABLE_NAME, null, FloorConfig.KEY_FLOOR_SITE_ID + "=? AND " + FloorConfig.KEY_FLOOR_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(floorId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    float scale = cursor.getFloat(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_SCALE));
                    double latitude = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LONGITUDE));
                    double latitudeOffset = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LATITUDE_OFFSET));
                    double longitudeOffset = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LONGITUDE_OFFSET));
                    double rotationAngle = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_ROTATION_ANGLE));
                    long height = cursor.getLong(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_HEIGHT));

                    return new FloorConfig(siteId, floorId, scale, latitude, longitude, latitudeOffset, longitudeOffset, rotationAngle, height);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordFloorConfig", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<FloorConfig> checkTableFloorConfig() {
        List<FloorConfig> FloorConfigArrayList = new ArrayList<FloorConfig>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + FloorConfig.TABLE_NAME + " where " + FloorConfig.KEY_FLOOR_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    long floorId = cursor.getLong(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_ID));
                    float scale = cursor.getFloat(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_SCALE));
                    double latitude = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LONGITUDE));
                    double latitudeOffset = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LATITUDE_OFFSET));
                    double longitudeOffset = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_LONGITUDE_OFFSET));
                    double rotationAngle = cursor.getDouble(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_ROTATION_ANGLE));
                    long height = cursor.getLong(cursor.getColumnIndex(FloorConfig.KEY_FLOOR_HEIGHT));

                    FloorConfig region = new FloorConfig(siteId, floorId, scale, latitude, longitude, latitudeOffset, longitudeOffset, rotationAngle, height);

                    FloorConfigArrayList.add(region);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableFloorConfig", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableFloorConfig", err.getMessage());
            err.printStackTrace();
        }

        return FloorConfigArrayList;
    }


    public void clearTableFloor() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + FloorConfig.TABLE_NAME + " WHERE " + FloorConfig.KEY_FLOOR_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableFloor " + FloorConfig.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
