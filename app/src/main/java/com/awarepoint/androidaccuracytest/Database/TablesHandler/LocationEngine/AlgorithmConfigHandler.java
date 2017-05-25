package com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.AlgorithmConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkashipara on 8/12/2016.
 */
public class AlgorithmConfigHandler {
    DatabaseManager databaseManager;
    int siteId;

    public AlgorithmConfigHandler(DatabaseManager dbManager) {
        this.databaseManager = dbManager;
        this.siteId = dbManager.getSiteId();
    }

    public boolean addRecordAlgorithmConfig(AlgorithmConfig alConf) {
        SQLiteDatabase db = null;
        try {

            db = databaseManager.getWritableDatabase();
            ContentValues tblValues = new ContentValues();

            tblValues.put(AlgorithmConfig.KEY_CONFIG_SITE_ID, siteId);
            tblValues.put(AlgorithmConfig.KEY_CONF_ID, alConf.getConfId());
            tblValues.put(AlgorithmConfig.KEY_KEY_ID, alConf.getKeyId());
            tblValues.put(AlgorithmConfig.KEY_NAME, alConf.getName());
            tblValues.put(AlgorithmConfig.KEY_KEYTYPE, alConf.getKeyType());
            tblValues.put(AlgorithmConfig.KEY_KEYDESCRIPTION, alConf.getKeyDescription());
            tblValues.put(AlgorithmConfig.KEY_VALUE, alConf.getKeyValue());


            AlgorithmConfig checkalConf = checkRecordAlgoConfig(alConf.getConfId());

            if (checkalConf == null) {
                db.insert(AlgorithmConfig.TABLE_NAME, null, tblValues);
            } else {
                db.update(AlgorithmConfig.TABLE_NAME, tblValues, AlgorithmConfig.KEY_CONFIG_SITE_ID + "=? AND " + AlgorithmConfig.KEY_CONF_ID + "=?"
                        , new String[]{String.valueOf(siteId), String.valueOf(alConf.getConfId())});
            }

            return true;
        } catch (SQLException sqle) {
            Log.e("SQLException Insert AlgorithmConfig: " + alConf.getConfId(), sqle.getMessage());
            sqle.printStackTrace();
        } catch (Exception err) {
            Log.e("Exception Insert AlgorithmConfig: ", err.getMessage());
            err.printStackTrace();
        }
        return false;
    }


    public AlgorithmConfig checkRecordAlgoConfig(long algoConfId) {

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            Cursor cursor = db.query(AlgorithmConfig.TABLE_NAME, null, AlgorithmConfig.KEY_CONFIG_SITE_ID + "=? AND " + AlgorithmConfig.KEY_CONF_ID + "=?"
                    , new String[]{String.valueOf(siteId), String.valueOf(algoConfId)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    int confId = cursor.getInt(cursor.getColumnIndex(AlgorithmConfig.KEY_CONF_ID));
                    int keyId = cursor.getInt(cursor.getColumnIndex(AlgorithmConfig.KEY_KEY_ID));
                    String name = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_NAME));
                    String keyType = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_KEYTYPE));
                    String keyDescription = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_KEYDESCRIPTION));
                    String keyValue = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_VALUE));

                    return new AlgorithmConfig(siteId, confId, keyId, name, keyType, keyDescription, keyValue);
                }
            }

            cursor.close();
        } catch (Exception err) {
            Log.i("Error checkRecordAlgoConfig", err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    public List<AlgorithmConfig> checkTableAlgoConf() {
        List<AlgorithmConfig> algoConfigArrayList = new ArrayList<AlgorithmConfig>();

        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + AlgorithmConfig.TABLE_NAME + " where " + AlgorithmConfig.KEY_CONFIG_SITE_ID + " =" + siteId, null);

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    int confId = cursor.getInt(cursor.getColumnIndex(AlgorithmConfig.KEY_CONF_ID));
                    int keyId = cursor.getInt(cursor.getColumnIndex(AlgorithmConfig.KEY_KEY_ID));
                    String name = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_NAME));
                    String keyType = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_KEYTYPE));
                    String keyDescription = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_KEYDESCRIPTION));
                    String keyValue = cursor.getString(cursor.getColumnIndex(AlgorithmConfig.KEY_VALUE));

                    AlgorithmConfig alconf = new AlgorithmConfig(siteId, confId, keyId, name, keyType, keyDescription, keyValue);


                    algoConfigArrayList.add(alconf);
                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (SQLiteException sqle) {
            Log.i("Error checkTablealgorithmConfigs", sqle.getMessage());
        } catch (Exception err) {
            Log.i("Error checkTablealgorithmConfigs", err.getMessage());
            err.printStackTrace();
        }

        return algoConfigArrayList;
    }


    public void clearTableAlgoConf() {
        try {
            SQLiteDatabase db = databaseManager.getReadableDatabase();
            String deleteTable = "DELETE FROM " + AlgorithmConfig.TABLE_NAME + " WHERE " + AlgorithmConfig.KEY_CONFIG_SITE_ID + "=" + String.valueOf(siteId);
            db.execSQL(deleteTable);
        } catch (SQLException sqlerr) {
            Log.e("clearTable algorithmConfigs " + AlgorithmConfig.TABLE_NAME, sqlerr.getMessage());
            sqlerr.printStackTrace();
        }
    }
}
