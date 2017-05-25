package com.awarepoint.androidaccuracytest.SyncData.LocationEngine;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.BeaconConfig;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.BeaconConfigHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ureyes on 4/29/2016.
 */
public class SyncBeaconConfig extends AsyncTask<Void, Integer, String> {

    int siteId;
    private String ApplianceIP;

    BeaconConfigHandler beaconConfigHandler;

    DatabaseManager databaseManager;
    MainActivity mainActivity;

    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";

    private String url_beacons = baseUrl + "/api/ble/beacons";

    public SyncBeaconConfig(MainActivity mActivity) {

        this.mainActivity = mActivity;
        this.databaseManager = mActivity.databaseManager;

        this.siteId = mActivity.databaseManager.getSiteId();
        this.ApplianceIP = mActivity.databaseManager.getApplianceIP();

        beaconConfigHandler = new BeaconConfigHandler(databaseManager);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.createSyncDialogProgress("Config1 SYNC");
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            //check if the areaId is already store
         //   return mainActivity.httpsServiceHandler.makeServiceCall(wsUrl, HttpServiceHandler.GET, null);
            return mainActivity.sh.makeServiceCall(url_beacons, ServiceHandler.GET);
        } catch (Exception err) {
            mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
            Log.e(mainActivity.TAG, err.getMessage());
            err.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String jsonOutput) {
        super.onPostExecute(jsonOutput);

        if (jsonOutput != null) {

            if (!jsonOutput.contains("<html>")) {
                ParseJsonBeacons parseJsonBeacons = new ParseJsonBeacons(jsonOutput);
                parseJsonBeacons.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(mainActivity, "Rest APIs not in Service or Timeout.. ", Toast.LENGTH_SHORT).show();
                mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
            }

        } else {
            Toast.makeText(mainActivity, "Rest APIs not in Service or Timeout.. ", Toast.LENGTH_SHORT).show();
            mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
        }
    }


    // Get a JSON with all the areas for the specified SiteId
    public class ParseJsonBeacons extends AsyncTask<Void, Integer, Void> {
        String jsonOutput;

        public ParseJsonBeacons(String jsonOutput) {
            this.jsonOutput = jsonOutput;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String formatJsonBeacons = "{Beacons:" + jsonOutput + "}";
            if (!jsonOutput.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonOutput);

                    final JSONArray jsonArray = jsonObject.getJSONObject("_embedded").getJSONArray("beacons");

                    mainActivity.progressDialog.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        publishProgress(i);
                        JSONObject beacon = jsonArray.getJSONObject(i);

                        if (beacon.length() > 0) {

                            long beaconId = beacon.getLong(BeaconConfig.KEY_BEACON_ID);
                            int beaconType = beacon.getInt(BeaconConfig.KEY_BEACON_TYPE);
                            int beaconZone = beacon.getInt(BeaconConfig.KEY_BEACON_ZONE);
                            //      int beaconPower = beacon.getInt(BeaconConfig.KEY_BEACON_POWER);
                            //     int beaconInterval = beacon.getInt(BeaconConfig.KEY_BEACON_INTERVAL);
                            Double beaconX = beacon.getDouble(BeaconConfig.KEY_BEACON_X);
                            Double beaconY = beacon.getDouble(BeaconConfig.KEY_BEACON_Y);
                            Double beaconLat = beacon.getDouble(BeaconConfig.KEY_BEACON_LAT);
                            Double beaconLong = beacon.getDouble(BeaconConfig.KEY_BEACON_LONG);
                            long beaconRoomId = beacon.getLong(BeaconConfig.KEY_BEACON_ROOM_ID);
                            Long beaconPairedId = beacon.getLong(BeaconConfig.KEY_BEACON_PAIRED_ID);
                            long beaconFloorId = beacon.getLong(BeaconConfig.KEY_BEACON_FLOOR_ID);


                            if (beacon.getBoolean(BeaconConfig.KEY_BEACON_PLACED)) {
                                beaconConfigHandler.addRecordBeacons(new BeaconConfig(siteId, beaconId, beaconType, beaconZone, 1, 1
                                        , beaconX, beaconY, beaconLat, beaconLong, beaconRoomId, beaconPairedId, beaconFloorId));


                                // here include

                            }
                        }
                    }


                    //       }
                } catch (JSONException e1) {
                    Log.e(mainActivity.TAG, e1.getMessage());
                    e1.printStackTrace();
                } catch (Exception e1) {
                    Log.e(mainActivity.TAG, e1.getMessage());
                    e1.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainActivity.progressDialog.dismiss();
            SyncFloorConfig syncflor =  new SyncFloorConfig(mainActivity);
            syncflor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

           /* Button btnSyncBeacons = (Button) mainActivity.fragmentHeader.mfragmentView.findViewById(R.id.btn_sync_beacons);
            if (btnSyncBeacons != null)
                btnSyncBeacons.setEnabled(true);*/
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mainActivity.progressDialog.setProgress(values[0]);
        }

    }
}
