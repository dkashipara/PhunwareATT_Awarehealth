package com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.AreaPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class AreaPointHandler {

    DatabaseManager databaseManager;
    int siteId;

    public AreaPointHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //AreaPoint table
    public boolean addRecordAreaPoint(AreaPoint areaPoint) {

        try {

            SQLiteDatabase db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(AreaPoint.KEY_AREA_SITE_ID, siteId);
            tblValues.put(AreaPoint.KEY_AREA_ID, areaPoint.getAreaId());
            tblValues.put(AreaPoint.KEY_AREA_X, areaPoint.getX());
            tblValues.put(AreaPoint.KEY_AREA_Y, areaPoint.getY());
            tblValues.put(AreaPoint.KEY_AREA_POLY_INDEX, areaPoint.getPolyIndex());

            db.insert(AreaPoint.TABLE_NAME, null, tblValues);

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert AreaPoint: " + areaPoint.getAreaId(), sqle.getMessage());
            sqle.printStackTrace();
            return false;
        } catch (Exception err) {
            Log.e("Exception Insert AreaPoint: " + areaPoint.getAreaId(), err.getMessage());
            err.printStackTrace();
            return false;
        }


    }

    public AreaPoint checkRecordAreaPoint(long areaId) {
        AreaPoint tblAreaPoint = null;
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(AreaPoint.TABLE_NAME, null, AreaPoint.KEY_AREA_SITE_ID + "=? AND " + AreaPoint.KEY_AREA_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    tblAreaPoint = new AreaPoint(siteId, areaId, cursor.getDouble(cursor.getColumnIndex(AreaPoint.KEY_AREA_X))
                            , cursor.getDouble(cursor.getColumnIndex(AreaPoint.KEY_AREA_Y)), cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_POLY_INDEX)));
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordAreaPoint", err.getMessage());
            err.printStackTrace();
        }

        return tblAreaPoint;
    }


    public List<AreaPoint> checkTableAreaPointForAreaId(long areaId) {
        List<AreaPoint> areaPointArrayList = new ArrayList<AreaPoint>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + AreaPoint.TABLE_NAME + " where " + AreaPoint.KEY_AREA_SITE_ID + " =" + siteId
                    + " AND " + AreaPoint.KEY_AREA_ID + "=" + areaId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    double areaX = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_X));
                    double areaY = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_Y));
                    int arePolyIndex = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_POLY_INDEX));


                    AreaPoint areaPoint = new AreaPoint(siteId, areaId, areaX, areaY, arePolyIndex);

                    areaPointArrayList.add(areaPoint);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableAreaPoint", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableAreaPoint", err.getMessage());
            err.printStackTrace();
        }

        return areaPointArrayList;
    }


    public List<AreaPoint> checkTableAreaPoint() {
        List<AreaPoint> AreaPointArrayList = new ArrayList<AreaPoint>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + AreaPoint.TABLE_NAME + " where " + AreaPoint.KEY_AREA_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int siteId = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_SITE_ID));
                    long areaId = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_ID));
                    double areaX = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_X));
                    double areaY = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_Y));
                    int arePolyIndex = cursor.getInt(cursor.getColumnIndex(AreaPoint.KEY_AREA_POLY_INDEX));


                    AreaPoint areaPoint = new AreaPoint(siteId, areaId, areaX, areaY, arePolyIndex);

                    AreaPointArrayList.add(areaPoint);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableAreaPoint", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableAreaPoint", err.getMessage());
            err.printStackTrace();
        }

        return AreaPointArrayList;
    }


    public void clearTableAreaPoint() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + AreaPoint.TABLE_NAME + " WHERE " + AreaPoint.KEY_AREA_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableAreaPoint " + AreaPoint.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
