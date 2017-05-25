package com.awarepoint.androidaccuracytest.SyncData.LocationEngine;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfigNeighbors;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.RoomConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.RoomConfigNeighborsHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;
import com.awarepoint.androidaccuracytest.SyncData.SyncAreas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ureyes on 4/29/2016.
 */
public class SyncRoomConfig extends AsyncTask<Void, Integer, String> {

    int siteId;
    private String ApplianceIP;

    RoomConfigHandler roomConfigHandler;
    RoomConfigNeighborsHandler roomConfigNeighborsHandler;

    DatabaseManager databaseManager;
    MainActivity mainActivity;


    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";
       private String url_rooms = baseUrl + "/api/ble/rooms";

    public SyncRoomConfig(MainActivity mActivity) {

        this.mainActivity = mActivity;
        this.databaseManager = mActivity.databaseManager;

        this.siteId = mActivity.databaseManager.getSiteId();
        this.ApplianceIP = mActivity.databaseManager.getApplianceIP();

        roomConfigHandler = new RoomConfigHandler(databaseManager);
        roomConfigNeighborsHandler = new RoomConfigNeighborsHandler(databaseManager);
 }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.createSyncDialogProgress("Config4 SYNC");
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            //check if the areaId is already store
            return mainActivity.sh.makeServiceCall(url_rooms, ServiceHandler.GET);
         //   return mainActivity.httpsServiceHandler.makeServiceCall(wsUrl, HttpServiceHandler.GET, null);

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

            if (!jsonAreas.contains("<html>")) {
                ParseJsonRooms parseJsonRooms = new ParseJsonRooms(jsonAreas);
                parseJsonRooms.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(mainActivity, "Rest APIs not in Service or Timeout.. ", Toast.LENGTH_SHORT).show();
                mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);

            }

        }

    }


    // Get a JSON with all the areas for the specified SiteId
    public class ParseJsonRooms extends AsyncTask<Void, Integer, Void> {
        String jsonOutput;

        public ParseJsonRooms(String jsonOutput) {
            this.jsonOutput = jsonOutput;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String formatJsonRooms = "{Rooms:" + jsonOutput + "}";
            if (!jsonOutput.isEmpty()) {
                try {
                      JSONObject jsonObject = new JSONObject(jsonOutput);
                    //           if(jsonObject.has(RoomConfig.TABLE_NAME)) {
                  //  JSONObject jsonObject = new JSONObject(formatJsonRooms);

                    final JSONArray rooms = jsonObject.getJSONObject("_embedded").getJSONArray("rooms");
                    //    JSONArray rooms = jsonObject.getJSONArray(RoomConfig.TABLE_NAME);

                    mainActivity.progressDialog.setMax(rooms.length());
                    for (int i = 0; i < rooms.length(); ++i) {
                        publishProgress(i);
                        JSONObject room = rooms.getJSONObject(i);

                        if (room.length() > 0) {

                            long roomId = room.getLong(RoomConfig.KEY_ROOM_ID);
                            String roomCategory = room.getString(RoomConfig.KEY_ROOM_CATEGORY);
                            Object h = room.get(RoomConfig.KEY_ROOM_HALLWAY_ID);
                            long hallwayId = 0;
                            if (!h.equals(null)) {

                                hallwayId = room.getLong(RoomConfig.KEY_ROOM_HALLWAY_ID);
                            }

                            if (room.has(RoomConfig.KEY_ROOM_NEIGHBOR_ROOMS)) {

                                JSONArray neighborIds = room.getJSONArray(RoomConfig.KEY_ROOM_NEIGHBOR_ROOMS);

                                long neigborId = 0;
                                for (int j = 0; j < neighborIds.length(); ++j) {
                                    neigborId = (int) neighborIds.get(j);

                                    roomConfigNeighborsHandler.addRecordRoomNeighbors(new RoomConfigNeighbors(siteId, roomId, neigborId));
                                }
                            }
                            roomConfigHandler.addRecordRooms(new RoomConfig(siteId, roomId, roomCategory, hallwayId));
                        }

                    }
                    //        }
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

            SyncAreas syncAreas = new SyncAreas(mainActivity);
            syncAreas.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mainActivity.progressDialog.setProgress(values[0]);
        }

    }


}
