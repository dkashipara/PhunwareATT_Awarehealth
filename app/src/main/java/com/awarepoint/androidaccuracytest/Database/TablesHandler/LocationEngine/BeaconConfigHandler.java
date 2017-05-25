package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.BeaconConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class BeaconConfigHandler {

    DatabaseManager databaseManager;
    int siteId;

    public BeaconConfigHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //BeaconConfig table
    public boolean addRecordBeacons(BeaconConfig beacon) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(BeaconConfig.KEY_BEACON_SITE_ID, siteId);
            tblValues.put(BeaconConfig.KEY_BEACON_ID, beacon.getBeaconId());
            tblValues.put(BeaconConfig.KEY_BEACON_TYPE, beacon.getBeaconType());
            tblValues.put(BeaconConfig.KEY_BEACON_ZONE, beacon.getBeaconZone());
            tblValues.put(BeaconConfig.KEY_BEACON_POWER, 1);
            tblValues.put(BeaconConfig.KEY_BEACON_INTERVAL, 1);
            tblValues.put(BeaconConfig.KEY_BEACON_X, beacon.getBeaconX());
            tblValues.put(BeaconConfig.KEY_BEACON_Y, beacon.getBeaconY());
            tblValues.put(BeaconConfig.KEY_BEACON_LAT, beacon.getBeaconLat());
            tblValues.put(BeaconConfig.KEY_BEACON_LONG, beacon.getBeaconLong());
            tblValues.put(BeaconConfig.KEY_BEACON_ROOM_ID, beacon.getBeaconRoomId());
            tblValues.put(BeaconConfig.KEY_BEACON_PAIRED_ID, beacon.getBeaconPairedId());
            tblValues.put(BeaconConfig.KEY_BEACON_FLOOR_ID, beacon.getBeaconFloorId());

            BeaconConfig checkRegion = checkRecordBeacons(beacon.getBeaconId());

            if (checkRegion == null) {
                db.insert(BeaconConfig.TABLE_NAME, null, tblValues);
            } else {
                db.update(BeaconConfig.TABLE_NAME, tblValues, BeaconConfig.KEY_BEACON_SITE_ID + "=? AND " + BeaconConfig.KEY_BEACON_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(beacon.getBeaconId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert BeaconConfig: " + beacon.getBeaconId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert BeaconConfig: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public BeaconConfig checkRecordBeacons(long beaconsId) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(BeaconConfig.TABLE_NAME, null, BeaconConfig.KEY_BEACON_SITE_ID + "=? AND " + BeaconConfig.KEY_BEACON_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(beaconsId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    long beaconId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_ID));
                    int beaconType = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_TYPE));
                    int beaconZone = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_ZONE));
                    int beaconPower = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_POWER));
                    int beaconInterval = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_INTERVAL));
                    Double beaconX = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_X));
                    Double beaconY = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_Y));
                    Double beaconLat = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_LAT));
                    Double beaconLong = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_LONG));
                    long beaconRoomId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_ROOM_ID));
                    Long beaconPairedId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_PAIRED_ID));
                    long beaconFloorId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_FLOOR_ID));


                    return new BeaconConfig(siteId, beaconId, beaconType, beaconZone, beaconPower, beaconInterval
                            , beaconX, beaconY, beaconLat, beaconLong, beaconRoomId, beaconPairedId, beaconFloorId);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordBeacons", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<BeaconConfig> checkTableBeacons() {
        List<BeaconConfig> beaconConfigArrayList = new ArrayList<BeaconConfig>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + BeaconConfig.TABLE_NAME + " where " + BeaconConfig.KEY_BEACON_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    long beaconId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_ID));
                    int beaconType = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_TYPE));
                    int beaconZone = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_ZONE));
                    int beaconPower = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_POWER));
                    int beaconInterval = cursor.getInt(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_INTERVAL));
                    Double beaconX = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_X));
                    Double beaconY = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_Y));
                    Double beaconLat = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_LAT));
                    Double beaconLong = cursor.getDouble(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_LONG));
                    long beaconRoomId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_ROOM_ID));
                    Long beaconPairedId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_PAIRED_ID));
                    long beaconFloorId = cursor.getLong(cursor.getColumnIndex(BeaconConfig.KEY_BEACON_FLOOR_ID));


                    BeaconConfig beacon = new BeaconConfig(siteId, beaconId, beaconType, beaconZone, beaconPower, beaconInterval
                            , beaconX, beaconY, beaconLat, beaconLong, beaconRoomId, beaconPairedId, beaconFloorId);


                    beaconConfigArrayList.add(beacon);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableBeacons", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableBeacons", err.getMessage());
            err.printStackTrace();
        }

        return beaconConfigArrayList;
    }


    public void clearTableBeacons() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + BeaconConfig.TABLE_NAME + " WHERE " + BeaconConfig.KEY_BEACON_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableBeacons " + BeaconConfig.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
