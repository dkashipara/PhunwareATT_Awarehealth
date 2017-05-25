package com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by dkashipara on 8/12/2016.
 */
public class AlgorithmConfig {
    public static final String TABLE_NAME = "algorithmConfigs";
    public static final String KEY_CONFIG_SITE_ID = "siteId";
    public static final String KEY_CONF_ID = "id";
    public static final String KEY_KEY_ID = "keyid";
    public static final String KEY_NAME = "name";
    public static final String KEY_KEYTYPE = "keyType";
    public static final String KEY_KEYDESCRIPTION = "description";
    public static final String KEY_VALUE = "value";


    private int siteId;
    private int confId;
    private int keyId;
    private String name;
    private String keyType;
    private String keyDescription;
    private String keyValue;

    public AlgorithmConfig() {
    }

    public AlgorithmConfig(int siteId, int confId, int keyId, String name, String keyType, String keyDescription, String keyValue) {
        this.siteId = siteId;
        this.confId = confId;
        this.keyId = keyId;
        this.name = name;
        this.keyType = keyType;
        this.keyDescription = keyDescription;
        this.keyValue = keyValue;
    }

    public int getSiteId() {
        return siteId;
    }

    public long getConfId() {
        return confId;
    }

    public int getKeyId() {
        return keyId;
    }

    public String getName() {
        return name;
    }

    public String getKeyType() {
        return keyType;
    }

    public String getKeyDescription() {
        return keyDescription;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_CONFIG_SITE_ID + " INTEGER, " + KEY_CONF_ID + " INTEGER, " + KEY_KEY_ID + " INTEGER, " + KEY_NAME + " TEXT, "
                    + KEY_KEYTYPE + " TEXT, " + KEY_KEYDESCRIPTION + " TEXT, " + KEY_VALUE + " TEXT " + ")";
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

}
