package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RegionConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class RegionConfigHandler {

    DatabaseManager databaseManager;
    int siteId;

    public RegionConfigHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //RegionConfig table
    public boolean addRecordRegions(RegionConfig region) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(RegionConfig.KEY_REGION_SITE_ID, siteId);
            tblValues.put(RegionConfig.KEY_REGION_ID, region.getRegionId());
            tblValues.put(RegionConfig.KEY_REGION_TYPE, region.getRegionType());
            tblValues.put(RegionConfig.KEY_REGION_PARENT_ID, region.getParentId());
            tblValues.put(RegionConfig.KEY_REGION_NAME, region.getRegionName());

            RegionConfig checkRegion = checkRecordRegions(region.getRegionId());

            if (checkRegion == null) {
                db.insert(RegionConfig.TABLE_NAME, null, tblValues);
            } else {
                db.update(RegionConfig.TABLE_NAME, tblValues, RegionConfig.KEY_REGION_SITE_ID + "=? AND " + RegionConfig.KEY_REGION_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(region.getRegionId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert RegionConfig: " + region.getRegionId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert RegionConfig: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public RegionConfig checkRecordRegions(long regionsId) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(RegionConfig.TABLE_NAME, null, RegionConfig.KEY_REGION_SITE_ID + "=? AND " + RegionConfig.KEY_REGION_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(regionsId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    String regionType = cursor.getString(cursor.getColumnIndex(RegionConfig.KEY_REGION_TYPE));
                    long parentId = cursor.getLong(cursor.getColumnIndex(RegionConfig.KEY_REGION_PARENT_ID));
                    String regionName = cursor.getString(cursor.getColumnIndex(RegionConfig.KEY_REGION_NAME));

                    return new RegionConfig(siteId, regionsId, regionType, parentId, regionName);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordRegions", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<RegionConfig> checkTableRegions() {
        List<RegionConfig> regionConfigArrayList = new ArrayList<RegionConfig>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + RegionConfig.TABLE_NAME + " where " + RegionConfig.KEY_REGION_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    long areaId = cursor.getLong(cursor.getColumnIndex(RegionConfig.KEY_REGION_ID));
                    String regionType = cursor.getString(cursor.getColumnIndex(RegionConfig.KEY_REGION_TYPE));
                    long parentId = cursor.getLong(cursor.getColumnIndex(RegionConfig.KEY_REGION_PARENT_ID));
                    String regionName = cursor.getString(cursor.getColumnIndex(RegionConfig.KEY_REGION_NAME));


                    RegionConfig region = new RegionConfig(siteId, areaId, regionType, parentId, regionName);

                    regionConfigArrayList.add(region);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableRegions", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableRegions", err.getMessage());
            err.printStackTrace();
        }

        return regionConfigArrayList;
    }


    public void clearTableRegions() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + RegionConfig.TABLE_NAME + " WHERE " + RegionConfig.KEY_REGION_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableRegions " + RegionConfig.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
