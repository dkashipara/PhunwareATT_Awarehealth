package com.awarepoint.androidaccuracytest.iBeaconMask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jlubawy on 7/12/2016.
 */
public enum AwpLocationZone {
    RTLS(0x01, "RTLS"),
    EGRESS(0x02, "Egress"),
    RAPID_ROOM(0x03, "Rapid Room"),
    BED_BAY(0x04, "Bed/Bay"),
    DONGLE(0x05, "Dongle"),
    PROXIMITY(0x06, "Proximity"),
    WAY_FINDING(0x07, "Way Finding"),

    /* Reserve -1 as a sentinel value to represent an invalid zone */
    INVALID(-1, "Invalid");

    private static Map<Integer, AwpLocationZone> map = new HashMap<>();

    private int zone;
    private String name;

    static {
        for (AwpLocationZone zoneEnum : AwpLocationZone.values()) {
            map.put(zoneEnum.zone, zoneEnum);
        }
    }

    AwpLocationZone(int zone, String name) {
        this.zone = zone;
        this.name = name;
    }

    public static AwpLocationZone valueOf(int zone) {
        if (zone != 0xFF && map.containsKey(zone)) {
            return map.get(zone);
        } else {
            return INVALID;
        }
    }

    public int getZone() {
        return this.zone;
    }

    public String toString() {
        return this.name + " (" + this.zone + ")";
    }
}
