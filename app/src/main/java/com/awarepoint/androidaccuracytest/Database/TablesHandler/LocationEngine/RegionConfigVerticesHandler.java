package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class RegionConfigVerticesHandler {

    DatabaseManager databaseManager;
    int siteId;

    public RegionConfigVerticesHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //RegionConfigVertices table
    public boolean addRecordRegionsVertices(RegionConfigVertices regionVertices) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_SITE_ID, siteId);
            tblValues.put(com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_REGION_ID, regionVertices.getRegionId());
            tblValues.put(com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_REGION_X, regionVertices.getX());
            tblValues.put(com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_REGION_Y, regionVertices.getY());

            db.insert(RegionConfigVertices.TABLE_NAME, null, tblValues);

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert RegionConfigVertices: " + regionVertices.getRegionId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert RegionConfigVertices: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public List<RegionConfigVertices> checkTableRegionsVertices(long regionId) {
        List<RegionConfigVertices> regionConfigVerticesArrayList = new ArrayList<RegionConfigVertices>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.TABLE_NAME + " WHERE "
                            + com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_SITE_ID + " =" + String.valueOf(siteId) + " AND "
                            + com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_REGION_ID + " =" + String.valueOf(regionId)
                    , null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    double x = cursor.getDouble(cursor.getColumnIndex(com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_REGION_X));
                    double y = cursor.getDouble(cursor.getColumnIndex(com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_REGION_Y));
                    RegionConfigVertices region = new RegionConfigVertices(siteId, regionId, x, y);

                    regionConfigVerticesArrayList.add(region);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableRegionsVertices", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableRegionsVertices", err.getMessage());
            err.printStackTrace();
        }

        return regionConfigVerticesArrayList;
    }


    public void clearTableRegionsVertices() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.TABLE_NAME + " WHERE " + com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.KEY_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableRegionsVertices " + com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfigVertices.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
