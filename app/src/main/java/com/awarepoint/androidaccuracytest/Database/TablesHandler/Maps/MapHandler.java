package com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/18/2016.
 */
public class MapHandler {

    DatabaseManager databaseManager;
    int siteId;

    public MapHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //Map table
    public boolean addRecordMap(Map map) {

        try {

            SQLiteDatabase db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(Map.KEY_MAP_SITE_ID, siteId);
            tblValues.put(Map.KEY_MAP_AREA_ID, map.getAreaId());
            tblValues.put(Map.KEY_MAP_GRAPHIC, map.getGraphic());
            tblValues.put(Map.KEY_MAP_GRAPHIC_CHECKSUM, map.getGraphicChecksum());
            tblValues.put(Map.KEY_MAP_SCALE, map.getScale());
            tblValues.put(Map.KEY_MAP_WIDTH, map.getWidth());
            tblValues.put(Map.KEY_MAP_HEIGHT, map.getHeight());


            Map checkMap = checkRecordMap(map.getAreaId());

            if (checkMap == null) {
                db.insert(Map.TABLE_NAME, null, tblValues);
            } else {
                db.update(Map.TABLE_NAME, tblValues, Map.KEY_MAP_SITE_ID + "=? AND " + Map.KEY_MAP_AREA_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(map.getAreaId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert Map: " + map.getAreaId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert Map: " + map.getAreaId(), err.getMessage());
            err.printStackTrace();
        }

        return false;
    }

    public Map checkRecordMap(long areaId) {
        Map tblMap = null;
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(Map.TABLE_NAME, null, Map.KEY_MAP_SITE_ID + "=? AND " + Map.KEY_MAP_AREA_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    byte[] imgByte = cursor.getBlob(2);
                    tblMap = new Map(siteId, areaId, imgByte, cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordMap", err.getMessage());
            err.printStackTrace();
        }

        return tblMap;
    }


    public List<Map> checkTableMap() {
        List<Map> MapArrayList = new ArrayList<Map>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + Map.TABLE_NAME + " where " + Map.KEY_MAP_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int siteId = cursor.getInt(cursor.getColumnIndex(Map.KEY_MAP_SITE_ID));
                    long areaId = cursor.getInt(cursor.getColumnIndex(Map.KEY_MAP_AREA_ID));
                    byte[] graphic = cursor.getBlob(cursor.getColumnIndex(Map.KEY_MAP_GRAPHIC));
                    int graphicChecksum = cursor.getInt(cursor.getColumnIndex(Map.KEY_MAP_GRAPHIC_CHECKSUM));
                    int scale = cursor.getInt(cursor.getColumnIndex(Map.KEY_MAP_SCALE));
                    int width = cursor.getInt(cursor.getColumnIndex(Map.KEY_MAP_WIDTH));
                    int height = cursor.getInt(cursor.getColumnIndex(Map.KEY_MAP_HEIGHT));


                    Map map = new Map(siteId, areaId, graphic, graphicChecksum, scale, width, height);

                    MapArrayList.add(map);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableMap", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableMap", err.getMessage());
            err.printStackTrace();
        }

        return MapArrayList;
    }


    public void clearTableMap() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + Map.TABLE_NAME + " WHERE " + Map.KEY_MAP_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableMap " + Map.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public Bitmap getImage(byte[] imgByte) {
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

}
