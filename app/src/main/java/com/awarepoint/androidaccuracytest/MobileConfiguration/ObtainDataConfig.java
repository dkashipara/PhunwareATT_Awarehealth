package com.awarepoint.androidaccuracytest.MobileConfiguration;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.OAuth2Client;
import com.awarepoint.androidaccuracytest.OAuth2Response;
import com.awarepoint.androidaccuracytest.OAuthParameters;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;
import com.awarepoint.ble.api.manager.layout.LocalCacheManagerLayout;
import com.awarepoint.locationengine.configuration.domain.algorithm.AlgorithmConfig;
import com.awarepoint.locationengine.configuration.domain.device.BeaconConfig;
import com.awarepoint.locationengine.configuration.domain.topography.FloorConfig;
import com.awarepoint.locationengine.configuration.domain.topography.RegionConfig;
import com.awarepoint.locationengine.configuration.domain.topography.RoomConfig;
import com.awarepoint.locationengine.configuration.domain.update.ConfigurationUpdate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ureyes on 12/18/2015.
 * Copyright Awarepoint 2015. All rights reserved.
 */
public class ObtainDataConfig {

    public String jsonStrRegions;
    public String jsonStrRooms;
    public String jsonStrFloors;
    public String jsonStrBeacons;
    public String jsonStrBleAlgorithmConfig;

    private boolean hasAreas;
    private boolean hasBeacons;
    private boolean hasRooms;
    private boolean hasFloors;
    private boolean hasBleAlgorithmConfig;

    ServiceHandler sh;

    private Map<Long, RegionConfig> regionConfigMap;
    private Map<Long, RoomConfig> roomConfigMap;
    private Map<Long, BeaconConfig> beaconConfigMap;
    private Map<Long, FloorConfig> floorConfigMap;
    private List<BleAlgorithmItemConfiguration> bleAlgorithmConfig;

    //OAuth URL and data. In your application, you will most likely get the
    //username and password via a UI screen.
    private String oauthUrl = "https://oauthservice-phun.ahealth.awarepoint.com/oauth/token"; //replace for your target environment
    private String oauthUserName = "phunware"; //replace with your own
    private String oauthPassword = "uUtGY912mN8"; //replace with your own
    private String oauthApiKey = "phunwareapikey"; //replace with your own
    private OAuth2Response oauthResponse = null;

    //API URLs
    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";
    private String url_regions = baseUrl + "/api/ble/areas";
    private String url_beacons = baseUrl + "/api/ble/beacons";
    private String url_rooms = baseUrl + "/api/ble/rooms";
    private String url_floors = baseUrl + "/api/ble/floors";
    private String url_ble_algorithm_config = baseUrl + "/api/ble/algorithmConfig";

    private LocalCacheManagerLayout cacheManagerLayout;
    private ParseConfiguration parseConfiguration;

    MainActivity mainActivity;
    Context context;

    public ObtainDataConfig(MainActivity activity){
        this.mainActivity = activity;
        this.context = mainActivity.getApplicationContext();

        try {
            parseConfiguration = new ParseConfiguration();
            cacheManagerLayout = new LocalCacheManagerLayout();
        }
        catch (Exception exc) {

        }
    }

    public void getCacheManagerLayout(){
            new GetOAuthToken().execute();
    }







    public class GetOAuthToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            OAuth2Client oauthClient = new OAuth2Client();
            OAuthParameters params = new OAuthParameters(oauthUrl, oauthApiKey, oauthUserName, oauthPassword);

