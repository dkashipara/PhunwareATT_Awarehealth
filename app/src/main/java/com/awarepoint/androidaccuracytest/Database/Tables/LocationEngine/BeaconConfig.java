package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 3/18/2016.
 */
public class BeaconConfig {
    public static final String TABLE_NAME = "beaconsConfig";

    public static final String KEY_BEACON_SITE_ID = "siteId";
    public static final String KEY_BEACON_ID = "id";
    public static final String KEY_BEACON_TYPE = "type";
    public static final String KEY_BEACON_ZONE = "zone";
    public static final String KEY_BEACON_POWER = "beaconPower";
    public static final String KEY_BEACON_INTERVAL = "beaconInterval";
    public static final String KEY_BEACON_X = "x";
    public static final String KEY_BEACON_Y = "y";
    public static final String KEY_BEACON_LAT = "latitude";
    public static final String KEY_BEACON_LONG = "longitude";
    public static final String KEY_BEACON_ROOM_ID = "areaId";
    public static final String KEY_BEACON_PAIRED_ID = "pairedPid";
    public static final String KEY_BEACON_FLOOR_ID = "floorId";
    public static final String KEY_BEACON_PLACED = "placed";

    private int siteId = 0;
    private long beaconId = 0;
    private int beaconType;
    private int beaconZone;
    private int beaconPower = 0;
    private int beaconInterval = 0;
    private Double beaconX = 0.0;
    private Double beaconY = 0.0;
    private Double beaconLat = 0.0;
    private Double beaconLong = 0.0;
    private long beaconRoomId = 0;
    private Long beaconPairedId = 0L;
    private long beaconFloorId = 0;

    public BeaconConfig() {
    }


    public BeaconConfig(int siteId, long beaconId, int beaconType, int beaconZone, int beaconPower, int beaconInterval
            , Double beaconX, Double beaconY, Double beaconLat, Double beaconLong, long beaconRoomId, Long beaconPairedId, long beaconFloorId) {
        this.siteId = siteId;
        this.beaconId = beaconId;
        //  this.beaconType =  BeaconType.getBeaconType(beaconType);
        //   this.beaconZone = BeaconZone.getBeaconZone(beaconZone);
        this.beaconType = beaconType;
        this.beaconZone = beaconZone;
        this.beaconPower = beaconPower;
        this.beaconInterval = beaconInterval;
        this.beaconX = beaconX;
        this.beaconY = beaconY;
        this.beaconLat = beaconLat;
        this.beaconLong = beaconLong;
        this.beaconRoomId = beaconRoomId;
        this.beaconPairedId = beaconPairedId;
        this.beaconFloorId = beaconFloorId;
    }

    private String getBeaconTypeFromint(int i) {

        return "stting";
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_BEACON_SITE_ID + " INTEGER, " + KEY_BEACON_ID + " INTEGER, " + KEY_BEACON_TYPE + " TEXT, " + KEY_BEACON_ZONE + " TEXT, "
                    + KEY_BEACON_POWER + " INTEGER, " + KEY_BEACON_INTERVAL + " INTEGER, " + KEY_BEACON_X + " REAL, " + KEY_BEACON_Y + " REAL, "
                    + KEY_BEACON_LAT + " REAL, " + KEY_BEACON_LONG + " REAL, " + KEY_BEACON_ROOM_ID + " INTEGER, "
                    + KEY_BEACON_PAIRED_ID + " INTEGER, " + KEY_BEACON_FLOOR_ID + " INTEGER, " +
                    " PRIMARY KEY (" + KEY_BEACON_SITE_ID + "," + KEY_BEACON_ID + "))";
            db.execSQL(tblCreate);
        } catch (SQLException sqlerr) {
            Log.e("createTable: " + TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public void dropTable(SQLiteDatabase db) {
        try {
            String tblMAP = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(tblMAP);
        } catch (SQLException sqlerr) {
            Log.e("dropTable " + TABLE_NAME, sqlerr.getMessage());
        }
    }


    public long getBeaconId() {
        return beaconId;
    }

    public int getBeaconType() {
        return beaconType;
    }

    public int getBeaconZone() {
        return beaconZone;
    }

    public int getBeaconPower() {
        return beaconPower;
    }

    public int getBeaconInterval() {
        return beaconInterval;
    }

    public Double getBeaconX() {
        return beaconX;
    }

    public Double getBeaconY() {
        return beaconY;
    }

    public Double getBeaconLat() {
        return beaconLat;
    }

    public Double getBeaconLong() {
        return beaconLong;
    }

    public long getBeaconRoomId() {
        return beaconRoomId;
    }

    public Long getBeaconPairedId() {
        return beaconPairedId;
    }

    public long getBeaconFloorId() {
        return beaconFloorId;
    }
}
