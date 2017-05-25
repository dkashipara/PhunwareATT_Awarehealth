package com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.MapMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/25/2016.
 */
public class MapMetadataHandler {

    DatabaseManager databaseManager;
    int siteId;

    public MapMetadataHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //Map table
    public boolean addRecordMapMetadata(MapMetadata mapMetadata) {

        try {

            SQLiteDatabase db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(MapMetadata.KEY_MAP_METADATA_SITE_ID, siteId);
            tblValues.put(MapMetadata.KEY_MAP_METADATA_AREA_ID, mapMetadata.getAreaId());
            tblValues.put(MapMetadata.KEY_MAP_METADATA_NATIVE_ZOOM, mapMetadata.getNativeZoom());
            tblValues.put(MapMetadata.KEY_MAP_METADATA_WIDTH, mapMetadata.getWidth());
            tblValues.put(MapMetadata.KEY_MAP_METADATA_HEIGHT, mapMetadata.getHeight());
            tblValues.put(MapMetadata.KEY_MAP_METADATA_LAST_START_TIME, mapMetadata.getLastTilingStartTime());
            tblValues.put(MapMetadata.KEY_MAP_METADATA_LAST_COMPLETE_TIME, mapMetadata.getLastTilingCompletedTime());


            MapMetadata checkMapMetadata = checkRecordMapMetadata(mapMetadata.getAreaId());

            if (checkMapMetadata == null) {
                db.insert(MapMetadata.TABLE_NAME, null, tblValues);
            } else {
                db.update(MapMetadata.TABLE_NAME, tblValues, MapMetadata.KEY_MAP_METADATA_SITE_ID + "=? AND " + MapMetadata.KEY_MAP_METADATA_AREA_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(mapMetadata.getAreaId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert MapMetadata: " + mapMetadata.getAreaId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert MapMetadata: " + mapMetadata.getAreaId(), err.getMessage());
            err.printStackTrace();
        }

        return false;
    }

    public MapMetadata checkRecordMapMetadata(long areaId) {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(MapMetadata.TABLE_NAME, null, MapMetadata.KEY_MAP_METADATA_SITE_ID + "=? AND " + MapMetadata.KEY_MAP_METADATA_AREA_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    return new MapMetadata(siteId, areaId, cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_NATIVE_ZOOM)), cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_WIDTH))
                            , cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_HEIGHT)), cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_LAST_START_TIME))
                            , cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_LAST_COMPLETE_TIME)));
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordMapMetadata", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<MapMetadata> checkTableMapMetadata() {
        List<MapMetadata> MapArrayList = new ArrayList<MapMetadata>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + MapMetadata.TABLE_NAME + " where " + MapMetadata.KEY_MAP_METADATA_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int siteId = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_SITE_ID));
                    long areaId = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_AREA_ID));
                    int nativeZoom = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_NATIVE_ZOOM));
                    int width = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_WIDTH));
                    int height = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_HEIGHT));
                    int lastTilingStartTime = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_LAST_START_TIME));
                    int lastTilingCompletedTime = cursor.getInt(cursor.getColumnIndex(MapMetadata.KEY_MAP_METADATA_LAST_COMPLETE_TIME));

                    MapMetadata mapMetadata = new MapMetadata(siteId, areaId, nativeZoom, width, height, lastTilingStartTime, lastTilingCompletedTime);

                    MapArrayList.add(mapMetadata);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableMapMetadata", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableMapMetadata", err.getMessage());
            err.printStackTrace();
        }

        return MapArrayList;
    }


    public void clearTableMapMetadata() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + MapMetadata.TABLE_NAME + " WHERE " + MapMetadata.KEY_MAP_METADATA_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableMapMetadata " + MapMetadata.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


}
