package com.awarepoint.androidaccuracytest.MobileConfiguration;

import com.awarepoint.locationengine.configuration.domain.algorithm.AlgorithmConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(c)2016 Awarepoint. All rights reserved.
 */

public class AlgorithmConfigParser {

    final String useAlgorithmConfigPlatform = "Nexus_5X";
    final String useAlgorithmConfigType = "SITE_DEFAULT";

    final String KEY_TYPE_INTEGER = "INTEGER";
    final String KEY_TYPE_DOUBLE = "DOUBLE";
    final String KEY_TYPE_BOOLEAN = "BOOLEAN";
    final String KEY_TYPE_STRING = "STRING";

    final String EMBEDDED = "_embedded";
    final String ITEMS = "items";
    final String ITEM_ID = "id";
    final String ITEM_CONFIGTYPE = "configType";
    final String ITEM_PLATFORM = "platform";
    final String ITEM_PLATFORM_NAME = "name";
    final String ITEM_PLATFORM_ID = "id";
    final String ITEM_CONFIGVALUES = "configValues";
    final String ITEM_CONFIGVALUE_ID = "id";
    final String ITEM_CONFIGVALUE_VALUE = "value";
    final String ITEM_CONFIGVALUE_KEY = "key";
    final String ITEM_CONFIGVALUE_KEY_ID = "id";
    final String ITEM_CONFIGVALUE_KEY_NAME = "name";
    final String ITEM_CONFIGVALUE_KEY_TYPE = "keyType";
    final String ITEM_CONFIGVALUE_KEY_DESCRIPTION = "description";

    final String MOBILEINPUT_WINDOWSIZE = "mobileInput.windowSize";
    final String MOBILEINPUT_CALCULATIONINTERVAL = "mobileInput.calculationInterval";
    final String MOBILEINPUT_ISACCELEROMETERINPUTENABLED = "mobileInput.isAccelerometerInputEnabled";
    final String MOBILEINPUT_MOTIONDETECTIONTHRESHOLD = "mobileInput.motionDetectionThershold";
    final String SERVERINPUT_WINDOWSIZE = "serverInput.windowSize";
    final String OUTPUT_ISLATLONGNEEDED = "outPut.isLatLongNeeded";
    final String CORE_MINIMALRSSICOUNT = "core.minimalRSSICount";
    final String RAPIDROOM_LOUDESTMARGIN = "rapidRoom.loudestMargin";
    final String RAPIDROOM_ISUSINGMAXRSSI = "rapidRoom.isUsingMaxRSSI";
    final String FLOORSELECTOR_ISUSINGCLOSTBEACONHEARDLOGIC = "floorSelector.isUsingClosetBeaconHeardLogic";
    final String FLOORSELECTOR_NUMLOUDESTBEACONS = "floorSelector.numLoudestBeacons";
    final String RTLS_ISSTRONGBEACONENABLED = "rtls.isStrongBeaconEnabled";
    final String RTLS_STRONGBEACONRSSITHRESHOLD = "rtls.strongBeaconRSSIThreshold";
    final String RTLS_RSSIDISTANCECONVERTER_ROOMPATHLOSSEXPONENT = "rtls.rssiDistanceConverter.roomPathLossExponent";
    final String RTLS_RSSIDISTANCECONVERTER_HALLWAYPATHLOSSEXPONENT = "rtls.rssiDistanceConverter.hallwayPathLossExponent";
    final String RTLS_RSSIDISTANCECONVERTER_OPENSPACEPATHLOSSEXPONENT = "rtls.rssiDistanceConverter.openSpacePathLossExponent";
    final String RTLS_RSSIDISTANCECONVERTER_DEFAULTPATHLOSSEXPONENT = "rtls.rssiDistanceConverter.defaultPathLossExponent";
    final String WAYFINDING_LOUDESTMARGIN = "wayfinding.loudestMargin";
    final String CALM_FLOORSELECTOR_INMOTIONWEIGHT = "calm.FloorSelector.inMotionWeight";
    final String CALM_FLOORSELECTOR_OUTMOTIONWEIGHT = "calm.FloorSelector.outMotionWeight";
    final String CALM_ASSETRTLSINMOTIONWEIGHT = "calm.assetRTLSInMotionWeight";
    final String CALM_ASSETRTLSOUTMOTIONWEIGHT = "calm.assetRTLSOutMotionWeight";
    final String CALM_ASSETRAPIDROOMINMOTIONWEIGHT = "calm.assetRapidRoomInMotionWeight";
    final String CALM_ASSETRAPIDROOMOUTMOTIONWEIGHT = "calm.assetRapidRoomOutMotionWeight";
    final String CALM_ASSETEGRESSINMOTIONWEIGHT = "calm.assetEgressInMotionWeight";
    final String CALM_ASSETEGRESSOUTMOTIONWEIGHT = "calm.assetEgressOutMotionWeight";
    final String CALM_PATIENTRTLSINMOTIONWEIGHT = "calm.patientRTLSInMotionWeight";
    final String CALM_PATIENTRTLSOUTMOTIONWEIGHT = "calm.patientRTLSOutMotionWeight";
    final String CALM_PATIENTRAPIDROOMINMOTIONWEIGHT = "calm.patientRapidRoomInMotionWeight";
    final String CALM_PATIENTRAPIDROOMOUTMOTIONWEIGHT = "calm.patientRapidRoomOutMotionWeight";
    final String CALM_PATIENTEGRESSINMOTIONWEIGHT = "calm.patientEgressInMotionWeight";
    final String CALM_PATIENTEGRESSOUTMOTIONWEIGHT = "calm.patientEgressOutMotionWeight";
    final String CALM_PATIENTBEDBAYINMOTIONWEIGHT = "calm.patientBedBayInMotionWeight";
    final String CALM_PATIENTBEDBAYOUTMOTIONWEIGHT = "calm.patientBedBayOutMotionWeight";
    final String CALM_STAFFRTLSINMOTIONWEIGHT = "calm.staffRTLSInMotionWeight";
    final String CALM_STAFFRTLSOUTMOTIONWEIGHT = "calm.staffRTLSOutMotionWeight";
    final String CALM_STAFFRAPIDROOMINMOTIONWEIGHT = "calm.staffRapidRoomInMotionWeight";
    final String CALM_STAFFRAPIDROOMOUTMOTIONWEIGHT = "calm.staffRapidRoomOutMotionWeight";
    final String CALM_STAFFEGRESSINMOTIONWEIGHT = "calm.staffEgressInMotionWeight";
    final String CALM_STAFFEGRESSOUTMOTIONWEIGHT = "calm.staffEgressOutMotionWeight";
    final String CALM_STAFFBEDBAYINMOTIONWEIGHT = "calm.staffBedBayInMotionWeight";
    final String CALM_STAFFBEDBAYOUTMOTIONWEIGHT = "calm.staffBedBayOutMotionWeight";
    final String CALM_WAYFINDINGINMOTIONWEIGHT = "calm.wayFindingInMotionWeight";
    final String CALM_WAYFINDINGOUTMOTIONWEIGHT = "calm.wayFindingOutMotionWeight";
    final String LOG_LOGLEVEL = "log.logLevel";

