package com.awarepoint.androidaccuracytest.Database.TablesHandler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Sites;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 01/27/2016.
 */
public class SitesHandler {

    DatabaseManager databaseManager;
    String hostName;


    public SitesHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        hostName = dbManager.getApplianceIP();
    }

    //Sites table
    public boolean addRecordApplianceSites(Sites sites) {
        int siteId = 0;
        String siteName = "";
        try {
            siteId = sites.getSiteId();
            siteName = sites.getSiteName();

            SQLiteDatabase db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(Sites.KEY_HOST_NAME, hostName);
            tblValues.put(Sites.KEY_SITE_ID, siteId);
            tblValues.put(Sites.KEY_SITE_NAME, siteName);

            Sites checkApplianceSite = checkRecordApplianceSites(siteId);

            if (checkApplianceSite == null) {
                db.insert(Sites.TABLE_NAME, null, tblValues);
            } else {
                db.update(Sites.TABLE_NAME, tblValues, Sites.KEY_HOST_NAME + "=? AND " + Sites.KEY_SITE_ID + "=?", new String[]{hostName, siteName});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert Record: " + siteName, sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert Record: " + siteName, err.getMessage());
            err.printStackTrace();
        }

        return false;
    }

    public Sites checkRecordApplianceSites(int siteId) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(Sites.TABLE_NAME, new String[]{Sites.KEY_SITE_NAME}, Sites.KEY_HOST_NAME + "=? AND " + Sites.KEY_SITE_ID + " =?"
                    , new String[]{hostName, String.valueOf(siteId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    return new Sites(hostName, siteId, cursor.getString(0));
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordProvisionData", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public int checkRecordApplianceSiteId(String siteName) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(Sites.TABLE_NAME, new String[]{Sites.KEY_SITE_ID}, Sites.KEY_HOST_NAME + "=? AND " + Sites.KEY_SITE_NAME + " =?"
                    , new String[]{hostName, siteName}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    return cursor.getInt(0);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordProvisionData", err.getMessage());
            err.printStackTrace();
        }

        return 0;
    }

    public List<Sites> checkTableApplianceSites() {
        List<Sites> sitesArrayList = new ArrayList<Sites>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + Sites.TABLE_NAME + " WHERE " + Sites.KEY_HOST_NAME + "='" + hostName + "'", null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {
                    int siteId = cursor.getInt(cursor.getColumnIndex(Sites.KEY_SITE_ID));
                    String siteName = cursor.getString(cursor.getColumnIndex(Sites.KEY_SITE_NAME));

                    Sites applianceSite = new Sites(hostName, siteId, siteName);

                    sitesArrayList.add(applianceSite);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTableApplianceSites", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTableApplianceSites", err.getMessage());
            err.printStackTrace();
        }

        return sitesArrayList;
    }

    public void clearTableApplianceSites(String hostName) {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + Sites.TABLE_NAME + " WHERE " + Sites.KEY_HOST_NAME + "='" + hostName + "'";
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTableApplianceSites " + Sites.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }


}