            try {
                oauthResponse = oauthClient.SubmitRequest(params);

                //Create new service handler for other HTTP calls to the API to use
                //and pass the OAuth token in the constructor so every API call
                //can use it.
                sh = new ServiceHandler(oauthResponse.getAccessToken());
            }
            catch (Exception exc)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            new GetRegionsConfig().execute();

        }

    }






    public class GetRegionsConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            //mainActivity.writeText("initiated GetRegionsConfig()");

            jsonStrRegions = openJsonFile_InternalStorage("regions.json");

            try {
                if (jsonStrRegions.isEmpty()) {  // if area.json is stored in app
                    hasAreas = false;
                    jsonStrRegions = sh.makeServiceCall(url_regions, ServiceHandler.GET);
                } else {
                    hasAreas = true;
                }
            }catch(Exception err){
                Log.e("GetRegionsConfig",  "Web Service: "+url_regions+" execution failed: " +err.getMessage());
                err.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStrRegions == null)
                jsonStrRegions = "";

            if (!jsonStrRegions.isEmpty() && jsonStrRegions.substring(0,6) !="<html>") {
                Log.d("JSON Areas Response: ", jsonStrRegions);
                try {
                    regionConfigMap = ParseConfiguration.parseJsonRegionConfig(jsonStrRegions);
                }
                catch (Exception exc) {
                    Log.e("ServiceHandler", "Problem parsing area JSON in to regionConfigMap. Error: " + exc.getMessage(), exc);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any area data from the url");
            }

            if (regionConfigMap != null) {
                if (regionConfigMap.size() > 0) {
                    Set<RegionConfig> list_areaLocations = new HashSet<>(regionConfigMap.values());

                    cacheManagerLayout.handleRegionConfig(ConfigurationUpdate.create(RegionConfig.class, list_areaLocations));

                    if (!hasAreas)
                        saveJsonFile_InternalStorage("regions.json", jsonStrRegions);

                    new GetRoomsConfig().execute();
                }
            }

        }

    }

    private class GetRoomsConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            jsonStrRooms =  openJsonFile_InternalStorage("rooms.json");

            try{
                if(jsonStrRooms.isEmpty()) {
                    hasRooms = false;
                    //mainActivity.writeText("GetRoomsConfig from web service");
                    jsonStrRooms = sh.makeServiceCall(url_rooms, ServiceHandler.GET);
                }else{
                    //mainActivity.writeText("GetRoomsConfig from local storage");
                    hasRooms = true;
                }

            }catch(Exception err){
                //mainActivity.writeText("GetRoomsConfig Web Service: " + url_rooms + " execution failed: " + err.getMessage());
                Log.e("GetRegionsConfig",  "Web Service: "+url_rooms+" execution failed: " +err.getMessage());
                err.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStrRooms== null )
                jsonStrRooms = "";

            if (!jsonStrRooms.isEmpty()) {
                Log.d("JSON Rooms Response: ", jsonStrRooms);
                try {
                    roomConfigMap = parseConfiguration.parseJsonRoomConfig(jsonStrRooms);
                }
                catch (Exception exc) {
                    Log.e("ServiceHandler", "Problem parsing room JSON in to roomConfigMap. Error: " + exc.getMessage(), exc);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any room data from the url");
            }

            if(roomConfigMap !=null) {
                if (roomConfigMap.size() > 0) {
                    Set<RoomConfig> list_roomLocations = new HashSet<>(roomConfigMap.values());

                    cacheManagerLayout.handleRoomConfig(ConfigurationUpdate.create(RoomConfig.class, list_roomLocations));

                    if (!hasRooms)
                        saveJsonFile_InternalStorage("rooms.json", jsonStrRooms);

                    new GetFloorsConfig().execute();
                }
            }


        }

    }

    private class GetFloorsConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            jsonStrFloors =  openJsonFile_InternalStorage("floors.json");

            try{
                if(jsonStrFloors.isEmpty()) {
                    hasFloors = false;
                    jsonStrFloors = sh.makeServiceCall(url_floors, ServiceHandler.GET);
                }else{
                    hasFloors = true;
                }

            }catch(Exception err){
                Log.e("GetFloorsConfig", "Web Service: " + url_floors + " execution failed: " + err.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStrFloors== null )
                jsonStrFloors = "";

            if (!jsonStrFloors.isEmpty()) {
                Log.d("JSON Floors Response: ", jsonStrFloors);
                try {
                    floorConfigMap = parseConfiguration.parseJsonFloorConfig(jsonStrFloors);
                }
                catch (Exception exc) {
                    Log.e("ServiceHandler", "Problem parsing floor JSON in to floorConfigMap. Error: " + exc.getMessage(), exc);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any room data from the url");
            }

            if(floorConfigMap !=null) {
                if (floorConfigMap.size() > 0) {
                    Set<FloorConfig> list_floorLocations = new HashSet<>(floorConfigMap.values());

                    cacheManagerLayout.handleFloorConfig(ConfigurationUpdate.create(FloorConfig.class, list_floorLocations));

                    if (!hasFloors)
                        saveJsonFile_InternalStorage("floors.json", jsonStrFloors);

                    new GetBeaconsConfig().execute();
                }
            }


        }

    }

    private class GetBeaconsConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            jsonStrBeacons =  openJsonFile_InternalStorage("beacons.json");

            try{
                if(jsonStrBeacons.isEmpty()) {
                    hasBeacons = false;
                    jsonStrBeacons = sh.makeServiceCall(url_beacons, ServiceHandler.GET);
                }else{
                    hasBeacons = true;
                }

            }catch(Exception err){
                Log.e("GetRegionsConfig", "Web Service: "+url_beacons+" execution failed: " +err.getMessage());
                err.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStrBeacons== null )
                jsonStrBeacons = "";

            if (!jsonStrBeacons.isEmpty()) {

                Log.d("JSON Beacons Response: ", jsonStrBeacons);
                try {
                    beaconConfigMap = parseConfiguration.parseJsonBeaconConfig(jsonStrBeacons);
                }
                catch (Exception exc) {
                    Log.e("ServiceHandler", "Problem parsing beacon JSON in to beaconConfigMap. Error: " + exc.getMessage(), exc);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any beacon data from the url");
            }

            if(beaconConfigMap !=null) {

                if (beaconConfigMap.size() > 0) {
                    Set<BeaconConfig> list_beaconLocations = new HashSet<>(beaconConfigMap.values());

                    cacheManagerLayout.handleBeaconConfig(ConfigurationUpdate.create(BeaconConfig.class, list_beaconLocations));

                    if (!hasBeacons)
                        saveJsonFile_InternalStorage("beacons.json", jsonStrBeacons);

                    new GetBleAlgorithmConfig().execute();
                }
            }
        }
    }

    private class GetBleAlgorithmConfig extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            jsonStrBleAlgorithmConfig =  openJsonFile_InternalStorage("ble-algorithm.json");

            try{
                if(jsonStrBleAlgorithmConfig.isEmpty()) {
                    hasBleAlgorithmConfig = false;
                    jsonStrBleAlgorithmConfig = sh.makeServiceCall(url_ble_algorithm_config, ServiceHandler.GET);
                }else{
                    hasBleAlgorithmConfig = true;
                }

            }catch(Exception err){
                Log.e("GetRegionsConfig", "Web Service: "+url_ble_algorithm_config+" execution failed: " +err.getMessage());
                err.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStrBleAlgorithmConfig== null )
                jsonStrBleAlgorithmConfig = "";

            if (!jsonStrBleAlgorithmConfig.isEmpty()) {

                Log.d("JSON Config Response: ", jsonStrBleAlgorithmConfig);
                try {
                    bleAlgorithmConfig = parseConfiguration.parseJsonBleAlgorithmConfig(jsonStrBleAlgorithmConfig);

                    Set<AlgorithmConfig> list_algoConfig = GetDeviceConfigItem(bleAlgorithmConfig);

                        cacheManagerLayout.handleAlgorithmConfig(ConfigurationUpdate.create(AlgorithmConfig.class, list_algoConfig));
                }
                catch (Exception exc) {
                    Log.e("ServiceHandler", "Problem parsing BLE algorithm JSON in to Set<AlgorithmConfig>. Error: " + exc.getMessage(), exc);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any BLE algorithm config data from the url");
            }

            //If there are beacons that were downloaded, then start listening for BLE scans.
            InitializeBeaconScanning();
        }
    }

    public Set<AlgorithmConfig> GetDeviceConfigItem(List<BleAlgorithmItemConfiguration> bleAlgorithmConfigList)
    {
        Set<AlgorithmConfig> list_algoConfig = new HashSet<AlgorithmConfig>();

        if (bleAlgorithmConfigList.size() > 0)
            for (int i = 0; i < bleAlgorithmConfigList.size(); i++) {

                BleAlgorithmItemConfiguration configItem = bleAlgorithmConfigList.get(i);

                //Get the config item for this device.
           //     if (configItem.getPlatform().getName().equals("Nexus_5X"))

                if (configItem.getPlatform().getName().equals("Nexus_5X")&&configItem.getConfigType().equals("SITE_DEFAULT")) {
                    list_algoConfig = configItem.getConfigValues();
                }

            }

        return list_algoConfig;
    }









    public void InitializeBeaconScanning()
    {
        if(beaconConfigMap !=null) {

            if (beaconConfigMap.size() > 0) {
                Set<BeaconConfig> list_beaconLocations = new HashSet<>(beaconConfigMap.values());

                cacheManagerLayout.handleBeaconConfig(ConfigurationUpdate.create(BeaconConfig.class, list_beaconLocations));

                if (!hasBeacons)
                    saveJsonFile_InternalStorage("beacons.json", jsonStrBeacons);

               // mainActivity.cacheManagerLayout = cacheManagerLayout;

                try {
        //            mainActivity.executeListener();
                }
                catch (Exception exc) {

                }
            }
        }
    }

    public void saveJsonFile_InternalStorage(String fileName, String jsonFile) {

        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(jsonFile.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String openJsonFile_InternalStorage(String fileName) {

            String jsonFile = "";
            try {
                FileInputStream fis = context.openFileInput(fileName);
                InputStreamReader InputRead = new InputStreamReader(fis);

                char[] inputBuffer= new char[10000];

                int charRead;
                String readstring;
                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    readstring= String.copyValueOf(inputBuffer,0,charRead);
                    jsonFile +=readstring;
                }

                InputRead.close();

            } catch (IOException err) {
                //Log.d("openJsonFile_InternalStorage", "No file stored in device: "+ err.getMessage());
                return jsonFile;
            } catch (Exception err) {
                //Log.d("openJsonFile_InternalStorage", "No file stored in device: " + err.getMessage());
                return jsonFile;
            }

            return jsonFile;
        }

}
