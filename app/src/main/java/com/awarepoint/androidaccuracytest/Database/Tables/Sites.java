package com.awarepoint.androidaccuracytest.Database.Tables;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ureyes on 11/26/2015.
 */
public class Sites {
    public static final String TABLE_NAME = "Sites";

    // Columns names
    public static final String KEY_HOST_NAME = "hostname";
    public static final String KEY_SITE_ID = "id";
    public static final String KEY_SITE_NAME = "description";

    private String hostName;
    private int siteId;
    private String siteName;

    public Sites() {
    }

    public Sites(String hostName, int Id, String Name) {
        this.hostName = hostName;
        this.siteId = Id;
        this.siteName = Name;
    }

    public void createTable(SQLiteDatabase db) {

        try {
            String tblSites = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " + KEY_HOST_NAME + " TEXT, " + KEY_SITE_ID + " INTEGER , " + KEY_SITE_NAME + " TEXT, PRIMARY KEY (" + KEY_HOST_NAME + " , " + KEY_SITE_ID + ") )";
            db.execSQL(tblSites);
        } catch (SQLException sqlerr) {
            Log.e("createTable " + TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


    public void dropTable(SQLiteDatabase db) {
        try {
            String tblProvisionData = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(tblProvisionData);
        } catch (SQLException sqlerr) {
            Log.e("dropTable " + TABLE_NAME, sqlerr.getMessage());
        }
    }

    public int getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getHostName() {
        return hostName;
    }
}