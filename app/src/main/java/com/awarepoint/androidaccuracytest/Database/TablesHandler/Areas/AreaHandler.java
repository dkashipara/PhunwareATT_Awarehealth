package com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/17/2016.
 */
public class AreaHandler {

    DatabaseManager databaseManager;
    int siteId;

    public AreaHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    //Area table
    public boolean addRecordArea(Area area) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();


            tblValues.put(Area.KEY_AREA_SITE_ID, siteId);
            tblValues.put(Area.KEY_AREA_ID, area.getAreaId());
            tblValues.put(Area.KEY_AREA_FLOOR_INDEX, area.getFloorIndex());
            tblValues.put(Area.KEY_AREA_UTILIZATION_TYPE, area.getAreaUtilizationType());
            tblValues.put(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED, area.getAreaUtilizationTypeInherited());
            tblValues.put(Area.KEY_AREA_NAME, area.getName());
            tblValues.put(Area.KEY_AREA_PARENT_ID, area.getParentId());
            tblValues.put(Area.KEY_AREA_MIN_X, area.getMinX());
            tblValues.put(Area.KEY_AREA_MIN_Y, area.getMinY());
            tblValues.put(Area.KEY_AREA_MAX_X, area.getMaxX());
            tblValues.put(Area.KEY_AREA_MAX_Y, area.getMaxY());
            tblValues.put(Area.KEY_AREA_DELETED, area.getDeleted());
            tblValues.put(Area.KEY_AREA_TYPE, area.getType());
            tblValues.put(Area.KEY_AREA_CUTDOWN, area.getCutDown());
            tblValues.put(Area.KEY_AREA_TABLE_NAME, area.getTableName());


            Area checkArea = checkRecordArea(area.getAreaId());

