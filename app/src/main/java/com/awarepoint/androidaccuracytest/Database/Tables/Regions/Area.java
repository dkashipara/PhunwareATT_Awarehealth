package com.awarepoint.androidaccuracytest.Database.Tables.Regions;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/17/2016.
 */
public class Area {
    public static final String TABLE_NAME = "Area";

    // Columns names
    public static final String KEY_AREA_SITE_ID = "siteId";
    public static final String KEY_AREA_ID = "id";
    public static final String KEY_AREA_FLOOR_INDEX = "floorIndex";
    public static final String KEY_AREA_UTILIZATION_TYPE = "areaUtilizationType";
    public static final String KEY_AREA_UTILIZATION_TYPE_INHERITED = "areaUtilizationTypeInherited";
    public static final String KEY_AREA_NAME = "name";
    public static final String KEY_AREA_PARENT_ID = "parentId";
    public static final String KEY_AREA_POINTS = "areaPoints";
    public static final String KEY_AREA_MIN_X = "minx";
    public static final String KEY_AREA_MIN_Y = "miny";
    public static final String KEY_AREA_MAX_X = "maxx";
    public static final String KEY_AREA_MAX_Y = "maxy";
    public static final String KEY_AREA_DELETED = "deleted";
    public static final String KEY_AREA_TYPE = "type";
    public static final String KEY_AREA_CUTDOWN = "cutDown";
    public static final String KEY_AREA_TABLE_NAME = "tableName";


    private long areaId;
    private int siteId;
    private int floorIndex;
    private int areaUtilizationType;
    private boolean areaUtilizationTypeInherited;
    private String name;
    private long parentId;

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private boolean deleted;
    private String type;
    private boolean cutDown;
    private String tableName;


    public Area() {
    }

    public Area(int siteId, long areaId, int floorIndex, int areaUtilizationType, boolean areaUtilizationTypeInherited, String name, long parentId
            , double minX, double minY, double maxX, double maxY, boolean deleted, String type, boolean cutDown, String tableName) {
        this.siteId = siteId;
        this.areaId = areaId;
        this.floorIndex = floorIndex;
        this.areaUtilizationType = areaUtilizationType;
        this.areaUtilizationTypeInherited = areaUtilizationTypeInherited;
        this.name = name;
        this.parentId = parentId;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.deleted = deleted;
        this.type = type;
        this.cutDown = cutDown;
        this.tableName = tableName;
    }


    public void createTable(SQLiteDatabase db) {

        try {
            String tblArea = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_AREA_SITE_ID + " INTEGER, " + KEY_AREA_ID + " INTEGER, " + KEY_AREA_FLOOR_INDEX + " INTEGER " +
                    ", " + KEY_AREA_UTILIZATION_TYPE + " INTEGER, " + KEY_AREA_UTILIZATION_TYPE_INHERITED + " TEXT, " + KEY_AREA_NAME + " TEXT " +
                    ", " + KEY_AREA_PARENT_ID + " INTEGER, " + KEY_AREA_MIN_X + " REAL " +
                    ", " + KEY_AREA_MIN_Y + " REAL, " + KEY_AREA_MAX_X + " REAL, " + KEY_AREA_MAX_Y + " REAL " +
                    ", " + KEY_AREA_DELETED + " TEXT, " + KEY_AREA_TYPE + " TEXT, " + KEY_AREA_CUTDOWN + " TEXT " +
                    ", " + KEY_AREA_TABLE_NAME + " TEXT, " +
                    "PRIMARY KEY (" + KEY_AREA_SITE_ID + "," + KEY_AREA_ID + "))";
            db.execSQL(tblArea);
        } catch (SQLException sqlerr) {
            Log.e("createTable: " + TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public void dropTable(SQLiteDatabase db) {
        try {
            String tblArea = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(tblArea);
        } catch (SQLException sqlerr) {
            Log.e("dropTable " + TABLE_NAME, sqlerr.getMessage());
        }
    }


    public long getAreaId() {
        return areaId;
    }

    public int getSiteId() {
        return siteId;
    }

    public int getFloorIndex() {
        return floorIndex;
    }

    public int getAreaUtilizationType() {
        return areaUtilizationType;
    }

    public boolean isAreaUtilizationTypeInherited() {
        return areaUtilizationTypeInherited;
    }

    public String getAreaUtilizationTypeInherited() {
        if (areaUtilizationTypeInherited)
            return "true";
        else
            return "false";

    }


    public String getName() {
        return name;
    }

    public long getParentId() {
        return parentId;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getDeleted() {
        if (deleted)
            return "true";
        else
            return "false";
    }

    public String getType() {
        return type;
    }

    public boolean isCutDown() {
        return cutDown;
    }

    public String getCutDown() {
        if (cutDown)
            return "true";
        else
            return "false";
    }


    public String getTableName() {
        return tableName;
    }
}
