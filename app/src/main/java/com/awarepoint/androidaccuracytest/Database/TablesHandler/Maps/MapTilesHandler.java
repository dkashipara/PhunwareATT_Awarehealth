package com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.MapTiles;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/18/2016.
 */
public class MapTilesHandler {


    DatabaseManager databaseManager;
    int siteId;

    public MapTilesHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = databaseManager.getSiteId();
    }

    //Map table
    public boolean addRecordMapTiles(MapTiles mapTiles) {

        try {

            SQLiteDatabase db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(MapTiles.KEY_MAP_SITE_ID, siteId);
            tblValues.put(MapTiles.KEY_MAP_AREA_ID, mapTiles.getAreaId());
            tblValues.put(MapTiles.KEY_MAP_GRAPHIC, mapTiles.getGraphic());
            tblValues.put(MapTiles.KEY_MAP_ZOOM_LEVEL, mapTiles.getZoomLevel());
            tblValues.put(MapTiles.KEY_MAP_X, mapTiles.getX());
            tblValues.put(MapTiles.KEY_MAP_Y, mapTiles.getY());


            MapTiles checkMapTiles = checkRecordSpecificMapTiles(mapTiles.getAreaId(), mapTiles.getZoomLevel(), mapTiles.getX(), mapTiles.getY());

            if (checkMapTiles == null) {
                db.insert(MapTiles.TABLE_NAME, null, tblValues);
            } else {
                db.update(MapTiles.TABLE_NAME, tblValues, MapTiles.KEY_MAP_SITE_ID + "=? AND " + MapTiles.KEY_MAP_AREA_ID + "=? AND " + MapTiles.KEY_MAP_ZOOM_LEVEL + "=? AND "
                                + MapTiles.KEY_MAP_X + "=? AND " + MapTiles.KEY_MAP_Y + "=? "
                        , new String[]{String.valueOf(mapTiles.getSiteId()), String.valueOf(mapTiles.getAreaId()), String.valueOf(mapTiles.getZoomLevel())
                                , String.valueOf(mapTiles.getX()), String.valueOf(mapTiles.getY())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert MapTiles: " + mapTiles.getAreaId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert MapTiles: " + mapTiles.getAreaId(), err.getMessage());
            err.printStackTrace();
        }

        return false;
    }


    public List<MapTiles> checkMapTilesForAreaIdNativeZoom(long areaId, int zoomLevel) {
        List<MapTiles> mapTilesList = new ArrayList<>();
        try {

            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(MapTiles.TABLE_NAME, null, MapTiles.KEY_MAP_SITE_ID + "=? AND " + MapTiles.KEY_MAP_AREA_ID + "=? AND "
                            + MapTiles.KEY_MAP_ZOOM_LEVEL + "=? "
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId), String.valueOf(zoomLevel)}, null, null, null);


            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(MapTiles.KEY_MAP_GRAPHIC));
                    int x = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_X));
                    int y = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_Y));

                    mapTilesList.add(new MapTiles(siteId, areaId, imgByte, zoomLevel, x, y));

                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordMapTiles", err.getMessage());
            err.printStackTrace();
        }

        return mapTilesList;
    }


    public List<MapTiles> checkMapTilesForAreaId(long areaId) {
        List<MapTiles> mapTilesList = new ArrayList<>();
        try {

            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(MapTiles.TABLE_NAME, null, MapTiles.KEY_MAP_SITE_ID + "=? AND " + MapTiles.KEY_MAP_AREA_ID + "=? "
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId)}, null, null, null);


            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(MapTiles.KEY_MAP_GRAPHIC));
                    int zoomLevel = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_ZOOM_LEVEL));
                    int x = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_X));
                    int y = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_Y));

                    mapTilesList.add(new MapTiles(siteId, areaId, imgByte, zoomLevel, x, y));

                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordMapTiles", err.getMessage());
            err.printStackTrace();
        }

        return mapTilesList;
    }

    public List<Integer> checkMaxMapTilesPositionForAreaId(long areaId, int zoomLevel) {
        List<Integer> maxList = new ArrayList<>();
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT MAX( " + MapTiles.KEY_MAP_X + ") as maxX, MAX( " + MapTiles.KEY_MAP_Y + ") as maxY FROM " + MapTiles.TABLE_NAME +
                            " WHERE " + MapTiles.KEY_MAP_SITE_ID + " = " + String.valueOf(siteId) + " AND " +
                            MapTiles.KEY_MAP_AREA_ID + "= " + String.valueOf(areaId) + " AND " + MapTiles.KEY_MAP_ZOOM_LEVEL + "= " + String.valueOf(zoomLevel)
                    , null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    int maxX = cursor.getInt(cursor.getColumnIndex("maxX"));
                    int maxY = cursor.getInt(cursor.getColumnIndex("maxY"));

                    maxList.add(maxX);
                    maxList.add(maxY);

                    cursor.close();
                }
            }

        } catch (Exception err) {
            Log.i("Error checkRecordMapTiles", err.getMessage());
            err.printStackTrace();
        }

        return maxList;
    }


    public MapTiles checkRecordSpecificMapTiles(long areaId, int zoomLevel, int x, int y) {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(MapTiles.TABLE_NAME, null, MapTiles.KEY_MAP_SITE_ID + "=? AND " + MapTiles.KEY_MAP_AREA_ID + "=? AND "
                            + MapTiles.KEY_MAP_ZOOM_LEVEL + "=? AND " + MapTiles.KEY_MAP_X + "=? AND " + MapTiles.KEY_MAP_Y + "=? "
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId), String.valueOf(zoomLevel), String.valueOf(x), String.valueOf(y)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(MapTiles.KEY_MAP_GRAPHIC));
                    return new MapTiles(siteId, areaId, imgByte, cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_ZOOM_LEVEL)), x, y);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordMapTiles", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<MapTiles> checkTableMapTiles() {
        List<MapTiles> MapTilesArrayList = new ArrayList<MapTiles>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + MapTiles.TABLE_NAME + " where " + MapTiles.KEY_MAP_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int siteId = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_SITE_ID));
                    long areaId = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_AREA_ID));
                    byte[] graphic = cursor.getBlob(cursor.getColumnIndex(MapTiles.KEY_MAP_GRAPHIC));
                    int zoomLevel = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_ZOOM_LEVEL));
                    int x = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_X));
                    int y = cursor.getInt(cursor.getColumnIndex(MapTiles.KEY_MAP_Y));

                    MapTilesArrayList.add(new MapTiles(siteId, areaId, graphic, zoomLevel, x, y));
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableMapTiles", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableMapTiles", err.getMessage());
            err.printStackTrace();
        }

        return MapTilesArrayList;
    }


    public void clearTableMapTiles() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + MapTiles.TABLE_NAME + " WHERE " + MapTiles.KEY_MAP_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableMapTiles " + MapTiles.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

    public byte[] convertBitmapToBytes(Bitmap mapTile) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mapTile.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] graphicByte = stream.toByteArray();

        return graphicByte;
    }

}
