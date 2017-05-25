package com.awarepoint.androidaccuracytest.iBeaconMask;

import java.util.List;

/**
 * Created by jlubawy on 7/13/2016.
 */
public class AwpIBeacon extends IBeacon {
    public static final UUID AWP_PROXIMITY_UUID = UUID.fromString("F56D9233-9ADF-48E2-902D-34A544DD1B82");

    private AwpLocationZone zone;

    public AwpIBeacon(String address, int rssi, List<AdvertisementData> advDataList, byte[] advData, long timestamp) {
        super(address, rssi, advDataList, advData, timestamp);
        this.zone = AwpLocationZone.valueOf((super.getMajor() >> 12) & 0xF);
    }

    public AwpIBeacon(byte[] advData, UUID proximityUuid, int major, int minor, byte measuredPower, AwpLocationZone zone) {
        super(advData, proximityUuid, major, minor, measuredPower);
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "AwpIBeacon{" +
                "proximityUuid=" + proximityUuid +
                ", major=" + major +
                ", minor=" + minor +
                ", measuredPower=" + measuredPower +
                ", zone=" + zone +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AwpIBeacon that = (AwpIBeacon) o;

        return zone == that.zone;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (zone != null ? zone.hashCode() : 0);
        return result;
    }

    public AwpLocationZone getZone() {
        return zone;
    }
}
