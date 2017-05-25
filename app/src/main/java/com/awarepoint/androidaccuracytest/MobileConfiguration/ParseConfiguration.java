package com.awarepoint.androidaccuracytest.MobileConfiguration;

import com.awarepoint.locationengine.configuration.domain.device.BeaconConfig;
import com.awarepoint.locationengine.configuration.domain.device.BeaconType;
import com.awarepoint.locationengine.configuration.domain.device.BeaconZone;
import com.awarepoint.locationengine.configuration.domain.topography.FloorConfig;
import com.awarepoint.locationengine.configuration.domain.topography.RegionConfig;
import com.awarepoint.locationengine.configuration.domain.topography.RegionType;
import com.awarepoint.locationengine.configuration.domain.topography.RoomCategory;
import com.awarepoint.locationengine.configuration.domain.topography.RoomConfig;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ureyes on 12/21/2015.
 * Copyright Awarepoint 2015. All rights reserved.
 */
public class ParseConfiguration {
    private static final String TAG = "ParseConfiguration";


    public ParseConfiguration(){}

    public static Map<Long, RegionConfig> parseJsonRegionConfig(String jsonRegionConfig) throws MinimumPolygonVerticesException, ParseMobileLocationEngineConfigurationException {
        final String EMBEDDED = "_embedded";
        final String AREAS = "bleAreas";
        final String AREA_POINTS = "areaPoints";
        final String AREA_NAME = "name";
        final String AREA_PARENT_ID="parentId";
        final String AREA_TYPE="type";
        final String AREA_ID="id";

        final String REGIONS = "regionsConfig";
        final String REGION_ID="regionId";
        final String REGION_TYPE="regionType";
        final String REGION_PARENT_ID="parentId";
        final String REGION_NAME="regionName";
        final String REGION_VERTICES="vertices";

        Map<Long, RegionConfig> regionConfigMap = new HashMap<>();

        if (!jsonRegionConfig.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonRegionConfig);
                if(jsonObject.has(EMBEDDED) && (jsonObject.getJSONObject(EMBEDDED).has(AREAS))) {

                    JSONArray regions = jsonObject.getJSONObject(EMBEDDED).getJSONArray(AREAS);

                    for (int i = 0; i < regions.length(); ++i) {
                        List<ImmutablePair<Double, Double>> regionPolygon = new ArrayList<>();

                        JSONObject region = regions.getJSONObject(i);

                        if (region.length() > 0) {

                            long regionId = region.getLong(AREA_ID);
                            String regionTypeJson = region.getString(AREA_TYPE);
                            long parentId = region.getLong(AREA_PARENT_ID);
                            String regionName = region.getString(AREA_NAME);

                            RegionType areaType = RegionType.valueOf(regionTypeJson);

                            if (region.has(AREA_POINTS)) {

                                JSONArray areaPoints = region.getJSONArray(AREA_POINTS);

                                if (areaPoints.length() >= 3) {
                                    for (int j = 0; j < areaPoints.length(); ++j) {
                                        JSONObject areaPoint = areaPoints.getJSONObject(j);

                                        // parse x, y
                                        Double x = areaPoint.getDouble("x");
                                        Double y = areaPoint.getDouble("y");
                                        regionPolygon.add(new ImmutablePair<>(x, y));
                                    }
                                } else {
                                    throw new MinimumPolygonVerticesException();
                                    //break;
                                }
                            }

                            regionConfigMap.put(regionId, new RegionConfig(regionId, areaType, parentId, regionName, regionPolygon));
                        }
                    }
                }
            } catch (JSONException e1) {
                throw new ParseMobileLocationEngineConfigurationException(e1);
            } catch (Exception e1) {
                throw new ParseMobileLocationEngineConfigurationException(e1);
            }
        }
        return regionConfigMap;
    }

    public Map<Long, RoomConfig> parseJsonRoomConfig(String jsonRoomConfig) throws ParseMobileLocationEngineConfigurationException {

        final String EMBEDDED = "_embedded";
        final String ROOMS = "rooms";
        final String ROOM_ID = "id";
        final String ROOM_CATEGORY="category";
        final String ROOM_HALLWAY_ID="hallway";
        final String ROOM_NEIGHBOR="firstOrderRooms";

        Map<Long, RoomConfig> roomConfigMap = new HashMap<>();

        long roomId=0;
        RoomCategory category;
        Long hallwayId=null;

        if (!jsonRoomConfig.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonRoomConfig);
                if(jsonObject.has(EMBEDDED) && jsonObject.getJSONObject(EMBEDDED).has(ROOMS)) {

                    JSONArray rooms = jsonObject.getJSONObject(EMBEDDED).getJSONArray(ROOMS);

                    for (int i = 0; i < rooms.length(); ++i) {
                        List<Long> neighborRooms = new ArrayList<>();

                        JSONObject room = rooms.getJSONObject(i);
                        try {
                            roomId = room.getInt(ROOM_ID);
                        } catch(Exception exc) {
                            throw new InvalidRoomIdException("Invalid room " + ROOM_ID + " in JSON.");
                        }

                        try {
                            category = RoomCategory.valueOf(room.getString(ROOM_CATEGORY));
                        }
                        catch(Exception exc) {
                            //throw new InvalidRoomCategoryException("Error parsing room " + ROOM_CATEGORY + " in JSON.", exc);

                            //Default to hallway if there is a problem parsing hallway, because bad data was entered.
                            category = RoomCategory.HALLWAY;
                        }

                        try {
                            if (room.isNull(ROOM_HALLWAY_ID)) {
                                hallwayId = null;
                            }
                            else {
                                hallwayId = room.getLong(ROOM_HALLWAY_ID);
                            }
                        }
                        catch(Exception exc) {
                            throw new ParseMobileLocationEngineConfigurationException(exc, "Error parsing room hallway in JSON.");
                        }

                        JSONArray neighborRoomsId = room.getJSONArray(ROOM_NEIGHBOR);

                        if (neighborRoomsId.length() > 0) {
                            for (int li = 0; li < neighborRoomsId.length(); li++) {
                                neighborRooms.add(neighborRoomsId.getLong(li));
                            }

                        }

                        if (hallwayId == null)
                            hallwayId = (long)0;

                        roomConfigMap.put(roomId, new RoomConfig(roomId, category, hallwayId, neighborRooms));

                    }
                }
            } catch (JSONException e1) {
                throw new ParseMobileLocationEngineConfigurationException(e1);
            } catch (Exception e1){
                throw new ParseMobileLocationEngineConfigurationException(e1);

            }
        }

        return roomConfigMap;
    }

    public Map<Long, FloorConfig> parseJsonFloorConfig(String jsonFloorConfig) throws ParseMobileLocationEngineConfigurationException {
        final String FLOORS = "floors";
        final String FLOOR_ID = "id";
        final String SCALE ="scale";
        final String EMBEDDED = "_embedded";
        final String LATITUDE = "latitude";
        final String LONGITUDE = "longitude";
        final String LATITUDEOFFSET = "x";
        final String LONGITUDEOFFSET = "y";
        final String ANGLE = "angle";

        Map<Long, FloorConfig> floorConfigMap = new HashMap<>();

        long floorId=0;
        float scale =0;
        Double latitude = 0.0;
        Double longitude = 0.0;
        Double latitudeOffset = 0.0;
        Double longitudeOffset = 0.0;
        Double rotationAngle = 0.0;
        Set<ImmutablePair<Double, Double>> floorVertices = new HashSet<ImmutablePair<Double, Double>>();

        if (!jsonFloorConfig.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonFloorConfig);
                if(jsonObject.has(EMBEDDED) && jsonObject.getJSONObject(EMBEDDED).has(FLOORS)) {

                    JSONArray floors = jsonObject.getJSONObject((EMBEDDED)).getJSONArray(FLOORS);

                    for (int i = 0; i < floors.length(); ++i) {

                        JSONObject floor = floors.getJSONObject(i);

                        if (floor.length() > 0) {

                            floorId = floor.getLong(FLOOR_ID);
                            scale = (float) floor.getDouble(SCALE);

                            try {
                                latitude = floor.getDouble(LATITUDE);
                            }
                            catch (Exception exc) {}

                            try {
                                longitude = floor.getDouble(LONGITUDE);
                            }
                            catch (Exception exc) {}

                            try {
                                latitudeOffset = floor.getDouble(LATITUDEOFFSET);
                            }
                            catch (Exception exc) {}

                            try {
                                longitudeOffset = floor.getDouble(LONGITUDEOFFSET);
                            }
                            catch (Exception exc) {}

                            try {
                                rotationAngle = floor.getDouble(ANGLE);
                            }
                            catch (Exception exc) {}

                            floorConfigMap.put(floorId, new FloorConfig(floorId, scale, latitude, longitude, latitudeOffset, longitudeOffset,
                                    rotationAngle, floorVertices));
                        }

                    }
                }
            } catch (JSONException e1) {
                throw new ParseMobileLocationEngineConfigurationException(e1);
            } catch (Exception e1){
                throw new ParseMobileLocationEngineConfigurationException(e1);
            }
        }

        return floorConfigMap;
    }


    public Map<Long, BeaconConfig> parseJsonBeaconConfig(String jsonBeaconConfig) throws ParseMobileLocationEngineConfigurationException {
        final String BEACONS = "beacons";
        final String BEACON_ID = "id";
        final String BEACON_TYPE = "type";
        final String BEACON_ZONE = "zone";
        final String BEACON_POWER = "power";
        final String BEACON_INTERVAL = "interval";
        final String BEACON_X = "x";
        final String BEACON_Y = "y";
        final String BEACON_LAT = "latitude";
        final String BEACON_LONG = "longitude";
        final String BEACON_ROOM_ID = "areaId";
        final String BEACON_PAIRED_ID = "pairedPid";
        final String BEACON_FLOOR_ID = "floorId";
        final String EMBEDDED = "_embedded";
        final String BEACON_PLACED = "placed";

        Map<Long, BeaconConfig> beaconConfigMap = new HashMap<>();

        long beaconId = 0;
        BeaconType beaconType;
        BeaconZone beaconZone;
        int beaconPower=0;
        int beaconInterval=0;
        Double beaconX = 0.0;
        Double beaconY = 0.0;
        Double beaconLat = 0.0;
        Double beaconLong = 0.0;
        long beaconRoomId = 0;
        Long beaconPairedId = 0L;
        long beaconFloorId = 0;
        boolean beaconPlaced = false;

        if (!jsonBeaconConfig.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonBeaconConfig);
                if(jsonObject.has(EMBEDDED) && jsonObject.getJSONObject(EMBEDDED).has(BEACONS)) {

                    JSONArray beacons = jsonObject.getJSONObject(EMBEDDED).getJSONArray(BEACONS);
                    for (int i = 0; i < beacons.length(); ++i) {

                        JSONObject beaconsJSONObject = beacons.getJSONObject(i);

                        if (beaconsJSONObject.has(BEACON_ID)) {
                            beaconId = beaconsJSONObject.getLong(BEACON_ID);

                        } else {
                            throw new ParseMobileLocationEngineConfigurationException("Missing JSON field " + BEACON_ID);
                        }

                        beaconX = beaconsJSONObject.getDouble(BEACON_X);
                        beaconY = beaconsJSONObject.getDouble(BEACON_Y);
                        beaconLat = beaconsJSONObject.getDouble(BEACON_LAT);
                        beaconLong = beaconsJSONObject.getDouble(BEACON_LONG);
                        beaconRoomId = beaconsJSONObject.getLong(BEACON_ROOM_ID);

                        if (beaconsJSONObject.has(BEACON_PLACED)) {
                            beaconPlaced = beaconsJSONObject.getBoolean(BEACON_PLACED);
                        }

                        try {
                            beaconType = BeaconType.getBeaconType(beaconsJSONObject.getInt(BEACON_TYPE));
                        }
                        catch(Exception exc) {
                            throw new InvalidBeaconZoneException("Invalid beacon type " + BEACON_TYPE + " in JSON.", exc);
                        }

                        try {
                            beaconZone = BeaconZone.getBeaconZone(beaconsJSONObject.getInt(BEACON_ZONE));
                        }
                        catch(Exception exc) {
                            throw new InvalidBeaconZoneException("Invalid beacon zone " + BEACON_ZONE + " in JSON.", exc);
                        }

                        beaconPower = beaconsJSONObject.getInt(BEACON_POWER);
                        beaconInterval = beaconsJSONObject.getInt(BEACON_INTERVAL);
                        beaconPairedId = beaconsJSONObject.getLong(BEACON_PAIRED_ID);
                        beaconFloorId = beaconsJSONObject.getLong(BEACON_FLOOR_ID);

                        BeaconConfig beacon = new BeaconConfig(
                                beaconId, beaconType, beaconZone,
                                beaconPower, beaconInterval, beaconX, beaconY, beaconLat, beaconLong,
                                beaconRoomId, beaconFloorId, beaconPairedId, beaconPlaced);

                        beaconConfigMap.put(beaconId, beacon);

                    }
                }
            } catch (JSONException e1) {
                throw new ParseMobileLocationEngineConfigurationException(e1);
            } catch (Exception e1){
                throw new ParseMobileLocationEngineConfigurationException(e1);
            }
        }

        return beaconConfigMap;
    }

    public List<BleAlgorithmItemConfiguration> parseJsonBleAlgorithmConfig(String jsonBleAlgorithmConfig) throws ParseMobileLocationEngineConfigurationException {

        List<BleAlgorithmItemConfiguration> config;
        AlgorithmConfigParser parser = new AlgorithmConfigParser();

        config = parser.Parse(jsonBleAlgorithmConfig);

        return config;
    }

}
