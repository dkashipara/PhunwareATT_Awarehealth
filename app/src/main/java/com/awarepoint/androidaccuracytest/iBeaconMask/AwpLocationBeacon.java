package com.awarepoint.androidaccuracytest.iBeaconMask;

import java.util.List;

/**
 * Created by jlubawy on 7/7/2016.
 */
public class AwpLocationBeacon extends Beacon {
    public final static int TYPE = 0x00;

    private byte measuredPower;
    private AwpLocationZone zone;

    public AwpLocationBeacon(String address, int rssi, List<AdvertisementData> advDataList, byte[] advData, long timestamp) {
        super(address, rssi, advDataList, advData, timestamp);

        final AdvertisementData mfgData = Beacon.findAdvDataType(advDataList, Beacon.ADV_DATA_TYPE_MANUFACTURER_SPECIFIC_DATA);
        final byte[] mfgDataInts = mfgData.getData();

        this.measuredPower = mfgDataInts[4];
        this.zone = AwpLocationZone.valueOf(mfgDataInts[9]);
    }

    public AwpLocationBeacon(byte[] advData, byte measuredPower, AwpLocationZone zone) {
        super(advData);
        this.measuredPower = measuredPower;
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "AwpLocationBeacon{" +
                "measuredPower=" + measuredPower +
                ", zone=" + zone +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwpLocationBeacon that = (AwpLocationBeacon) o;

        if (measuredPower != that.measuredPower) return false;
        return zone == that.zone;

    }

    @Override
    public int hashCode() {
        int result = (int) measuredPower;
        result = 31 * result + (zone != null ? zone.hashCode() : 0);
        return result;
    }

    public int getMeasuredPower() {
        return measuredPower;
    }

    public AwpLocationZone getZone() {
        return zone;
    }
}