            if (checkArea == null) {
                db.insert(Area.TABLE_NAME, null, tblValues);
            } else {
                db.update(Area.TABLE_NAME, tblValues, Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(area.getAreaId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert Area: " + area.getAreaId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert Area: " + area.getAreaId(), err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public Area checkRecordArea(long areaId) {
        Area tblArea = null;
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(Area.TABLE_NAME, null, Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(areaId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    boolean areaUtilizationTypeInherited = false;
                    boolean deleted = false;
                    boolean cutDown = false;


                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED)) == "true") {
                        areaUtilizationTypeInherited = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_DELETED)) == "true") {
                        deleted = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_CUTDOWN)) == "true") {
                        cutDown = true;
                    }


                    int floorIndex = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_FLOOR_INDEX));
                    int areaUtilizationType = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE));
                    String name = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));
                    int parentId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_PARENT_ID));
                    double minX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_X));
                    double minY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MIN_Y));
                    double maxX = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_X));
                    double maxY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_Y));

                    String type = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TYPE));
                    String tableName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TABLE_NAME));


                    tblArea = new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInherited, name, parentId
                            , minX, minY, maxX, maxY, deleted, type, cutDown, tableName);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordArea", err.getMessage());
            err.printStackTrace();
        }

        return tblArea;
    }


    public Area checkRecordAreaByName(String areaName) {
        Area tblArea = null;
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.query(Area.TABLE_NAME, null, Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_NAME + "=?"
                    , new String[]{String.valueOf(siteId), areaName}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    boolean areaUtilizationTypeInherited = false;
                    boolean deleted = false;
                    boolean cutDown = false;


                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED)) == "true") {
                        areaUtilizationTypeInherited = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_DELETED)) == "true") {
                        deleted = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_CUTDOWN)) == "true") {
                        cutDown = true;
                    }

                    long areaId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_ID));
                    int floorIndex = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_FLOOR_INDEX));
                    int areaUtilizationType = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE));
                    String name = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));
                    int parentId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_PARENT_ID));
                    double minX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_X));
                    double minY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MIN_Y));
                    double maxX = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_X));
                    double maxY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_Y));

                    String type = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TYPE));
                    String tableName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TABLE_NAME));


                    tblArea = new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInherited, name, parentId
                            , minX, minY, maxX, maxY, deleted, type, cutDown, tableName);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordAreabyName", err.getMessage());
            err.printStackTrace();
        }

        return tblArea;
    }


    public Area checkRecordAreaByParent(long parentId, String areaName) {
        Area tblArea = null;
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.query(Area.TABLE_NAME, null, Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_NAME + "=? AND " + Area.KEY_AREA_PARENT_ID + "=? "
                    , new String[]{String.valueOf(siteId), areaName, String.valueOf(parentId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    boolean areaUtilizationTypeInherited = false;
                    boolean deleted = false;
                    boolean cutDown = false;


                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED)) == "true") {
                        areaUtilizationTypeInherited = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_DELETED)) == "true") {
                        deleted = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_CUTDOWN)) == "true") {
                        cutDown = true;
                    }

                    long areaId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_ID));
                    int floorIndex = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_FLOOR_INDEX));
                    int areaUtilizationType = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE));
                    String name = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));
                    double minX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_X));
                    double minY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MIN_Y));
                    double maxX = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_X));
                    double maxY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_Y));

                    String type = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TYPE));
                    String tableName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TABLE_NAME));


                    tblArea = new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInherited, name, parentId
                            , minX, minY, maxX, maxY, deleted, type, cutDown, tableName);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordAreabyName", err.getMessage());
            err.printStackTrace();
        }

        return tblArea;
    }


    public Area checkRecordFloorByName(String campusName, String BuildingName, String floorName) {
        Area tblArea = null;
        try {
            long campusId = 0;
            long buildingId = 0;
            long floorId = 0;

            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Area campusArea = checkRecordAreaByName(campusName);
            if (campusArea != null)
                campusId = campusArea.getAreaId();

            Area buildingArea = checkRecordAreaByParent(campusId, BuildingName);
            if (buildingArea != null)
                buildingId = buildingArea.getAreaId();

            Area floorArea = checkRecordAreaByParent(buildingId, floorName);
            if (floorArea != null)
                floorId = floorArea.getAreaId();


            Cursor cursor = db.query(Area.TABLE_NAME, null, Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_ID + "=? AND "
                            + Area.KEY_AREA_PARENT_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(floorId), String.valueOf(buildingId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    boolean areaUtilizationTypeInherited = false;
                    boolean deleted = false;
                    boolean cutDown = false;


                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED)) == "true") {
                        areaUtilizationTypeInherited = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_DELETED)) == "true") {
                        deleted = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_CUTDOWN)) == "true") {
                        cutDown = true;
                    }

                    long areaId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_ID));
                    int floorIndex = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_FLOOR_INDEX));
                    int areaUtilizationType = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE));
                    String name = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));
                    int parentId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_PARENT_ID));
                    double minX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_X));
                    double minY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MIN_Y));
                    double maxX = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_X));
                    double maxY = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_MAX_Y));

                    String type = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TYPE));
                    String tableName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TABLE_NAME));


                    tblArea = new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInherited, name, parentId
                            , minX, minY, maxX, maxY, deleted, type, cutDown, tableName);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordAreabyName", err.getMessage());
            err.printStackTrace();
        }

        return tblArea;
    }


    public List<Area> checkTableArea() {
        List<Area> AreaArrayList = new ArrayList<Area>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + Area.TABLE_NAME + " where " + Area.KEY_AREA_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int siteId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_SITE_ID));
                    long areaId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_ID));
                    int floorIndex = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_FLOOR_INDEX));
                    int areaUtilizationType = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE));

                    String areaName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));
                    int parentId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_PARENT_ID));

                    double minX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_X));
                    double minY = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_Y));
                    double maxX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MAX_X));
                    double maxY = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MAX_Y));
                    String type = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TYPE));
                    String tableName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TABLE_NAME));

                    boolean areaUtilizationTypeInheritedBool = false;
                    boolean deletedBool = false;
                    boolean cutDownBool = false;


                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED)) == "true") {
                        areaUtilizationTypeInheritedBool = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_DELETED)) == "true") {
                        deletedBool = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_CUTDOWN)) == "true") {
                        cutDownBool = true;
                    }


                    Area area = new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInheritedBool, areaName, parentId
                            , minX, minY, maxX, maxY, deletedBool, type, cutDownBool, tableName);

                    AreaArrayList.add(area);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableArea", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableArea", err.getMessage());
            err.printStackTrace();
        }

        return AreaArrayList;
    }


    public List<Area> checkTableFloorArea() {
        List<Area> AreaArrayList = new ArrayList<Area>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + Area.TABLE_NAME + " where " + Area.KEY_AREA_SITE_ID + " =" + siteId + " AND " + Area.KEY_AREA_TYPE + "='FLOOR'", null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int siteId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_SITE_ID));
                    long areaId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_ID));
                    int floorIndex = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_FLOOR_INDEX));
                    int areaUtilizationType = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE));

                    String areaName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));
                    int parentId = cursor.getInt(cursor.getColumnIndex(Area.KEY_AREA_PARENT_ID));

                    double minX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_X));
                    double minY = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MIN_Y));
                    double maxX = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MAX_X));
                    double maxY = cursor.getDouble(cursor.getColumnIndex(Area.KEY_AREA_MAX_Y));
                    String type = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TYPE));
                    String tableName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_TABLE_NAME));

                    boolean areaUtilizationTypeInheritedBool = false;
                    boolean deletedBool = false;
                    boolean cutDownBool = false;


                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED)) == "true") {
                        areaUtilizationTypeInheritedBool = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_DELETED)) == "true") {
                        deletedBool = true;
                    }

                    if (cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_CUTDOWN)) == "true") {
                        cutDownBool = true;
                    }


                    Area area = new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInheritedBool, areaName, parentId
                            , minX, minY, maxX, maxY, deletedBool, type, cutDownBool, tableName);

                    AreaArrayList.add(area);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableArea", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableArea", err.getMessage());
            err.printStackTrace();
        }

        return AreaArrayList;
    }


    // Get the Area's names for a specific REGION TYPE
    public List<String> checkCampusForSite() {
        List<String> AreaArrayList = new ArrayList<String>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT NAME FROM " + Area.TABLE_NAME + " WHERE " + Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_TYPE + "='CAMPUS' ORDER BY NAME", new String[]{String.valueOf(siteId)});

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    String areaName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));

                    AreaArrayList.add(areaName);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableArea", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableArea", err.getMessage());
            err.printStackTrace();
        }

        return AreaArrayList;
    }


    // Get the Area's names for a specific REGION TYPE
    public List<String> checkBuildingsForCampus(String CampusName) {
        List<String> AreaArrayList = new ArrayList<String>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            String Query = "select " + Area.KEY_AREA_NAME + " from " + Area.TABLE_NAME + " where " + Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_PARENT_ID + " in ( SELECT " + Area.KEY_AREA_ID + " FROM " +
                    Area.TABLE_NAME + " where " + Area.KEY_AREA_NAME + "= ? and " + Area.KEY_AREA_TYPE + "='CAMPUS' )  and " + Area.KEY_AREA_TYPE + "='BUILDING' ORDER BY NAME";

            Cursor cursor = db.rawQuery(Query, new String[]{String.valueOf(siteId), CampusName});

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    String areaName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));

                    AreaArrayList.add(areaName);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableArea", sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.i("Error checkTableArea", err.getMessage());
            err.printStackTrace();
        }

        return AreaArrayList;
    }


    // Get the Area's names for a specific REGION TYPE
    public List<String> checkFloorsForBuilding(String BuildingName) {
        List<String> AreaArrayList = new ArrayList<String>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            String Query = "select " + Area.KEY_AREA_NAME + " from " + Area.TABLE_NAME + " where " + Area.KEY_AREA_SITE_ID + "=? AND " + Area.KEY_AREA_PARENT_ID + " in ( SELECT " + Area.KEY_AREA_ID + " FROM " +
                    Area.TABLE_NAME + " where " + Area.KEY_AREA_NAME + "= ? and " + Area.KEY_AREA_TYPE + "='BUILDING' )  and " + Area.KEY_AREA_TYPE + "='FLOOR' ORDER BY NAME";

            Cursor cursor = db.rawQuery(Query, new String[]{String.valueOf(siteId), BuildingName});

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    String areaName = cursor.getString(cursor.getColumnIndex(Area.KEY_AREA_NAME));

                    AreaArrayList.add(areaName);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableArea", sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.i("Error checkTableArea", err.getMessage());
            err.printStackTrace();
        }

        return AreaArrayList;
    }


    public void clearTableArea() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + Area.TABLE_NAME + " WHERE " + Area.KEY_AREA_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableArea " + Area.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }

}