    public List<BleAlgorithmItemConfiguration> Parse(String json) {

        List<BleAlgorithmItemConfiguration> config = new ArrayList<BleAlgorithmItemConfiguration>();

        try {

            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has(EMBEDDED)) {
                JSONObject embedded = jsonObject.getJSONObject(EMBEDDED);

                if (embedded.has(ITEMS)) {
                    JSONArray items = embedded.getJSONArray(ITEMS);

                    if (items.length() > 0) {
                        for (int itemIndex = 0; itemIndex < items.length(); itemIndex++) {

                            JSONObject jsonItem = (JSONObject)items.get(itemIndex);
                            JSONArray configValues = jsonItem.getJSONArray(ITEM_CONFIGVALUES);
                            JSONObject jsonPlatform = (JSONObject)jsonItem.getJSONObject(ITEM_PLATFORM);
                            BleAlgorithmItemConfiguration configItem = new BleAlgorithmItemConfiguration();

                            config.add(configItem);

                            configItem.setId(jsonItem.getInt(ITEM_ID));
                            configItem.setConfigType(jsonItem.getString(ITEM_CONFIGTYPE));
                            configItem.setPlatform(new BleAlgorithmPlatformConfiguration());
                            configItem.getPlatform().setId(jsonPlatform.getInt(ITEM_PLATFORM_ID));
                            configItem.getPlatform().setName(jsonPlatform.getString(ITEM_PLATFORM_NAME));

                            if (configItem.getPlatform().getName().toLowerCase().equals(useAlgorithmConfigPlatform.toLowerCase())
                                    && configItem.getConfigType().toLowerCase().equals(useAlgorithmConfigType.toLowerCase())) {
                                if (configValues.length() > 0) {
                                    for (int configValueIndex = 0; configValueIndex < configValues.length(); configValueIndex++) {

                                        JSONObject jsonConfigValue = (JSONObject) configValues.get(configValueIndex);
                                        JSONObject jsonConfigValueKey = jsonConfigValue.getJSONObject(ITEM_CONFIGVALUE_KEY);
                                        String configValueKeyName = jsonConfigValueKey.getString(ITEM_CONFIGVALUE_KEY_NAME);
                                        String configValueKeyType = jsonConfigValueKey.getString(ITEM_CONFIGVALUE_KEY_TYPE);

                                        if (configValueKeyType.toUpperCase().equals(KEY_TYPE_INTEGER))
                                            configItem.getConfigValues().add(new AlgorithmConfig(configValueKeyName, jsonConfigValue.getInt(ITEM_CONFIGVALUE_VALUE)));

                                        if (configValueKeyType.toUpperCase().equals(KEY_TYPE_DOUBLE))
                                            configItem.getConfigValues().add(new AlgorithmConfig(configValueKeyName, jsonConfigValue.getDouble(ITEM_CONFIGVALUE_VALUE)));

                                        if (configValueKeyType.toUpperCase().equals(KEY_TYPE_BOOLEAN))
                                            configItem.getConfigValues().add(new AlgorithmConfig(configValueKeyName, jsonConfigValue.getBoolean(ITEM_CONFIGVALUE_VALUE)));

                                        if (configValueKeyType.toUpperCase().equals(KEY_TYPE_STRING))
                                            configItem.getConfigValues().add(new AlgorithmConfig(configValueKeyName, jsonConfigValue.getString(ITEM_CONFIGVALUE_VALUE)));

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        catch (JSONException jsonExc) {

        }
        catch (Exception exc) {

        }

        return config;
    }

}
