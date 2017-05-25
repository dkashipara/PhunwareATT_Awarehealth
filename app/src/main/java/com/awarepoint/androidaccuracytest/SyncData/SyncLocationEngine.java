package com.awarepoint.androidaccuracytest.SyncData;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.AlgorithmConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.BeaconConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfigVertices;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfigNeighbors;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.AreaPoint;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.R;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ureyes on 5/2/2016.
 */
public class SyncLocationEngine extends AsyncTask<Void, Integer, Boolean> {

    boolean hasError = false;
    String hasErrorMessage = "";


    MainActivity mainActivity;

    public SyncLocationEngine(MainActivity mActivity) {
        mainActivity = mActivity;
    }


    @Override
    protected void onPreExecute() {
        mainActivity.createSyncDialogProgress(" Sending Config to LE ");
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mainActivity.progressDialog.setProgress(values[0]);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {

            List<BeaconConfig> beaconConfigList = mainActivity.databaseHandler.beaconConfigHandler.checkTableBeacons();
            mainActivity.progressDialog.setMax(beaconConfigList.size());

            if (beaconConfigList.size() > 0) {

                for (int li = 0; li < beaconConfigList.size(); li++) {

                    publishProgress(li);

                    BeaconConfig beacon = beaconConfigList.get(li);

                    long beaconId = beacon.getBeaconId();
                    int beaconType = beacon.getBeaconType();
                    int beaconZone = beacon.getBeaconZone();
                    int beaconPower = beacon.getBeaconPower();
                    int beaconInterval = beacon.getBeaconInterval();
                    Double beaconX = beacon.getBeaconX();
                    Double beaconY = beacon.getBeaconY();
                    Double beaconLat = beacon.getBeaconLat();
                    Double beaconLong = beacon.getBeaconLong();
                    long beaconRoomId = beacon.getBeaconRoomId();
                    Long beaconPairedId = beacon.getBeaconPairedId();
                    long beaconFloorId = beacon.getBeaconFloorId();
                    String beaconTypestring = getbeacontype(beaconType);
                    String beaconZonestring = getbeaconZone(beaconZone);

                    mainActivity.configurationHandler.addBeaconConfig(beaconId, beaconTypestring, beaconZonestring, beaconPower, beaconInterval, beaconX, beaconY
                            , beaconLat, beaconLong, beaconRoomId, beaconFloorId, beaconPairedId, true);
                }

            }
        } catch (Exception err) {
            hasError = true;
            hasErrorMessage += "  Get LE BeaconConfig exception: " + err.getMessage();
        }


        try {

            List<Area> regionConfigList = mainActivity.databaseHandler.areaHandler.checkTableArea();
            mainActivity.progressDialog.setMax(regionConfigList.size());

            if (regionConfigList.size() > 0) {
                for (int li = 0; li < regionConfigList.size(); li++) {

                    publishProgress(li);

                    Area region = regionConfigList.get(li);

                    long regionId = region.getAreaId();
                    String regionType = region.getType();
                    long parentId = region.getParentId();
                    String regionName = region.getName();

                    List<ImmutablePair<Double, Double>> regionPolygonList = new ArrayList<>();

                    List<AreaPoint> regionConfigVerticesList = mainActivity.databaseHandler.areaPointHandler.checkTableAreaPointForAreaId(regionId);

                    if (regionConfigVerticesList.size() > 0) {
                        for (int lp = 0; lp < regionConfigVerticesList.size(); lp++) {
                            AreaPoint regionVertices = regionConfigVerticesList.get(lp);

                            regionPolygonList.add(new ImmutablePair<>(regionVertices.getX(), regionVertices.getY()));
                        }
                    }


                    mainActivity.configurationHandler.addRegionConfig(regionId, regionType, parentId, regionName, regionPolygonList);
                }
            }
        } catch (Exception err) {
            hasError = true;
            hasErrorMessage += "  Get LE RegionConfig exception: " + err.getMessage();
        }

        try {
            List<RoomConfig> roomConfigList = mainActivity.databaseHandler.roomConfigHandler.checkTableRooms();
            mainActivity.progressDialog.setMax(roomConfigList.size());

            if (roomConfigList.size() > 0) {

                for (int li = 0; li < roomConfigList.size(); li++) {
                    publishProgress(li);

                    RoomConfig room = roomConfigList.get(li);

                    long roomId = room.getRoomId();
                    String category = room.getRoomCategory();
                    long hallwayId = room.getHallwayId();

                    List<Long> neighborRoomsList = new ArrayList<>();

                    List<RoomConfigNeighbors> roomConfigNeighborsList = mainActivity.databaseHandler.roomConfigNeighborsHandler.checkTableRoomNeighbors(roomId);

                    if (roomConfigNeighborsList.size() > 0) {
                        for (int lp = 0; lp < roomConfigNeighborsList.size(); lp++) {
                            RoomConfigNeighbors roomConfigNeighbors = roomConfigNeighborsList.get(lp);

                            neighborRoomsList.add(roomConfigNeighbors.getNeighborId());
                        }
                    }

                    if (!category.equals(""))
                        mainActivity.configurationHandler.addRoomConfig(roomId, category, hallwayId, neighborRoomsList);

                }
            }
        } catch (Exception err) {
            hasError = true;
            hasErrorMessage += "  Get LE RoomConfig exception: " + err.getMessage();
        }

//adding algorithm configuration to full config request from the local db
        try {
            List<AlgorithmConfig> algoConfList = mainActivity.databaseHandler.algoConfHandler.checkTableAlgoConf();
            if (algoConfList.size() > 0) {
                // hasAlgoConfig = true;
                for (int li = 0; li < algoConfList.size(); li++) {
                    AlgorithmConfig alconf = algoConfList.get(li);
                    String key = alconf.getName();
                    String value = alconf.getKeyValue();

                    mainActivity.configurationHandler.addAlgorithmConfig(key, (Serializable) value);

                }
            }
        } catch (Exception e) {
            Log.i(mainActivity.TAG, "Error checkRecordAlgoConf:  " + e.getMessage());
            e.printStackTrace();
        }
        try {

            List<FloorConfig> floorConfigList = mainActivity.databaseHandler.floorConfigHandler.checkTableFloorConfig();
            mainActivity.progressDialog.setMax(floorConfigList.size());


            if (floorConfigList.size() > 0) {
                for (int li = 0; li < floorConfigList.size(); li++) {
                    publishProgress(li);

                    FloorConfig floor = floorConfigList.get(li);

                    long floorId = floor.getFloorId();
                    float scale = floor.getScale();
                    double latitude = floor.getLatitude();
                    double longitude = floor.getLongitude();
                    double latitudeOffset = floor.getLatitudeOffset();
                    double longitudeOffset = floor.getLongitudeOffset();
                    double rotationAngle = floor.getRotationAngle();

                    long height = floor.getHeight();

                    Set<ImmutablePair<Double, Double>> floorPolygonList = new HashSet<ImmutablePair<Double, Double>>();

                    List<FloorConfigVertices> floorConfigVerticesList = mainActivity.databaseHandler.floorConfigVerticesHandler.checkTableFloorsVertices(floorId);

                    if (floorConfigVerticesList.size() > 0) {
                        for (int lp = 0; lp < floorConfigVerticesList.size(); lp++) {
                            FloorConfigVertices floorVertice = floorConfigVerticesList.get(lp);

                            floorPolygonList.add(new ImmutablePair<>(floorVertice.getX(), floorVertice.getY()));
                        }
                    }
                    mainActivity.FloorHeightMap.put(floorId, height);

                    mainActivity.configurationHandler.addFloorConfig(floorId, scale, latitude, longitude, latitudeOffset, longitudeOffset, rotationAngle, floorPolygonList);
                }
            }

        } catch (Exception err) {
            hasError = true;
            hasErrorMessage += "  Get LE FloorConfig exception: " + err.getMessage();

        }


        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (hasError) {
            Toast.makeText(mainActivity, hasErrorMessage, Toast.LENGTH_LONG).show();
        }
        mainActivity.progressDialog.dismiss();

        Button btnSendDataLE = (Button) mainActivity.fragmentHeader.mfragmentView.findViewById(R.id.btn_sendLEdata);
        if (btnSendDataLE != null)
            btnSendDataLE.setEnabled(true);

        Spinner spinnerFloor = (Spinner) mainActivity.fragmentContent.fragmentRoomTransition.mfragmentView.findViewById(R.id.spinner_region_floor);
        if (spinnerFloor != null)
            spinnerFloor.setSelection(0);
    }


    private String getbeacontype(int int1) {
        String type = null;
        if (int1 == 0)
            type = "DIRECTIONAL";
        if (int1 == 1)
            type = "OMNI";
        return type;
    }

    private String getbeaconZone(int int1) {
        String zone = null;
        if (int1 == 0)
            zone = "DISABLED";
        if (int1 == 1)
            zone = "RTLS";
        if (int1 == 2)
            zone = "EGREES";
        if (int1 == 3)
            zone = "FASTROOM";
        if (int1 == 4)
            zone = "BEDBAY";
        if (int1 == 5)
            zone = "DONGLE";
        if (int1 == 6)
            zone = "PROXIMITY";
        if (int1 == 7)
            zone = "WAYFINDING";
        return zone;
    }
}
