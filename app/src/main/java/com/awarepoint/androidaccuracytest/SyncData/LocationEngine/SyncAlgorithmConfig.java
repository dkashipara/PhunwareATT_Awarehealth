package com.awarepoint.androidaccuracytest.SyncData.LocationEngine;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.AlgorithmConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.AlgorithmConfigHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;
import com.awarepoint.androidaccuracytest.SyncData.SyncMapImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dkashipara on 8/15/2016.
 */
public class SyncAlgorithmConfig extends AsyncTask<Void, Integer, String> {
    int siteId;
    private String ApplianceIP;

    AlgorithmConfigHandler alconfConfigHandler;

    DatabaseManager databaseManager;
    MainActivity mainActivity;


    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";
    private String url_ble_algorithm_config = baseUrl + "/api/ble/algorithmConfig";

    public SyncAlgorithmConfig(MainActivity mActivity) {

        this.mainActivity = mActivity;
        this.databaseManager = mActivity.databaseManager;

        this.siteId = mActivity.databaseManager.getSiteId();
        this.ApplianceIP = mActivity.databaseManager.getApplianceIP();

        alconfConfigHandler = new AlgorithmConfigHandler(databaseManager);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.createSyncDialogProgress("Config5 SYNC");
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            //check if the areaId is already store
            return mainActivity.sh.makeServiceCall(url_ble_algorithm_config, ServiceHandler.GET);

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
                ParseJsonAlgoConfs parseJsonConfs = new ParseJsonAlgoConfs(jsonOutput);
                parseJsonConfs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(mainActivity, "Rest APIs not in Service or Timeout.. ", Toast.LENGTH_SHORT).show();
                mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
            }

        } else {
            Toast.makeText(mainActivity, "Rest APIs not in Service or Timeout.. ", Toast.LENGTH_SHORT).show();
            mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
        }
    }


    public class ParseJsonAlgoConfs extends AsyncTask<Void, Integer, Void> {
        String jsonOutput;

        public ParseJsonAlgoConfs(String jsonOutput) {
            this.jsonOutput = jsonOutput;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String formatJsonAlConf = "{AlgoConfig:" + jsonOutput + "}";
            if (!jsonOutput.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonOutput);

                    final JSONArray jsonArray = jsonObject.getJSONObject("_embedded").getJSONArray("items");
                    JSONArray configvalues = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject namevaluepair = jsonArray.getJSONObject(i);

                        JSONObject platform = namevaluepair.getJSONObject("platform");

                        if (platform.get("name").equals("Nexus_5X") && namevaluepair.get("configType").equals("SITE_DEFAULT"))
                            configvalues = namevaluepair.getJSONArray("configValues");
                    }
                    // final JSONArray jsonArray2 = jsonObject.getJSONArray("configValues");

                    //    final JSONArray jsonArrayforConfig = jsonArray.getJSONArray(2);
                    mainActivity.progressDialog.setMax(configvalues.length());
                    for (int i = 0; i < configvalues.length(); ++i) {
                        publishProgress(i);
                        JSONObject alconf = configvalues.getJSONObject(i);
                        JSONObject keyc = alconf.getJSONObject("key");
                        //      JSONObject valc = alconf.getJSONObject("value");
                        if (alconf.length() > 0) {
                            int confId = keyc.getInt("id");
                            int keyId = alconf.getInt("id");
                            String name = keyc.getString(AlgorithmConfig.KEY_NAME);
                            String keyType = keyc.getString(AlgorithmConfig.KEY_KEYTYPE);
                            String keyDescription = keyc.getString(AlgorithmConfig.KEY_KEYDESCRIPTION);
                            String keyValue = alconf.getString(AlgorithmConfig.KEY_VALUE);


                            alconfConfigHandler.addRecordAlgorithmConfig(new AlgorithmConfig(siteId, confId, keyId, name, keyType, keyDescription, keyValue));

                        }
                    }


                    //       }
                } catch (JSONException e1) {
                    mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
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
            mainActivity.fragmentHeader.loadSites(databaseManager);

            List<Area> areaFloorsList = mainActivity.databaseHandler.areaHandler.checkTableFloorArea();
            if (areaFloorsList.size() > 0) {
                SyncMapImage syncMapImage = new SyncMapImage(mainActivity);
                syncMapImage.executeSynAreaIdMapImage(areaFloorsList);
            }else{
                Log.d("SYNC","Sync meta data needs area data available");
            }
           /* Button btnSyncData = (Button) mainActivity.fragmentHeader.mfragmentView.findViewById(R.id.btn_sync_data);
            if (btnSyncData != null)
                btnSyncData.setEnabled(true);*/


        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mainActivity.progressDialog.setProgress(values[0]);
        }
    }
}
