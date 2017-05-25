package com.awarepoint.androidaccuracytest.SyncData.LocationEngine;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfigVertices;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.FloorConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.FloorConfigVerticesHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ureyes on 4/29/2016.
 */
public class SyncFloorConfig extends AsyncTask<Void, Integer, String> {
    int siteId;
    private String ApplianceIP;

    FloorConfigHandler floorConfigHandler;
    FloorConfigVerticesHandler floorConfigVerticesHandler;

    DatabaseManager databaseManager;
    MainActivity mainActivity;


    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";
    private String url_floors = baseUrl + "/api/ble/floors";

    public SyncFloorConfig(MainActivity mActivity) {

        this.mainActivity = mActivity;
        this.databaseManager = mActivity.databaseManager;

        this.siteId = mActivity.databaseManager.getSiteId();
        this.ApplianceIP = mActivity.databaseManager.getApplianceIP();

        floorConfigHandler = new FloorConfigHandler(databaseManager);
        floorConfigVerticesHandler = new FloorConfigVerticesHandler(databaseManager);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.createSyncDialogProgress("Config2 SYNC");
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            //check if the areaId is already store
            return mainActivity.sh.makeServiceCall(url_floors, ServiceHandler.GET);
         //   return mainActivity.httpsServiceHandler.makeServiceCall(wsUrl, HttpServiceHandler.GET, null);

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
                ParseJsonFloors parseJsonFloors = new ParseJsonFloors(jsonOutput);
                parseJsonFloors.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(mainActivity, "Rest APIs not in Service or Timeout.. ", Toast.LENGTH_SHORT).show();
                mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);

            }
        }
    }


    // Get a JSON with all the areas for the specified SiteId
    public class ParseJsonFloors extends AsyncTask<Void, Integer, Void> {
        String jsonOutput;

        public ParseJsonFloors(String jsonOutput) {
            this.jsonOutput = jsonOutput;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String formatJsonFloors = "{Floors:" + jsonOutput + "}";
            if (!jsonOutput.isEmpty()) {
                try {
                         JSONObject jsonObject = new JSONObject(jsonOutput);
                  //  JSONObject jsonObject = new JSONObject(formatJsonFloors);

                    final JSONArray floors = jsonObject.getJSONObject("_embedded").getJSONArray("floors");

                    mainActivity.progressDialog.setMax(floors.length());
                    for (int i = 0; i < floors.length(); ++i) {
                        publishProgress(i);
                        JSONObject floor = floors.getJSONObject(i);

                        if (floor.length() > 0) {

                         //   JSONObject a = floor.getJSONObject(FloorConfig.MAP_KEY);
                       //     Log.d(mainActivity.TAG, "json mapkey: " + a);
                            long floorId = floor.getLong(FloorConfig.KEY_FLOOR_ID);

                            double scale = floor.getDouble(FloorConfig.KEY_FLOOR_SCALE);
                            long height = 2718;
                            Log.d(mainActivity.TAG, " floor " + floor);
                            //           Object o = floor.get(FloorConfig.KEY_FLOOR_LATNLONINFO);
                            double latitude = floor.getDouble(FloorConfig.KEY_FLOOR_LATITUDE);
                            double longitude =floor.getDouble(FloorConfig.KEY_FLOOR_LONGITUDE);
                            double latitudeOffset = floor.getDouble(FloorConfig.KEY_FLOOR_LATITUDE_OFFSET);
                            double longitudeOffset = floor.getDouble(FloorConfig.KEY_FLOOR_LONGITUDE_OFFSET);
                            double rotationAngle = floor.getDouble(FloorConfig.KEY_FLOOR_ROTATION_ANGLE);

                          /*  if (floor.isNull(FloorConfig.KEY_FLOOR_LATNLONINFO)) {
                                Log.d(mainActivity.TAG, "lat n lon is null");
                            } else {
                                JSONObject latnlon = floor.getJSONObject(FloorConfig.KEY_FLOOR_LATNLONINFO);
                                Log.d(mainActivity.TAG, "lat n lon " + latnlon.toString());
                                if (!(latnlon.isNull(FloorConfig.KEY_FLOOR_LATITUDE)))
                                    latitude = latnlon.getDouble(FloorConfig.KEY_FLOOR_LATITUDE);
                                if (!(latnlon.isNull(FloorConfig.KEY_FLOOR_LONGITUDE)))
                                    longitude = latnlon.getDouble(FloorConfig.KEY_FLOOR_LONGITUDE);
                                if (!(latnlon.isNull(FloorConfig.KEY_FLOOR_LATITUDE_OFFSET)))
                                    latitudeOffset = latnlon.getDouble(FloorConfig.KEY_FLOOR_LATITUDE_OFFSET);
                                if (!(latnlon.isNull(FloorConfig.KEY_FLOOR_LONGITUDE_OFFSET)))
                                    longitudeOffset = latnlon.getDouble(FloorConfig.KEY_FLOOR_LONGITUDE_OFFSET);
                                if (!(latnlon.isNull(FloorConfig.KEY_FLOOR_ROTATION_ANGLE)))
                                    rotationAngle = latnlon.getDouble(FloorConfig.KEY_FLOOR_ROTATION_ANGLE);
                            }*/


                            if (floor.has(FloorConfig.KEY_FLOOR_VERTICES)) {

                                JSONArray vertices = floor.getJSONArray(FloorConfig.KEY_FLOOR_VERTICES);

                                if (vertices.length() >= 3) {
                                    //iterate over each vertices and obtain x,y
                                    for (int j = 0; j < vertices.length(); ++j) {
                                        JSONObject vertice = vertices.getJSONObject(j);

                                        if (vertice.length() == 2) {
                                            // parse x, y
                                            Double x = vertice.getDouble("x");
                                            Double y = vertice.getDouble("y");

                                            floorConfigVerticesHandler.addRecordFloorsVertices(new FloorConfigVertices(siteId, floorId, x, y));

                                        } else {
                                            Log.w(mainActivity.TAG, "A vertex must contain exactly 2 arguments");
                                        }
                                    }
                                } else {
                                    Log.w(mainActivity.TAG, "Closed polygons need at least 2 vertices");
                                    break;
                                }
                            }
                            if (scale != 0) {
                                mainActivity.FloorHeightMap.put(floorId, height);
                                Log.d(mainActivity.TAG, "doInBackground:  id " + floorId + " h  " + height);
                            }
                            floorConfigHandler.addRecordFloorConfig(new FloorConfig(siteId, floorId, (float) scale, latitude, longitude, latitudeOffset, longitudeOffset, rotationAngle, height));
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

            SyncRoomConfig syncRoomConfig = new SyncRoomConfig(mainActivity);
            syncRoomConfig.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mainActivity.progressDialog.setProgress(values[0]);
        }

    }
}
