package com.awarepoint.androidaccuracytest.SyncData;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.AreaPoint;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas.AreaHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas.AreaPointHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;
import com.awarepoint.androidaccuracytest.SyncData.LocationEngine.SyncAlgorithmConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ureyes on 3/28/2016.
 * https://10.6.2.91/16/sysman/maps/1183586
 */
public class SyncAreas extends AsyncTask<Void, Integer, String> {
    AreaHandler areaHandler;
    AreaPointHandler areaPointHandler;
    int siteId;

    DatabaseManager databaseManager;
    MainActivity mainActivity;

    private String ApplianceIP;

    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";
    private String url_regions = baseUrl + "/api/ble/areas";


    public SyncAreas(MainActivity mActivity) {
        this.mainActivity = mActivity;
        this.databaseManager = mActivity.databaseManager;

        this.siteId = mActivity.databaseManager.getSiteId();
        this.ApplianceIP = mActivity.databaseManager.getApplianceIP();
        this.areaHandler = new AreaHandler(databaseManager);
        this.areaPointHandler = new AreaPointHandler(databaseManager);
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
            return  mainActivity.sh.makeServiceCall(url_regions, ServiceHandler.GET);
        //   return mainActivity.httpsServiceHandler.makeServiceCall(AreaUrl, HttpServiceHandler.GET, null);

        } catch (Exception err) {
            mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
            Log.e(mainActivity.TAG, err.getMessage());
            err.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String jsonAreas) {
        super.onPostExecute(jsonAreas);

        if (jsonAreas != null) {

            if (!jsonAreas.contains("DOCTYPE")) {
                ParseJsonAreas parseJsonAreas = new ParseJsonAreas(jsonAreas);
                parseJsonAreas.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    public class ParseJsonAreas extends AsyncTask<Void, Integer, Void> {
        String areasJson;

        public ParseJsonAreas(String areasJson) {
            this.areasJson = areasJson;
        }

        @Override
        protected Void doInBackground(Void... params) {

            String formatJsonArea = "{Areas:" + areasJson + "}";

            if (!areasJson.isEmpty()) {
                try {
                   // JSONObject jsonObject = new JSONObject(formatJsonArea);
                    JSONObject jsonObject = new JSONObject(areasJson);
                 //   final JSONArray areas = jsonObject.getJSONArray("Areas");

                    final JSONArray areas = jsonObject.getJSONObject("_embedded").getJSONArray("bleAreas");
                    mainActivity.progressDialog.setMax(areas.length());
                  //  JSONArray regions = jsonObject.getJSONObject(EMBEDDED).getJSONArray(AREAS);
                    for (int i = 0; i < areas.length(); i++) {
                        publishProgress(i);

                        JSONObject area = areas.getJSONObject(i);

                        long areaId = area.getInt(Area.KEY_AREA_ID);
                        int floorIndex = area.getInt(Area.KEY_AREA_FLOOR_INDEX);
                        int areaUtilizationType = area.getInt(Area.KEY_AREA_UTILIZATION_TYPE);
                        boolean areaUtilizationTypeInherited = area.getBoolean(Area.KEY_AREA_UTILIZATION_TYPE_INHERITED);
                        String name = area.getString(Area.KEY_AREA_NAME);
                        int parentId = area.getInt(Area.KEY_AREA_PARENT_ID);

                        JSONArray areaJSONArray = area.getJSONArray(Area.KEY_AREA_POINTS);

                        if (areaJSONArray.length() > 0) {

                            for (int li = 0; li < areaJSONArray.length(); li++) {

                                JSONObject areaPoint = areaJSONArray.getJSONObject(li);

                                double X = areaPoint.getDouble(AreaPoint.KEY_AREA_X);
                                double Y = areaPoint.getDouble(AreaPoint.KEY_AREA_Y);
                                int polyIndex = areaPoint.getInt(AreaPoint.KEY_AREA_POLY_INDEX);

                                AreaPoint point = new AreaPoint(siteId, areaId, X, Y, polyIndex);

                                areaPointHandler.addRecordAreaPoint(point);
                            }

                        }
                        int minX = area.getInt(Area.KEY_AREA_MIN_X);
                        int minY = area.getInt(Area.KEY_AREA_MIN_Y);
                        int maxX = area.getInt(Area.KEY_AREA_MAX_X);
                        int maxY = area.getInt(Area.KEY_AREA_MAX_Y);
//not available in ah api
                        boolean deleted =false;
                        String type = area.getString(Area.KEY_AREA_TYPE);
                        //removed from the API after 7.4
                        //  boolean cutDown = area.getBoolean(Area.KEY_AREA_CUTDOWN);

                        String tableName = "area";// area.getString(Area.KEY_AREA_TABLE_NAME);

                        //Add Areas
                        areaHandler.addRecordArea(new Area(siteId, areaId, floorIndex, areaUtilizationType, areaUtilizationTypeInherited, name, parentId
                                , minX, minY, maxX, maxY, deleted, type, true, tableName));

                    }
                } catch (JSONException e1) {
                    mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
                    Log.e("parseMAPLocations", e1.getMessage());
                    e1.printStackTrace();
                } catch (Exception e1) {
                    mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
                    Log.e("parseMAPLocations", e1.getMessage());
                    e1.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainActivity.progressDialog.dismiss();
            SyncAlgorithmConfig syncAlgoConfig = new SyncAlgorithmConfig(mainActivity);
            syncAlgoConfig.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            //   Button btnSyncDataSite = (Button)  mainActivity.fragmentHeader.mfragmentView.findViewById(R.id.btn_sync_data);
           /* if(btnSyncDataSite != null)
                btnSyncDataSite.setEnabled(true);*/


   //         mainActivity.fragmentContent.fragmentRoomTransition.loadCampus(mainActivity.databaseManager);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mainActivity.progressDialog.setProgress(values[0]);
        }

    }

}
